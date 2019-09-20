package com.trevor.message.feign;

import com.trevor.common.bo.JsonEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("data")
public interface PersonalCardFeign {


}
