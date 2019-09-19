package com.trevor.message.core.event.niuniu;

import com.trevor.common.bo.SocketResult;
import com.trevor.common.enums.GameStatusEnum;
import com.trevor.message.bo.CountDownFlag;
import com.trevor.message.bo.NiuniuData;
import com.trevor.message.bo.RoomData;
import com.trevor.message.bo.Task;
import com.trevor.message.core.event.BaseEvent;
import com.trevor.message.core.event.Event;
import org.apache.el.lang.ELArithmetic;
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
        //改变房间状态
        if (Objects.equals(task.getCountDown() ,5)) {
            changeGameStatus(data ,task ,socketResult ,5);
        }else if (Objects.equals(task.getCountDown() ,1)) {
            changeGameStatus(data ,task ,socketResult ,1);
        }

        socketResult.setCountDown(task.getCountDown());
        //房间里的玩家
        Set<String> players = data.getPlayers();
        socketService.broadcast(roomId ,socketResult ,players);

        if (Objects.equals(task.getCountDown() ,1)) {
            //倒计时为1，删除倒计时监听器
            scheduleDispatch.removeCountDown(task.getRoomId());
            //准备的倒计时结束，加入发4张牌的事件
            if (Objects.equals(task.getNiuniuCountDownFg() , CountDownFlag.NIUNIU_READY)) {
                Task faPai4Task = Task.getNiuniuFaPai4(roomId);
                taskQueue.addTask(roomId ,faPai4Task);
            //抢庄的到技术结束，加入发1张牌的事件
            }else if (Objects.equals(task.getNiuniuCountDownFg() , CountDownFlag.NIUNIU_QIANG_ZHUANG)) {
                Task qiangZhuangTask = Task.getNiuniuFaPai4(roomId);
                taskQueue.addTask(roomId ,qiangZhuangTask);
            }
        }
    }

    /**
     * 改变房间状态
     * @param data
     * @param task
     * @param socketResult
     */
    private void changeGameStatus(NiuniuData data ,Task task ,SocketResult socketResult ,Integer time){
        if (time == 5) {
            if (Objects.equals(task.getNiuniuCountDownFg() , CountDownFlag.NIUNIU_READY)) {
                data.setGameStatus(GameStatusEnum.READY_COUNT_DOWN_START.getCode());
                socketResult.setGameStatus(GameStatusEnum.READY_COUNT_DOWN_START.getCode());
            }
        }else if (time == 1) {
            if (Objects.equals(task.getNiuniuCountDownFg() , CountDownFlag.NIUNIU_READY)) {
                data.setGameStatus(GameStatusEnum.READY_COUNT_DOWN_END.getCode());
                socketResult.setGameStatus(GameStatusEnum.READY_COUNT_DOWN_END.getCode());
            }
        }
    }


}
