package com.trevor.biz.feign;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.CardTrans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 一句话描述该类作用:【】
 *
 * @author: trevor
 * @create: 2019-09-21 15:59
 **/

@FeignClient("data")
public interface PersonalCardFeign {

    @RequestMapping(value = "/api/person/card/cardNum/query/{userId}", method = RequestMethod.GET)
    JsonEntity<Integer> findCardNumByUserId(@PathVariable("userId") Long suerId);

}
