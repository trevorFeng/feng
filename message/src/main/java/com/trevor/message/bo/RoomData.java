package com.trevor.message.bo;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class RoomData {

    /**
     * 房间的id
     */
    protected String roomId;

    /**
     * 房间类型 1为13人牛牛，2为10人牛牛，3为6人牛牛 ，4为金花
     */
    protected String roomType;

}
