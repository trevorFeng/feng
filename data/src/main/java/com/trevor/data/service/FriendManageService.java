package com.trevor.data.service;

import com.trevor.data.dao.mysql.FriendManageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FriendManageService {

    @Resource
    private FriendManageMapper friendManageMapper;

    public Boolean findRoomAuthFriendAllow(Long roomAuth ,Long userId){
        Long aLong = friendManageMapper.countRoomAuthFriendAllow(roomAuth, userId);
        if (aLong > 0) {
            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }
    }

}
