package com.trevor.message.core.event.niuniu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trevor.common.bo.PaiXing;
import com.trevor.common.bo.Player;
import com.trevor.common.bo.RedisConstant;
import com.trevor.common.bo.SocketResult;
import com.trevor.common.domain.mysql.Room;
import com.trevor.common.domain.mysql.User;
import com.trevor.common.enums.GameStatusEnum;
import com.trevor.common.enums.RoomTypeEnum;
import com.trevor.common.enums.SpecialEnum;
import com.trevor.common.util.JsonUtil;
import com.trevor.common.util.NumberUtil;
import com.trevor.message.bo.NiuniuData;
import com.trevor.message.bo.RoomData;
import com.trevor.message.bo.Task;
import com.trevor.message.core.event.BaseEvent;
import com.trevor.message.core.event.Event;
import com.trevor.message.service.SocketService;
import com.trevor.message.socket.NiuniuSocket;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JoinRoomEvent extends BaseEvent implements Event {

    @Override
    public void execute(RoomData roomData, Task task) {
        NiuniuData data = (NiuniuData) roomData;
        NiuniuSocket socket = (NiuniuSocket) task.getSocket();
        //房主是否开启好友管理功能
        Boolean isFriendManage = task.getIsFriendManage();
        //加入的玩家是否是房主的好友
        Boolean roomAuthFriendAllow = task.getRoomAuthFriendAllow();
        List<Integer> special = data.getSpecial();

        User joinUser = task.getJoinUser();
        Integer realPlayersSize = data.getRealPlayers().size();
        String roomType = data.getRoomType();

        //房间规则检查
        SocketResult soc = checkRoom(isFriendManage ,special ,roomAuthFriendAllow
                ,joinUser ,realPlayersSize ,roomType);
        if (soc.getHead() != null) {
            socket.directSendMessage(soc ,socket.session);
            socket.close(socket.session);
            return;
        }

        //广播新人加入（给其他玩家发消息）
        soc.setHead(1000);
        String playerId = task.getPlayId();
        String roomId = task.getRoomId();
        Set<String> players = data.getPlayers();
        Map<String, Integer> totalScoreMap = data.getTotalScoreMap();
        if (!soc.getIsChiGuaPeople()) {
            soc.setTotalScore(totalScoreMap.get(playerId) == null ? 0 : totalScoreMap.get(playerId));
            socketService.broadcast(roomId ,soc ,players);
        }

        //删除自己的消息队列
        redisService.delete(RedisConstant.MESSAGES_QUEUE + playerId);
        //加入广播的队列
        socketService.join(roomId ,socket);
        data.getPlayers().add(playerId);

        //给新人发消息
        Integer runingNum = NumberUtil.stringFormatInteger(data.getRuningNum());
        Integer totalNum =  NumberUtil.stringFormatInteger(data.getTotalNum());
        //设置房间正在运行的局数
        soc.setRuningAndTotal((runingNum + 1) + "/" + totalNum);
        //不是吃瓜群众则加入到真正的玩家集合中并且删除自己的掉线状态\
        Set<String> disConnections = data.getDisConnections();
        if (!soc.getIsChiGuaPeople()) {
            //加入到真正的玩家中
            data.getRealPlayers().add(playerId);
            //删除自己掉线状态
            disConnections.remove(playerId);
        }
        //设置掉线的玩家
        soc.setDisConnectionPlayerIds(disConnections);
        //设置真正的玩家
        Set<String> realUserIds = data.getRealPlayers();
        Set<String> guanZhongUserIds = data.getGuanZhongs();
        soc.setPlayers(getRealRoomPlayerCount(realUserIds ,guanZhongUserIds ,totalScoreMap));
        //发送房间状态消息
        welcome(roomId ,soc);
    }

    private SocketResult checkRoom(Boolean isFriendManage ,List<Integer> special ,Boolean roomAuthFriendAllow
            ,User joinUser ,Integer realPlayersSize ,String roomType){
        if (isFriendManage) {
            //配置仅限好友
            if (special.contains(SpecialEnum.JUST_FRIENDS.getCode())) {
                //不是房主的好友
                if (!roomAuthFriendAllow) {
                    return new SocketResult(508);
                    //是房主的好友
                }else {
                    return dealCanSee(joinUser ,special ,realPlayersSize ,roomType);
                }
            }
            //未配置仅限好友
            else {
                return dealCanSee(joinUser ,special ,realPlayersSize ,roomType);
            }
            // 未开通
        }else {
            return dealCanSee(joinUser ,special ,realPlayersSize ,roomType);
        }
    }

    /**
     * 处理是否可以观战
     * @throws IOException
     */
    private SocketResult dealCanSee(User user, List<Integer> special ,Integer realPlayersSize ,String roomType){
        SocketResult socketResult = new SocketResult();
        socketResult.setUserId(String.valueOf(user.getId()));
        socketResult.setName(user.getAppName());
        socketResult.setPictureUrl(user.getAppPictureUrl());
        Boolean bo = realPlayersSize < RoomTypeEnum.getRoomNumByType(Integer.valueOf(roomType));
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

    /**
     * 得到房间里真正的玩家
     * @return
     */
    public List<Player> getRealRoomPlayerCount(Set<String> realUserIds ,Set<String> guanZhongUserIds ,Map<String ,Integer> totalScoreMap){
        List<String> userStrs = redisService.getBatchValue(realUserIds);

        List<User> users = Lists.newArrayList();
        for (String userStr : userStrs) {
            users.add(JsonUtil.parseJavaObject(userStr ,User.class));
        }

        List<Player> players = Lists.newArrayList();
        for (User user : users) {
            Player player = new Player();
            player.setUserId(user.getId());
            player.setName(user.getAppName());
            player.setPictureUrl(user.getAppPictureUrl());
            if (guanZhongUserIds.contains(String.valueOf(user.getId()))) {
                player.setIsGuanZhong(Boolean.TRUE);
            }
            players.add(player);
            player.setTotalScore(totalScoreMap.get(user.getId().toString()) == null ? 0 : totalScoreMap.get(user.getId().toString()));
        }
        return players;
    }

    /**
     * 欢迎玩家加入，发送房间状态信息
     */
    private void welcome(String roomId ,String playerId ,SocketResult socketResult ,NiuniuSocket socket ,String gameStatus ,Set<String> readyPlayers ,Set<String> ){
        socketResult.setHead(2002);
        //设置准备的玩家
        if (Objects.equals(gameStatus ,GameStatusEnum.READY.getCode()) ||
                Objects.equals(gameStatus , GameStatusEnum.READY_COUNT_DOWN_START.getCode()) ||
                Objects.equals(gameStatus , GameStatusEnum.READY_COUNT_DOWN_END.getCode())) {
            socketResult.setReadyPlayerIds(readyPlayers);
        }
        //设置玩家先发的4张牌
        else if (Objects.equals(gameStatus , GameStatusEnum.FA_FOUR_PAI.getCode())) {
            socketResult.setReadyPlayerIds(readyPlayers);
            if (readyPlayers.contains(userId)) {
                setPoke_4(socketResult ,userId);
            }
            socketResult.setQiangZhuangMap(getQiangZhuangPlayers());
        }
        //设置庄家
        else if (Objects.equals(gameStatus , GameStatusEnum.QIANG_ZHUANG_COUNT_DOWN_START.getCode()) ||
                    Objects.equals(gameStatus ,GameStatusEnum.QIANG_ZHUANG_COUNT_DOWN_END.getCode())) {
            Set<String> readyPlayers = getReadyPlayers();
            socketResult.setReadyPlayerIds(readyPlayers);
            if (readyPlayers.contains(userId)) {
                setPoke_4(socketResult ,userId);
            }
            socketResult.setZhuangJiaUserId(getZhuangJia());
            socketResult.setXianJiaXiaZhuMap(getXianJiaXiaZhu());
        }
        //设置玩家发的最后一张牌
        else if (Objects.equals(gameStatus , GameStatusEnum.BEFORE_CALRESULT.getCode())) {
            socketResult.setGameStatus(4);
            socketResult.setZhuangJiaUserId(getZhuangJia());
            setPoke_5(socketResult);
            socketResult.setTanPaiPlayerUserIds(getTanPaiPlayer());
            setScoreAndPaiXing(socketResult);
        }
        //下一句准备
        else if (Objects.equals(gameStatus , GameStatusEnum.BEFORE_DELETE_KEYS.getCode())) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            socketResult.setGameStatus(1);
            socketResult.setReadyPlayerIds(getReadyPlayers());
        }
        socketService.sendMessage(playerId ,socketResult ,roomId);
        return;
    }

    /**
     * 得到抢庄的玩家
     * @return
     */
    private Map<String ,String> getQiangZhuangPlayers(){
        Map<String ,String> qiangZhuangMap = redisService.getMap(RedisConstant.QIANGZHAUNG + roomId);
        if (!qiangZhuangMap.isEmpty()) {
            return qiangZhuangMap;
        }
        return null;
    }

    /**
     * 得到庄家
     * @return
     */
    private String getZhuangJia(){
        String zhuangJia = redisService.getValue(RedisConstant.ZHUANGJIA + roomId);
        return zhuangJia;
    }

    /**
     * 得到闲家下注
     * @return
     */
    private Map<String ,String> getXianJiaXiaZhu(){
        Map<String ,String> xianJiaXiaZhu = redisService.getMap(RedisConstant.XIANJIA_XIAZHU + roomId);
        if (!xianJiaXiaZhu.isEmpty()) {
            return xianJiaXiaZhu;
        }
        return null;
    }


    /**
     * 设置得分和牌型
     * @return
     */
    private void setScoreAndPaiXing(SocketResult socketResult){
        //设置本局得分
        Map<String ,String> scoreMap = redisService.getMap(RedisConstant.SCORE + roomId);
        Map<String ,Integer> stringIntegerMap = Maps.newHashMap();
        for (Map.Entry<String ,String> entry : scoreMap.entrySet()) {
            stringIntegerMap.put(entry.getKey() ,Integer.valueOf(entry.getValue()));
        }
        socketResult.setScoreMap(stringIntegerMap);

        Map<String ,String> paiXingMap = redisService.getMap(RedisConstant.PAI_XING + roomId);
        Map<String ,Integer> paiXingIntegerMap = Maps.newHashMap();
        for (Map.Entry<String ,String> entry : paiXingMap.entrySet()) {
            paiXingIntegerMap.put(entry.getKey() , JsonUtil.parseJavaObject(entry.getValue() , PaiXing.class).getPaixing());

        }
        socketResult.setPaiXing(paiXingIntegerMap);
    }

    /**
     * 设置4张牌
     * @param socketResult
     */
    public void setPoke_4(SocketResult socketResult , String userId){
        Map<String, String> map = redisService.getMap(RedisConstant.POKES + roomId);
        Map<String ,List<String>> userPokeMap_5 = new HashMap<>(2<<4);
        List<String> pokeList_4 = JsonUtil.parseJavaList(map.get(userId) ,String.class).subList(0 ,4);
        socketResult.setUserPokeList_4(pokeList_4);
    }



    /**
     * 设置5张牌
     * @param socketResult
     */
    public void setPoke_5(SocketResult socketResult){
        Map<String, String> map = redisService.getMap(RedisConstant.POKES + roomId);
        Map<String ,List<String>> userPokeMap_5 = new HashMap<>(2<<4);
        for (Map.Entry<String ,String> entry : map.entrySet()) {
            userPokeMap_5.put(entry.getKey() , JsonUtil.parseJavaList(entry.getValue() ,String.class));
        }
        socketResult.setUserPokeMap_5(userPokeMap_5);
    }

    /**
     * 得到摊牌的玩家
     * @return
     */
    private Set<String> getTanPaiPlayer(){
        return redisService.getSetMembers(RedisConstant.TANPAI + roomId);
    }
}
