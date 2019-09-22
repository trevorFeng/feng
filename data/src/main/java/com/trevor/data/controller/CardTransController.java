package com.trevor.data.controller;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.CardTrans;
import com.trevor.data.service.CardTransService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.*;

/**
 * 一句话描述该类作用:【】
 *
 * @author: trevor
 * @create: 2019-09-21 11:58
 **/
@RestController("/api/card/trans")
public class CardTransController {

    @Resource
    private CardTransService cardTransService;

    @RequestMapping(value = "/save" ,method = {RequestMethod.POST} ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> save(@RequestBody CardTrans cardTrans){
        return cardTransService.save(cardTrans);
    }

}
