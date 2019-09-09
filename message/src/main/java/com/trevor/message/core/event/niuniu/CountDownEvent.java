package com.trevor.message.core.event.niuniu;

import com.trevor.common.bo.SocketResult;
import com.trevor.message.bo.CountDownFlag;
import com.trevor.message.bo.NiuniuData;
import com.trevor.message.bo.RoomData;
import com.trevor.message.bo.Task;
import com.trevor.message.core.event.BaseEvent;
import com.trevor.message.core.event.Event;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class CountDownEvent extends BaseEvent implements Event {


    @Override
    public void execute(RoomData roomData, Task task) {
        String roomId = task.getRoomId();
        NiuniuData data = (NiuniuData) roomData;
        SocketResult socketResult = new SocketResult(1002);
        socketResult.setCountDown(task.getCountDown());
        //房间里的玩家
        Set<String> players = data.getPlayers();
        socketService.broadcast(roomId ,socketResult ,players);
        //倒计时为1，删除倒计时监听器
        if (Objects.equals(task.getCountDown() ,1)) {
            scheduleDispatch.removeCountDown(task.getRoomId());
        }
        //加入发一张牌的事件
        if (Objects.equals(task.getNiuniuCountDownFg() , CountDownFlag.NIUNIU_READY)) {
            Task faPai1Task = new Task();
            faPai1Task.setRoomType(data.getRoomType());
            faPai1Task.setFlag();
            taskQueue.addTask(roomId ,faPai1Task);
        }
    }
}
