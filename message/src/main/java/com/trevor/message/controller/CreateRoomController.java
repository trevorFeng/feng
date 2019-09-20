package com.trevor.message.controller;

import com.trevor.common.bo.JsonEntity;
import com.trevor.message.service.CreateRoomService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class CreateRoomController {

    @Resource
    private CreateRoomService createRoomService;

    @RequestMapping(value = "/api/create/room/{userId}", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Object> createRoom(@PathVariable("userId") Long userId){
        return createRoomService.createRoom(userId);
    }
}
