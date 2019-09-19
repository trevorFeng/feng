package com.trevor.message.bo;

import com.trevor.common.bo.PaiXing;
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
    private Integer basePoint;

    /**
     * 规则
     */
    private Integer rule;

    /**
     * 下注类型
     */
    private String xiazhu;

    /**
     * 特殊
     */
    private List<Integer> special;


    /**
     * 1---顺子牛，5倍
     * 2---五花牛，6倍
     * 3---同花牛，6倍
     * 4---葫芦牛，7倍
     * 5---炸弹牛，8倍
     * 6---五小牛，10倍
     */
    private Set<Integer> paiXing;

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
     * key为runingNum，value为庄家id
     */
    private Map<String ,String> zhuangJiaMap;

    /**
     * 外层key为runingNum,内层key为玩家id，内层value为下注的倍数
     */
    private Map<String ,Map<String ,Integer>> xiaZhuMap;

    /**
     * 外层key为runingNum,内层key为玩家id，内层value为牌型
     */
    private Map<String ,Map<String ,PaiXing>> paiXingMap;

    /**
     * key为runingNum,value为摊牌玩家id
     */
    private Map<String ,Set<String>> tanPaiMap;

    /**
     * 玩家每一句的分数
     */
    private Map<String ,Map<String ,Integer>> runingScoreMap;

    /**
     * 玩家总分
     */
    private Map<String ,Integer> totalScoreMap;
}
