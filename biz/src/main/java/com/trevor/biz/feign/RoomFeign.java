package com.trevor.biz.feign;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mysql.Room;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("data")
public interface RoomFeign {

    @RequestMapping(value = "/api/room/{roomId}", method = RequestMethod.GET)
    JsonEntity<Room> findIndexById(@PathVariable("roomId") String roomId);

    @RequestMapping(value = "/api/room/{roomId}", method = RequestMethod.GET)
    JsonEntity<Object> updateStatus(@PathVariable("roomId") String roomId);
}
