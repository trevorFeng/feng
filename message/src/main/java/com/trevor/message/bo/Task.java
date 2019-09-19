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

    /**
     * 牛牛加入房间的任务
     * @param roomId
     * @param isFriendManage
     * @param roomAuthFriendAllow
     * @param socket
     * @param joinUser
     * @return
     */
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

    /**
     * 牛牛准备的任务
     * @param roomId
     * @return
     */
    public static Task getNiuniuReady(String roomId ,String userId){
        Task task = new Task();
        task.flag = TaskFlag.READY;
        task.roomId = roomId;
        task.playId = userId;
        return task;
    }

    public static Task getNiuniuCountDown(Integer time ,String roomId){
        Task task = new Task();
        task.roomId = roomId;
        task.countDown = time;
        task.flag = TaskFlag.COUNT_DOWN;
        return task;
    }


    public static Task getNiuniuFaPai4(String roomId){
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.FA_PAI_4;
        return task;
    }

    public static Task getNiuniuFaPai1(String roomId){
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.FA_PAI_1;
        return task;
    }

    public static Task getNiuniuQiangZhuang(String roomId ,String userId ,Integer qiangZhuangBeiShu){
        Task task = new Task();
        task.roomId = roomId;
        task.playId = userId;
        task.flag = TaskFlag.QIANG_ZHAUNG;
        task.qiangZhuangBeiShu = qiangZhuangBeiShu;
        return task;
    }

    public static Task getNiuniuSelectZhuangJia(String roomId){
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.SELECT_ZHUANG_JIA;
        return task;
    }

    public static Task getNiuniuXiaZhu(String roomId ,String userId ,Integer xiaZhuBeiShu){
        Task task = new Task();
        task.roomId = roomId;
        task.playId = userId;
        task.flag = TaskFlag.XIA_ZHU;
        task.xiaZhuBeiShu = xiaZhuBeiShu;
        return task;
    }

    public static Task getNiuniuTanPai(String roomId ,String userId){
        Task task = new Task();
        task.roomId = roomId;
        task.playId = userId;
        task.flag = TaskFlag.TAN_PAI;
        return task;
    }
}
