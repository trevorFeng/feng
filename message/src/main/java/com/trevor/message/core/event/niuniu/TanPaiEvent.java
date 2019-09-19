package com.trevor.message.core.event.niuniu;

import com.trevor.common.bo.SocketResult;
import com.trevor.common.enums.GameStatusEnum;
import com.trevor.message.bo.NiuniuData;
import com.trevor.message.bo.RoomData;
import com.trevor.message.bo.Task;
import com.trevor.message.core.event.BaseEvent;
import com.trevor.message.core.event.Event;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TanPaiEvent extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        String gameStatus = data.getGameStatus();
        String playerId = task.getPlayId();
        String roomId = task.getRoomId();
        String runingNum = data.getRuningNum();
        Set<String> readyPlayers = data.getReadyPlayMap().get(runingNum);
        Set<String> players = data.getPlayers();
        //状态信息
        if (!Objects.equals(gameStatus , GameStatusEnum.TAN_PAI_COUNT_DOWN_START.getCode())) {
            socketService.sendToUserMessage(playerId ,new SocketResult(-501) ,roomId);
            return;
        }
        if (!readyPlayers.contains(playerId)) {
            socketService.sendToUserMessage(playerId ,new SocketResult(-503) ,roomId);
            return;
        }
        data.getTanPaiMap().putIfAbsent(runingNum ,new HashSet<>());
        data.getTanPaiMap().get(runingNum).add(playerId);

        //广播摊牌的消息
        SocketResult socketResult = new SocketResult();
        socketResult.setHead(1014);
        socketResult.setUserId(playerId);
        socketService.broadcast(roomId ,socketResult ,players);

        Integer readyPlayerSize = readyPlayers.size();
        Integer tanPaiSize = data.getTanPaiMap().get(runingNum).size();

        if (Objects.equals(readyPlayerSize ,tanPaiSize)) {
            //删除摊牌倒计时监听器
            scheduleDispatch.removeCountDown(roomId);
            //添加发一张牌事件
            //taskQueue.addTask(new FaPai1Event(roomId));
        }
    }
}
