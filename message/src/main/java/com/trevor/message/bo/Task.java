package com.trevor.message.bo;

import com.trevor.common.domain.mysql.User;
import com.trevor.message.socket.BaseServer;
import com.trevor.message.socket.NiuniuSocket;
import lombok.Data;

@Data
public class Task {

    /**
     * 房间id
     */
    private String roomId;


    /**
     * 游戏的任务标志
     */
    private String flag;


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

    /**
     * 是否开启好友管理
     */
    private Boolean isFriendManage;

    /**
     * 加入房间的玩家是否是房主的好友
     */
    private Boolean roomAuthFriendAllow;

    private BaseServer socket;

    private User joinUser;

    public static Task getNiuniuJoinRoom(String roomId , Boolean isFriendManage , Boolean roomAuthFriendAllow , NiuniuSocket socket ,User joinUser){
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.JOIN_ROOM;
        task.playId = String.valueOf(joinUser.getId());
        task.isFriendManage = isFriendManage;
        task.roomAuthFriendAllow = roomAuthFriendAllow;
        task.socket = socket;
        task.joinUser = joinUser;
        return task;
    }

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
        return null;
    }
}
