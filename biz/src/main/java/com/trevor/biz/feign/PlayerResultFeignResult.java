package com.trevor.biz.feign;

import com.trevor.common.domain.mongo.PlayerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class PlayerResultFeignResult {

    @Resource
    private PlayerResultFeign playerResultFeign;

    public void saveAll(List<PlayerResult> results){
        playerResultFeign.saveAll(results);
    }
}
