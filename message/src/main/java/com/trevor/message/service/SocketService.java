package com.trevor.message.service;

import com.google.common.collect.Lists;
import com.trevor.common.bo.Player;
import com.trevor.common.bo.RedisConstant;
import com.trevor.common.bo.SocketResult;
import com.trevor.common.domain.mysql.User;
import com.trevor.common.service.RedisService;
import com.trevor.common.service.UserService;
import com.trevor.message.socket.NiuniuSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.websocket.RemoteEndpoint;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author trevor
 * @date 06/27/19 18:01
 */
@Slf4j
@Service
public class SocketService {

    public static ConcurrentHashMap<String , NiuniuSocket> sockets = new ConcurrentHashMap<>(2<<11);

    @Resource(name = "executor")
    private Executor executor;

    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

    @PreDestroy
    public void destory(){
        Iterator<NiuniuSocket> iterator = sockets.values().iterator();
        while (iterator.hasNext()) {
            NiuniuSocket socket = iterator.next();
            socket.flush();
            socket.stop();
        }
    }

    /**
     * fixedRate设置的上一个任务的开始时间到下一个任务开始时间的间隔
     * fixedDelay是设定上一个任务结束后多久执行下一个任务，也就是fixedDelay只关心上一任务的结束时间和下一任务的开始时间
     */
    @Scheduled(initialDelay = 1000 * 3 ,fixedDelay = 1000)
    public void checkRoom(){
        Iterator<NiuniuSocket> iterator = sockets.values().iterator();
        while (iterator.hasNext()) {
            NiuniuSocket socket = iterator.next();
            log.info("给玩家：" + socket.userId + "发消息");
            socket.flush();
        }
    }

    /**
     * 房间广播
     * @param roomId
     * @param res
     */
    public void broadcast(String roomId , SocketResult res ,Set<String> playerIds){
        for (String playId : playerIds) {
            sendToUserMessage(playId ,res ,roomId);
        }
    }

    /**
     * 给玩家发消息
     * @param playId
     * @param res
     */
    public void sendToUserMessage(String playId ,SocketResult res ,String roomId ){
        NiuniuSocket socket = sockets.get(playId);
        if (socket != null && socket.session != null && socket.session.isOpen()) {
            socket.sendMessage(res);
        }else {
            if (socket != null) {
                leave(roomId ,socket);
            }
        }
    }

    /**
     * 用户离开
     * @param roomId
     * @param socket
     */
    public void leave(String roomId , NiuniuSocket socket){
        if (sockets.containsKey(socket.userId)) {
            NiuniuSocket s = sockets.get(socket.userId);
            s.close(socket.session);
            sockets.remove(socket.userId);
            subRoomPlayer(roomId ,socket.userId);
        }
    }

    /**
     * 用户加入
     * @param roomId
     * @param socket
     */
    public void join(String roomId , NiuniuSocket socket){
        if (sockets.containsKey(socket.userId)) {
            NiuniuSocket s = sockets.get(socket.userId);
            sockets.remove(socket.userId);
            s.directSendMessage(new SocketResult(500) ,s.session);
            s.close(s.session);
        }
        sockets.put(socket.userId , socket);
    }

    /**
     * 减少玩家
     * @param roomId
     * @param userId
     */
    public void subRoomPlayer(String roomId ,String userId){
        //移除玩家id
        redisService.setDeleteMember(RedisConstant.ROOM_PLAYER + roomId ,userId);
        //删除消息通道
        redisService.delete(RedisConstant.MESSAGES_QUEUE + userId);
    }



}
