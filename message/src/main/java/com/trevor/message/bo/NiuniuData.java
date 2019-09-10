package com.trevor.message.bo;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class NiuniuData extends RoomData{

    /**
     * 抢庄类型
     */
    private String robZhuangType;

    /**
     * 基本分数
     */
    private String basePoint;

    /**
     * 规则
     */
    private String rule;

    /**
     * 下注类型
     */
    private String xiazhu;

    /**
     * 特殊
     */
    private List<Integer> special;

    /**
     * 牌型类型
     */
    private String paiXingType ;

    /**
     * 总局数
     */
    private String totalNum;

    /**
     * 当前局数
     */
    private String runingNum;

    /**
     * 房间状态
     */
    private String gameStatus;

    /**
     * 房间里的玩家
     */
    private Set<String> players;

    /**
     * 真正的玩家
     */
    private Set<String> realPlayers;

    /**
     * 观众
     */
    private Set<String> guanZhongs;

    /**
     * 掉线的玩家
     */
    private Set<String> disConnections;

    /**
     * key为runingNum
     */
    private Map<String ,Set<String>> readyPlayMap;

    /**
     * 外层key为runingNum,内层key为玩家id，内层value为玩家的牌
     */
    private Map<String ,Map<String , List<String>>> pokesMap;

    /**
     * 外层key为runingNum,内层key为玩家id，内层value为抢庄的倍数
     */
    private Map<String ,Map<String ,Integer>> qiangZhuangMap;

    /**
     * 外层key为runingNum,内层key为玩家id，内层value为下注的倍数
     */
    private Map<String ,Map<String ,Integer>> xiaZhuMap;
}
