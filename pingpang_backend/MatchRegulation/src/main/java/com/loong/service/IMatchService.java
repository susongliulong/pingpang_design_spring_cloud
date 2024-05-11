package com.loong.service;

import com.loong.entity.Match;
import com.baomidou.mybatisplus.extension.service.IService;
import com.loong.entity.dto.MatchDTO;
import com.loong.entity.dto.SearchMessage;
import com.loong.entity.vo.MatchVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
public interface IMatchService extends IService<Match> {

    List<MatchVo> matchMessage(Integer pageNumber);

    List<MatchVo> matchMessage(Integer pageNumber, SearchMessage searchMessage);

    Long pages(SearchMessage searchMessage);

    boolean addMatchMessage(long matchId,String content);

    String getMatchMessage(long matchId);

    void updatePlayerNumber(Long matchId);

    boolean markMatchesOfSignUp(long userId,long matchId);

    void postMatch(MatchDTO matchDTO);

    void deleteAllData(Long id);

    List<Match> matches(long userId);

}
