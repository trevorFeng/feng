package com.trevor.message.feign;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RoomFeignResult {

    @Resource
    private RoomFeign roomFeign;

    public Room findRoomById(String roomId){
        JsonEntity<Room> result = roomFeign.findIndexById(roomId);
        if (result.getData() != null) {
            return result.getData();
        }else {
            log.error("没有相应的roomId:" + roomId);
            return null;
        }

    }

    public void updateStatus(Long roomId ,Integer status ,Integer runingNum){

    }

    public void updateRuningNum(Long roomId ,Integer runingNum){

    }
}
