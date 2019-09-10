package com.trevor.message.bo;

import lombok.Data;

@Data
public class Task {

    /**
     * 游戏类型  1为13人牛牛，2为10人牛牛，3为6人牛牛 ，4为金花
     */
    private String roomType;

    /**
     * 游戏的任务标志
     */
    private String flag;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 玩家id
     */
    private String playId;

    /**
     * 搶莊的倍數
     */
    private Integer qiangZhuangBeiShu;

    /**
     * 下注的倍數
     */
    private Integer xiaZhuBeiShu;

    /**
     * 牛牛倒计时的标志
     */
    private String niuniuCountDownFg;

    /**
     * 倒计时的时间
     */
    private Integer countDown;

    public static Task getNiuniuCountDown(Integer time ,String roomId ,String roomType){
        Task task = new Task();
        task.setRoomId(roomId);
        task.setCountDown(time);
        task.setFlag(TaskFlag.COUNT_DOWN);
        task.setRoomType(roomType);
        return task;
    }

    public Task getNiuniuReadyTask(){

        return null;
    }

    public Task getNiuniuFaPai1Task(){
        
    }
}
