package com.trevor.message.socket;

import com.trevor.common.service.RedisService;
import com.trevor.message.core.TaskQueue;
import com.trevor.message.feign.FriendManageFeignResult;
import com.trevor.message.feign.RoomFeignResult;
import com.trevor.message.feign.UserFeignResult;
import com.trevor.message.socket.service.NiuniuService;
import com.trevor.message.service.SocketService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author trevor
 * @date 06/27/19 18:05
 */
@Component
public class BaseServer {

    protected static UserFeignResult userFeignResult;

    protected static SocketService socketService;

    protected static TaskQueue taskQueue;

    protected static NiuniuService niuniuService;

    protected static RoomFeignResult roomFeignResult;

    protected static FriendManageFeignResult friendManageFeignResult;

    protected static RedisService redisService;

    @Resource
    public void setRedisService(RedisService redisService){
        BaseServer.redisService = redisService;
    }


    @Resource
    public void setPlayService(FriendManageFeignResult friendManageFeignResult){
        BaseServer.friendManageFeignResult = friendManageFeignResult;
    }

    @Resource
    public void setUserService(UserFeignResult userFeignResult){
        BaseServer.userFeignResult = userFeignResult;
    }

    @Resource
    public void setRoomSocketService(SocketService socketService){
        BaseServer.socketService = socketService;
    }

    @Resource
    public void setRoomFeignResult(RoomFeignResult roomFeignResult){
        BaseServer.roomFeignResult = roomFeignResult;
    }

    @Resource
    public void setNiuniuService(NiuniuService niuniuService){
        BaseServer.niuniuService = niuniuService;
    }

    @Resource
    public void setTaskQueue(TaskQueue taskQueue){
        BaseServer.taskQueue = taskQueue;
    }

}
