package com.trevor.message.core.schedule;

import com.trevor.message.bo.Task;
import com.trevor.message.bo.TaskFlag;
import com.trevor.message.core.TaskQueue;
import lombok.Data;

@Data
public class CountDownImpl implements CountDownListener{

    public static TaskQueue taskQueue;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 默认为5
     */
    private Integer time = 5;

    public CountDownImpl(String roomId, Integer time) {
        this.roomId = roomId;
        this.time = time;
    }

    @Override
    public void onCountDown() {
        if (time > 0) {
            Task task = Task.getNiuniuCountDown(time ,roomId);
            taskQueue.addTask(roomId ,task);
            time--;
        }
    }

    @Override
    public String getRoomId() {
        return roomId;
    }



}
