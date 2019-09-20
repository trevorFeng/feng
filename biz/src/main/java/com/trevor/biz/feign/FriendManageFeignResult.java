package com.trevor.biz.feign;

import com.trevor.common.bo.JsonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
