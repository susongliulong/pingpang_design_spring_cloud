package com.loong.mapper;

import com.loong.entity.Match;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
public interface MatchMapper extends BaseMapper<Match> {

    void addMatchMessage(long matchId, String content);

    String getMatchMessage(long matchId);
}
