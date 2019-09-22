package com.trevor.data.service;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.bo.ResponseHelper;
import com.trevor.common.domain.mysql.FriendsManage;
import com.trevor.common.enums.MessageCodeEnum;
import com.trevor.data.dao.mysql.FriendManageMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FriendManageService {

    @Resource
    private FriendManageMapper friendManageMapper;

    /**
     * 查询玩家是否是房主的好友查询玩家是否是房主的好友
     * @param roomAuth
     * @param userId
     * @return
     */
    public JsonEntity<Boolean> findRoomAuthFriendAllow(Long roomAuth ,Long userId){
        Long aLong = friendManageMapper.countRoomAuthFriendAllow(roomAuth, userId);
        if (aLong > 0) {
            return ResponseHelper.createInstance(Boolean.TRUE , MessageCodeEnum.HANDLER_SUCCESS);
        }else {
            return ResponseHelper.createInstance(Boolean.FALSE , MessageCodeEnum.HANDLER_SUCCESS);
        }
    }


    /**
     * 根据用户id查询管理的好友
     * @param userId
     * @return
     */
    public JsonEntity<List<FriendsManage>> findByUserId(Long userId) {
        return ResponseHelper.createInstance(friendManageMapper.findByUserId(userId) ,MessageCodeEnum.HANDLER_SUCCESS);
    }


    /**
     * 申请好友
     * @param userId
     * @param manageFriendId
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<Object> applyFriend(Long userId ,Long manageFriendId) {
        friendManageMapper.applyFriend(userId ,manageFriendId);
        return ResponseHelper.createInstance(null ,MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 通过好友申请
     * @param userId
     * @param manageFriendId
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<Object> passFriend(Long userId ,Long manageFriendId) {
        friendManageMapper.passFriend(userId ,manageFriendId);
        return ResponseHelper.createInstance(null ,MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 移除好友
     * @param userId
     * @param manageFriendId
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<Object> removeFriend(Long userId ,Long manageFriendId) {
        friendManageMapper.removeFriend(userId ,manageFriendId);
        return ResponseHelper.createInstance(null ,MessageCodeEnum.HANDLER_SUCCESS);
    }

}
