package com.trevor.message.feign;


import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.User;
import com.trevor.common.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UserFeignResult {

    @Resource
    private UserFeign userFeign;

    public User findByOpenId(String openId ,String hash){
        JsonEntity<User> result = userFeign.findByOpenId(openId, hash);
        if (result.getData() != null) {
            return result.getData();
        }
        return null;
    }

    public Boolean isFriendManage(Long userId){
        JsonEntity<Boolean> result = userFeign.isFriendManage(userId);
        if (result.getData() != null) {
            return result.getData();
        }
        return null;
    }

    public List<User> findUsersByIds(List<Long> ids) {
        JsonEntity<List<User>> result = userFeign.findUsersByIds(ids);
        if (result.getData() != null) {
            return result.getData();
        }
        return null;
    }
}
