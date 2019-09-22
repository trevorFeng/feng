package com.trevor.biz.feign;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 一句话描述该类作用:【】
 *
 * @author: trevor
 * @create: 2019-09-21 15:59
 **/

@Slf4j
@Component
public class PersonalCardFeignResult {

    @Resource
    private PersonalCardFeign personalCardFeign;


    /**
     * 查询用户的房卡数量
     * @param userId
     * @return
     */
    public Integer findCardNumByUserId(Long userId){
        JsonEntity<Integer> result = personalCardFeign.findCardNumByUserId(userId);
        if (result.getCode() != null && result.getCode() > 0) {
            return result.getData();
        }else {
            log.error("call /api/person/card/cardNum/query/{userId} error ,the error message is :" + result.getMessage());
            throw new RuntimeException("");
        }
    }


}
