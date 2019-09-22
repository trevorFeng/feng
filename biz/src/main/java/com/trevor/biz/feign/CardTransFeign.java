package com.trevor.biz.feign;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.CardTrans;
import com.trevor.common.domain.mysql.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 一句话描述该类作用:【】
 *
 * @author: trevor
 * @create: 2019-09-21 11:48
 **/
@FeignClient("data")
public interface CardTransFeign {

    @RequestMapping(value = "/api/card/trans/{cardNum}/{userId}", method = RequestMethod.POST)
    JsonEntity<Object> createCardPackage(@PathVariable("cardNum") Integer cardNum , @PathVariable("userId") Long userId);

    @RequestMapping(value = "/api/card/trans/save", method = RequestMethod.POST)
    JsonEntity<Object> save(@RequestBody CardTrans cardTrans);
}
