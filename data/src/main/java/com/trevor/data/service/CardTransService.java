package com.trevor.data.service;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.bo.ResponseHelper;
import com.trevor.common.domain.mysql.CardTrans;
import com.trevor.common.enums.MessageCodeEnum;
import com.trevor.data.dao.mysql.CardTransMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 一句话描述该类作用:【】
 *
 * @author: trevor
 * @create: 2019-09-21 12:01
 **/

@Service
public class CardTransService {

    @Resource
    private CardTransMapper cardTransMapper;

    /**
     * save
     * @param cardTrans
     */
    public JsonEntity<Object> save(CardTrans cardTrans){
        cardTransMapper.insertOne(cardTrans);
        return ResponseHelper.createInstance(null , MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 根据交易号查询交易房卡的数量
     * @param transNo
     * @return
     */
    public JsonEntity<Integer> findCardNumByTransNo(String transNo) {
        Integer cardNum = cardTransMapper.findCardNumByTransNo(transNo);
        return ResponseHelper.createInstance(cardNum ,MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 关闭交易,将版本号置为1
     * @param transNo
     * @param turnInTime
     * @param turnInUserId
     * @param turnInUserName
     * @return
     */
    public JsonEntity<Long> closeTrans(String transNo ,Long turnInTime ,Long turnInUserId ,String turnInUserName) {
        Long l = cardTransMapper.closeTrans(transNo ,turnInTime ,turnInUserId ,turnInUserName);
        return ResponseHelper.createInstance(l ,MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 查询发出的房卡
     * @param userId
     * @return
     */
    public JsonEntity<List<CardTrans>> findSendCardRecord(Long userId) {
        return ResponseHelper.createInstance(cardTransMapper.findSendCardRecord(userId) ,MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 查询收到的房卡
     * @param turnInUserId
     * @return
     */
    public JsonEntity<List<CardTrans>> findRecevedCardRecord(Long turnInUserId) {
        return ResponseHelper.createInstance(cardTransMapper.findRecevedCardRecord(turnInUserId) ,MessageCodeEnum.HANDLER_SUCCESS);
    }
}
