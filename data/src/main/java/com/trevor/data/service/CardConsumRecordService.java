package com.trevor.data.service;


import com.trevor.common.bo.JsonEntity;
import com.trevor.common.bo.ResponseHelper;
import com.trevor.common.domain.mysql.CardConsumRecord;
import com.trevor.common.enums.MessageCodeEnum;
import com.trevor.data.dao.mysql.CardConsumRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author trevor
 * @date 2019/3/8 16:23
 */
@Service
public class CardConsumRecordService {

    @Resource
    private CardConsumRecordMapper cardConsumRecordMapper;

    /**
     * 插入一条记录并返回主键
     * @param cardConsumRecord
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<Long> insertOne(CardConsumRecord cardConsumRecord){
        return ResponseHelper.createInstance(cardConsumRecordMapper.insertOne(cardConsumRecord) , MessageCodeEnum.HANDLER_SUCCESS);
    }

    /**
     * 根据房间id集合删除
     * @param roomIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<Object> deleteByRoomIds(List<Long> roomIds){
        cardConsumRecordMapper.deleteByRoomIds(roomIds);
        return ResponseHelper.createInstance(null ,MessageCodeEnum.HANDLER_SUCCESS);
    }


}
