package com.trevor.message.socket;

import com.google.common.collect.Maps;
import com.trevor.common.bo.PaiXing;
import com.trevor.common.bo.RedisConstant;
import com.trevor.common.bo.SocketResult;
import com.trevor.common.bo.WebKeys;
import com.trevor.common.domain.mysql.Room;
import com.trevor.common.domain.mysql.User;
import com.trevor.common.enums.FriendManageEnum;
import com.trevor.common.enums.GameStatusEnum;
import com.trevor.common.enums.RoomTypeEnum;
import com.trevor.common.enums.SpecialEnum;
import com.trevor.common.util.JsonUtil;
import com.trevor.common.util.NumberUtil;
import com.trevor.common.util.ObjectUtil;
import com.trevor.common.util.TokenUtil;
import com.trevor.message.bo.SocketMessage;
import com.trevor.message.bo.Task;
import com.trevor.message.decoder.NiuniuDecoder;
import com.trevor.message.encoder.NiuniuEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;



/**
 * 一句话描述该类作用:【牛牛服务端,每次建立链接就新建了一个对象】
 *
 * @author: trevor
 * @create: 2019-03-05 22:29
 **/
@ServerEndpoint(
        value = "/niuniu/{roomId}",
        encoders = {NiuniuEncoder.class},
        decoders = {NiuniuDecoder.class}
)
@Component
@Slf4j
public class NiuniuSocket extends BaseServer {

    public Session session;

    public String userId;

    public String roomId;

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        //roomId合法性检查
        Room room = roomFeignResult.findRoomById(roomId);
        if (room == null) {
            directSendMessage(new SocketResult(507) ,session);
            close(session);
            return;
        }
        //是否激活,0为未激活,1为激活，2为房间使用完成后关闭，3为房间未使用关闭
        if (!Objects.equals(room.getStatus() ,0) && !Objects.equals(room.getStatus() ,1)) {
            directSendMessage(new SocketResult(506) ,session);
            close(session);
            return;
        }
        //token合法性检查
        List<String> params = session.getRequestParameterMap().get(WebKeys.TOKEN);
        if (ObjectUtil.isEmpty(params)) {
            directSendMessage(new SocketResult(400) ,session);
            close(session);
            return;
        }
        String token = session.getRequestParameterMap().get(WebKeys.TOKEN).get(0);
        Map<String, Object> claims = TokenUtil.getClaimsFromToken(token);
        User user = userFeignResult.findByOpenId((String) claims.get(WebKeys.OPEN_ID) ,(String) claims.get("hash"));
        if (ObjectUtil.isEmpty(user)) {
            directSendMessage(new SocketResult(404) ,session);
            close(session);
            return;
        }
        //设置最大不活跃时间
        session.setMaxIdleTimeout(1000 * 60 * 45);
        this.roomId = roomId;
        this.userId = NumberUtil.formatString(user.getId());
        this.session = session;

        //是否开通好友管理功能
        Boolean isFriendManage = userFeignResult.isFriendManage(room.getRoomAuth());
        //玩家是否是房主的好友
        Boolean roomAuthFriendAllow = friendManageFeignResult.findRoomAuthFriendAllow(room.getRoomAuth(), user.getId());
        Task task = Task.getNiuniuJoinRoom(roomId ,isFriendManage ,roomAuthFriendAllow ,this ,user);
        taskQueue.addTask(roomId ,task);



