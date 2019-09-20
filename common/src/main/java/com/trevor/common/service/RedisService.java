package com.trevor.common.service;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 删除键值对
     * @param key
     */
    public void delete(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * 得到value
     * @param keys
     * @return
     */
    public List<String> getBatchValue(Set<String> keys){
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 向list右侧添加元素
     * @param key
     * @param value
     */
    public void listRightPush(String key ,String...value){
        BoundListOperations<String, String> bl = stringRedisTemplate.boundListOps(key);
        bl.rightPushAll(value);
    }

    /**
     * 得到list元素并删除list
     * @param key
     * @return
     */
    public List<String> getListMembersAndDelete(String key){
        BoundListOperations<String ,String> ops = stringRedisTemplate.boundListOps(key);
        List<String> list = ops.range(0 ,-1);
        delete(key);
        return list;
    }

    /**
     * 存入map
     * @param key
     * @param map
     */
    public void putAll(String key ,Map<String ,String> map){
        BoundHashOperations<String, String, String> bh = stringRedisTemplate.boundHashOps(key);
        bh.putAll(map);
    }

    /**
     * 存入map
     * @param redisKey
     * @param hashKey
     * @param hashValue
     */
    public void put(String redisKey ,String hashKey ,String hashValue){
        BoundHashOperations<String, String, String> bh = stringRedisTemplate.boundHashOps(redisKey);
        bh.put(hashKey ,hashValue);
    }







}
