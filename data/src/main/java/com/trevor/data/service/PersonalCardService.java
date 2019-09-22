package com.trevor.data.service;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.bo.ResponseHelper;
import com.trevor.common.enums.MessageCodeEnum;
import com.trevor.data.dao.mysql.PersonalCardMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: trevor
 * @Date: 2019\4\16 0016 23:16
 * @Description:
 */
@Service
public class PersonalCardService {

    @Resource
    private PersonalCardMapper personalCardMapper;

    /**
     * 根据玩家查询玩家拥有的房卡数量
     * @param userId
     * @return
     */
    public JsonEntity<Integer> findCardNumByUserId(Long userId) {
        return ResponseHelper.createInstance(personalCardMapper.findCardNumByUserId(userId) , MessageCodeEnum.HANDLER_SUCCESS);
    }
}
