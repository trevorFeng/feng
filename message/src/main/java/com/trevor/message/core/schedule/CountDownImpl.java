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
     * 房间类型
     */
    private String roomType;

    /**
     * 默认为5
     */
    private Integer time = 5;

    public CountDownImpl(String roomId, Integer time ,String roomType) {
        this.roomId = roomId;
        this.time = time;
        this.roomType = roomType;
    }

    @Override
    public void onCountDown() {
        if (time > 0) {
            Task task = new Task();
            task.setRoomId(roomId);
            task.setCountDown(time);
            task.setFlag(TaskFlag.COUNT_DOWN.toString());
            task.setRoomType(roomType);
            taskQueue.addTask(roomId ,task);
            time--;
        }
    }

    @Override
    public String getRoomId() {
        return roomId;
    }



}
