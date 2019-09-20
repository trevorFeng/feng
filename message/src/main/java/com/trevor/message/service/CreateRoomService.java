package com.trevor.message.service;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mongo.NiuniuRoomParam;
import com.trevor.message.core.GameCore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CreateRoomService {

    @Resource
    private GameCore gameCore;

    public JsonEntity<Object> createRoom(NiuniuRoomParam niuniuRoomParam ,String userId){
        //判断玩家拥有的房卡数量是否超过消耗的房卡数
        Integer cardNumByUserId = personalCardMapper.findCardNumByUserId(user.getId());
        Integer consumCardNum;
        if (Objects.equals(niuniuRoomParam.getConsumCardNum() , ConsumCardEnum.GAME_NUM_12_CARD_3.getCode())) {
            consumCardNum = ConsumCardEnum.GAME_NUM_12_CARD_3.getConsumCardNum();
            if (cardNumByUserId < ConsumCardEnum.GAME_NUM_12_CARD_3.getConsumCardNum()) {
                return ResponseHelper.withErrorInstance(MessageCodeEnum.USER_ROOMCARD_NOT_ENOUGH);
            }
        }else {
            consumCardNum = ConsumCardEnum.GAME_NUM_24_CARD_6.getConsumCardNum();
            if (cardNumByUserId < ConsumCardEnum.GAME_NUM_24_CARD_6.getConsumCardNum()) {
                return ResponseHelper.withErrorInstance(MessageCodeEnum.USER_ROOMCARD_NOT_ENOUGH);
            }
        }
        //生成房间，将房间信息存入数据库
        Integer totalNum;
        if (Objects.equals(consumCardNum ,ConsumCardEnum.GAME_NUM_12_CARD_3.getConsumCardNum())) {
            totalNum = 12;
        }else {
            totalNum = 24;
        }
        Long currentTime = System.currentTimeMillis();
        Room room = new Room();
        room.generateRoomBase(niuniuRoomParam.getRoomType() ,user.getId() ,currentTime ,totalNum);
        roomMapper.insertOne(room);

        //插入mongoDB
        niuniuRoomParam.setRoomId(room.getId());
        niuniuRoomParamMapper.save(niuniuRoomParam);

        //存入redis
        Map<String ,String> baseRoomInfoMap = niuniuRoomParam.generateBaseRoomInfoMap();
        baseRoomInfoMap.put(RedisConstant.RUNING_NUM ,"0");
        baseRoomInfoMap.put(RedisConstant.TOTAL_NUM ,totalNum.toString());
        redisService.putAll(RedisConstant.BASE_ROOM_INFO + room.getId() ,baseRoomInfoMap);

        //生成房卡消费记录
        CardConsumRecord cardConsumRecord = new CardConsumRecord();
        cardConsumRecord.generateCardConsumRecordBase(room.getId() , user.getId() ,consumCardNum);
        cardConsumRecordMapper.insertOne(cardConsumRecord);

        //更新玩家的房卡数量信息
        personalCardMapper.updatePersonalCardNum(user.getId() ,cardNumByUserId - consumCardNum);
        return ResponseHelper.createInstance(room.getId() , MessageCodeEnum.CREATE_SUCCESS);


        return null;
    }
}
