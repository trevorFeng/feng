package com.trevor.data.service;


import com.trevor.common.domain.mysql.CardConsumRecord;
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

    @Transactional(rollbackFor = Exception.class)
    public Long insertOne(CardConsumRecord cardConsumRecord){
        return cardConsumRecordMapper.insertOne(cardConsumRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoomRecordIds(List<Long> roomIds){
        cardConsumRecordMapper.deleteByRoomRecordIds(roomIds);
    }


}
