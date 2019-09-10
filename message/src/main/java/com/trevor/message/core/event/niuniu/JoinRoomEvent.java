package com.trevor.message.core.event.niuniu;

import com.trevor.common.bo.RedisConstant;
import com.trevor.common.bo.SocketResult;
import com.trevor.common.domain.mysql.Room;
import com.trevor.common.domain.mysql.User;
import com.trevor.common.enums.RoomTypeEnum;
import com.trevor.common.enums.SpecialEnum;
import com.trevor.common.util.JsonUtil;
import com.trevor.message.bo.NiuniuData;
import com.trevor.message.bo.RoomData;
import com.trevor.message.bo.Task;
import com.trevor.message.core.event.BaseEvent;
import com.trevor.message.core.event.Event;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class JoinRoomEvent extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        String roomId = task.getRoomId();
        String playerId = task.getPlayId();
        NiuniuData data = (NiuniuData) roomData;
        //房主是否开启好友管理功能
        Boolean isFriendManage = Objects.equals(task.getIsFriendManage() , Boolean.TRUE);
        List<Integer> special = data.getSpecial();
        //开通
        if (isFriendManage) {
            //配置仅限好友
            if (special.contains(SpecialEnum.JUST_FRIENDS.getCode())) {
                //不是房主的好友
                if (!task.getRoomAuthFriendAllow()) {
                    socketService.sendMessage(playerId ,new SocketResult(508) ,roomId);
                    return;
                    //是房主的好友
                }else {
                    return this.dealCanSee(user ,special ,room);
                }
            }
            //未配置仅限好友
            else {
                return this.dealCanSee(user ,special ,room);
            }
            // 未开通
        }else {
            return this.dealCanSee( user ,special ,room);
        }
    }

    /**
     * 处理是否可以观战
     * @throws IOException
     */
    private SocketResult dealCanSee(User user, List<Integer> special , Room room){
        SocketResult socketResult = new SocketResult();
        socketResult.setUserId(String.valueOf(user.getId()));
        socketResult.setName(user.getAppName());
        socketResult.setPictureUrl(user.getAppPictureUrl());
        Boolean bo = redisService.getSetSize(RedisConstant.REAL_ROOM_PLAYER + room.getId()) <
                RoomTypeEnum.getRoomNumByType(
                        Integer.valueOf(
                                redisService.getHashValue(RedisConstant.BASE_ROOM_INFO + room.getId() , RedisConstant.ROOM_TYPE)));
        //允许观战
        if (special!= null && special.contains(SpecialEnum.CAN_SEE.getCode())) {
            if (bo) {
                socketResult.setIsChiGuaPeople(Boolean.FALSE);
            }else {
                socketResult.setIsChiGuaPeople(Boolean.TRUE);
            }
            return socketResult;
            //不允许观战
        }else {
            if (bo) {
                socketResult.setIsChiGuaPeople(Boolean.FALSE);
                return socketResult;
            }else {
                return new SocketResult(509);
            }

        }
    }
}
