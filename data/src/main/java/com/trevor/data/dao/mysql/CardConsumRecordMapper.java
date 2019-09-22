package com.trevor.data.dao.mysql;


import com.trevor.common.domain.mysql.CardConsumRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author trevor
 * @date 2019/3/8 16:23
 */
@Repository
public interface CardConsumRecordMapper {

    /**
     * 插入一条记录并返回主键
     * @param cardConsumRecord
     * @return
     */
    Long insertOne(@Param("cardConsumRecord") CardConsumRecord cardConsumRecord);

    /**
     * 根据房间id删除
     * @param roomIds
     */
    void deleteByRoomIds(@Param("roomIds") List<Long> roomIds);
}
