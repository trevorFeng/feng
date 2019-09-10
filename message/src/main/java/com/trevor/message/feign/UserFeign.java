package com.trevor.message.feign;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("data")
public interface UserFeign {

    @RequestMapping(value = "/api/user/{openId}/{hash}", method = RequestMethod.GET)
    JsonEntity<User> findByOpenId(@PathVariable("openId") String openId ,@PathVariable("hash")String hash);

    @RequestMapping(value = "/api/user/{userId}", method = RequestMethod.GET)
    JsonEntity<Boolean> isFriendManage(@PathVariable("userId") Long userId);
}
