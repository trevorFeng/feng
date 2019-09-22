package com.trevor.data.controller;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.bo.ResponseHelper;
import com.trevor.common.domain.mysql.User;
import com.trevor.common.enums.MessageCodeEnum;
import com.trevor.data.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 根据openid查询用户，只包含openid和hash字段
     * @param openId
     * @param hash
     * @return
     */
    @RequestMapping(value = "/api/user/{openId}/{hash}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<User> findByOpenid(@PathVariable("openId") String openId , @PathVariable("hash")String hash){
        return ResponseHelper.createInstance(userService.findByOpenId(openId , hash) ,MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 查询玩家是否开启好友管理功能
     * @param userId
     * @return
     */
    @RequestMapping(value = "/api/user/{userId}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<Boolean> isFriendManage(@PathVariable("userId") Long userId){
        return ResponseHelper.createInstance(userService.isFriendManage(userId) ,MessageCodeEnum.HANDLER_SUCCESS);
    }

}
