package com.trevor.data.controller;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.bo.ResponseHelper;
import com.trevor.common.domain.mysql.Room;
import com.trevor.common.enums.MessageCodeEnum;
import com.trevor.data.service.RoomService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RoomController {

    @Resource
    private RoomService roomService;


    @RequestMapping(value = "/api/room/{roomId}", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Room> findById(@PathVariable("roomId") String roomId){
        return ResponseHelper.createInstance(roomService.findOneById(Long.valueOf(roomId)) ,MessageCodeEnum.HANDLER_SUCCESS);
    }
}
