package com.trevor.data.controller;

import com.trevor.common.bo.JsonEntity;
import com.trevor.data.service.PersonalCardService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 一句话描述该类作用:【】
 *
 * @author: trevor
 * @create: 2019-09-21 15:55
 **/

@RestController("/api/person/card")
public class PersonalCardController {

    @Resource
    private PersonalCardService personalCardService;


    /**
     * 根据玩家查询玩家拥有的房卡数量
     * @param userId
     * @return
     */
    @RequestMapping(value = "/cardNum/query/{userId}")
    public JsonEntity<Integer> findCardNumByUserId(@PathVariable("userId") Long userId){
        return personalCardService.findCardNumByUserId(userId);
    }

}