        //检查房间是否满足人数要求
        SocketResult soc = checkRoom(room ,user);
        if (soc.getHead() != null) {
            directSendMessage(soc ,session);
            close(session);
            return;
        }
        soc.setHead(1000);
        /**
         * 广播新人加入
         */
        if (!soc.getIsChiGuaPeople()) {
            Map<String ,String> totalScoreMap = redisService.getMap(RedisConstant.TOTAL_SCORE + roomId);
            soc.setTotalScore(totalScoreMap.get(userId) == null ? "0" : totalScoreMap.get(userId));
            roomSocketService.broadcast(roomId ,soc);
        }
        //删除自己的消息队列
        redisService.delete(RedisConstant.MESSAGES_QUEUE + userId);
        //加入到广播的集合中
        roomSocketService.join(roomId ,this);
        /**
         * 给自己发消息
         */
        Map<String ,String> baseRoomInfoMap = redisService.getMap(RedisConstant.BASE_ROOM_INFO + roomId);
        Integer runingNum = NumberUtil.stringFormatInteger(baseRoomInfoMap.get(RedisConstant.RUNING_NUM));
        Integer totalNum =  NumberUtil.stringFormatInteger(baseRoomInfoMap.get(RedisConstant.TOTAL_NUM));
        //设置房间正在运行的局数
        soc.setRuningAndTotal((runingNum + 1) + "/" + totalNum);
        //不是吃瓜群众则加入到真正的玩家集合中并且删除自己的掉线状态
        if (!soc.getIsChiGuaPeople()) {
            //加入到真正的玩家中
            redisService.setAdd(RedisConstant.REAL_ROOM_PLAYER + roomId ,userId);
            //删除自己掉线状态
            redisService.setDeleteMember(RedisConstant.DIS_CONNECTION + roomId ,userId);
        }
        //设置掉线的玩家
        soc.setDisConnectionPlayerIds(redisService.getSetMembers(RedisConstant.DIS_CONNECTION + roomId));
        //设置真正的玩家
        soc.setPlayers(roomSocketService.getRealRoomPlayerCount(this.roomId));
        //发送房间状态消息
        welcome(roomId ,soc);
    }

    /**
     * 接受用户消息
     */
    @OnMessage
    public void onMessage(SocketMessage socketMessage){
        if (Objects.equals(socketMessage.getMessageCode() ,1)) {
            niuniuService.dealReadyMessage(roomId ,this);
        }else if (Objects.equals(socketMessage.getMessageCode() ,2)) {
            niuniuService.dealQiangZhuangMessage(roomId ,this ,socketMessage);
        }else if (Objects.equals(socketMessage.getMessageCode() ,3)) {
            niuniuService.dealXiaZhuMessage(roomId ,this ,socketMessage);
        }else if (Objects.equals(socketMessage.getMessageCode() ,4)) {
            niuniuService.dealTanPaiMessage(roomId, this);
//        }else if (Objects.equals(socketMessage.getMessageCode() ,5)) {
//            playService.dealShuoHuaMessage(roomId ,this ,socketMessage);
//        }else if (Objects.equals(socketMessage.getMessageCode() ,6)) {
//            playService.dealChangeToGuanZhan(roomId ,this);
//        }
        }
    }

    /**
     * 关闭连接调用的方法
     */
    @OnClose
    public void onClose(){
        if (!ObjectUtil.isEmpty(userId)) {
            redisService.delete(RedisConstant.MESSAGES_QUEUE + userId);
            //如果是真正的玩家则广播消息
            if (redisService.getSetMembers(RedisConstant.REAL_ROOM_PLAYER + roomId).contains(userId)) {
                redisService.setAdd(RedisConstant.DIS_CONNECTION + roomId ,userId);
                SocketResult res = new SocketResult(1001 ,userId);
                roomSocketService.broadcast(roomId ,res);
            }

        }
    }

    /**
     * 发生错误时调用的方法
     */
    @OnError
    public void onError(Throwable t){
        log.error(t.getMessage() ,t);
    }

    /**
     * 向客户端发消息
     * @param pack
     */
    public void sendMessage(SocketResult pack) {
        redisService.listRightPush(RedisConstant.MESSAGES_QUEUE + userId , JsonUtil.toJsonString(pack));
    }

    /**
     * 向客户端发消息
     * @param pack
     */
    public void directSendMessage(SocketResult pack , Session s) {
        RemoteEndpoint.Async async = s.getAsyncRemote();
        if (s.isOpen()) {
            async.sendObject(pack);
        } else {
            close(s);
        }
    }

    /**
     * 向客户端刷消息
     */
    public void flush(){
        try {
            List<String> messages = redisService.getListMembersAndDelete(RedisConstant.MESSAGES_QUEUE + userId);
            if (!messages.isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer("[");
                for (String mess : messages) {
                    stringBuffer.append(mess).append(",");
                }
                stringBuffer.setLength(stringBuffer.length() - 1);
                stringBuffer.append("]");
                synchronized (session) {
                    RemoteEndpoint.Async async = session.getAsyncRemote();
                    if (session.isOpen()) {
                        async.sendText(stringBuffer.toString());
                    } else {
                        close(session);
                    }
                }
            }
        }catch (Exception e) {
            log.error(e.getMessage() ,e);
        }
    }

    /**
     * 关闭连接
     *
     * @param session
     */
    public void close(Session session) {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("close", e.getMessage(), e);
            }
        }
    }

    public void stop(){
        redisService.delete(RedisConstant.MESSAGES_QUEUE + userId);
    }


}
