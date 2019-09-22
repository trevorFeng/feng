package com.trevor.data.controller;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.bo.ResponseHelper;
import com.trevor.common.domain.mysql.Room;
import com.trevor.common.enums.MessageCodeEnum;
import com.trevor.data.service.FriendManageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class FriendManageController {

    @Resource
    private FriendManageService friendManageService;


    @RequestMapping(value = "/api/friendManage/allow/{roomAuth}/{userId}", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Boolean> findRoomAuthFriendAllow(@PathVariable("roomAuth") Long roomAuth ,@PathVariable("userId") Long userId){
        return friendManageService.findRoomAuthFriendAllow(roomAuth ,userId);
    }

}
