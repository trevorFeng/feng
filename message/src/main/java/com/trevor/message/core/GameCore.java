package com.trevor.message.core;

import com.google.common.collect.Maps;
import com.trevor.message.bo.*;
import com.trevor.message.core.event.niuniu.ReadyEvent;
import com.trevor.message.game.niuniu.Ready;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GameCore {

    /**
     * 全部房间的游戏数据
     */
    private static Map<String , RoomData> map = Maps.newConcurrentMap();

    @Resource
    private ReadyEvent ready;

    public void putRoomData(RoomData roomData ,String roomId){
        map.put(roomId ,roomData);
    }

    public void removeRoomData(String roomId){
        map.remove(roomId);
    }

    public RoomData getRoomData(String roomId){
        return map.get(roomId);
    }

    /**
     * 得到牛牛的特殊配置
     * @param roomId
     * @return
     */
    public List<Integer> getNiuniuSpecial(String roomId){
        return ((NiuniuData)map.get(roomId)).getSpecial();
    }

    public void execut(Task task){
        if (Objects.equals(task.getRoomType() , GameType.NIUNIN)) {
            executNiuniu(task);
        }else if (Objects.equals(task.getRoomType() ,GameType.JINHUA)) {

        }
    }

    public void executNiuniu(Task task){
        if (Objects.equals(task.getFlag() , TaskFlag.READY)) {
            ready.ready(map.get(task.getRoomId()) ,task);
        }else if (Objects.equals(task.getFlag() ,))
    }
}
