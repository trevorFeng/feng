package com.trevor.message.feign;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("data")
public interface FriendManageFeign {

    @RequestMapping(value = "/api/friendManage/allow/{roomAuth}/{userId}", method = RequestMethod.GET)
    JsonEntity<Boolean> findRoomAuthFriendAllow(@PathVariable("roomAuth") Long roomAuth ,@PathVariable("userId") Long userId);

}
