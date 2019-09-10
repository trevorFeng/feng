package com.trevor.message.feign;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Slf4j
@Component
public class FriendManageFeignResult {

    @Resource
    private FriendManageFeign friendManageFeign;

    public Boolean findRoomAuthFriendAllow( Long roomAuth ,Long userId){
        JsonEntity<Boolean> result = friendManageFeign.findRoomAuthFriendAllow(roomAuth ,userId);
        if (result.getData() != null) {
            return result.getData();
        }else {
            return null;
        }

    }
}
