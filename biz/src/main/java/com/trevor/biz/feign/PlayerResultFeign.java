package com.trevor.biz.feign;

import com.trevor.common.bo.JsonEntity;
import com.trevor.common.domain.mongo.PlayerResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("data")
public interface PlayerResultFeign {

    @RequestMapping(value = "/api/room/{roomId}", method = RequestMethod.GET)
    JsonEntity<Object> saveAll(@PathVariable("roomId") List<PlayerResult> results);
}
