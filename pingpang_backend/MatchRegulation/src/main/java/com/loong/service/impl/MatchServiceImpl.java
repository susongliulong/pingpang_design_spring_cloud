package com.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.entity.Match;
import com.loong.entity.SignUp;
import com.loong.entity.User;
import com.loong.entity.dto.MatchDTO;
import com.loong.entity.dto.SearchMessage;
import com.loong.entity.vo.MatchVo;
import com.loong.mapper.MatchMapper;
import com.loong.mapper.SignUpMapper;
import com.loong.mapper.UserMapper;
import com.loong.service.IMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
@Service
public class MatchServiceImpl extends ServiceImpl<MatchMapper, Match> implements IMatchService {

    @Autowired
    private MatchMapper matchMapper;

    @Autowired
    private UserMapper  userMapper;

    @Autowired
    private SignUpMapper signUpMapper;

    @Override
    public List<MatchVo> matchMessage(Integer pageNumber) {

        Page<Match> matchPage = new Page<>(pageNumber, 10);
        matchMapper.selectPage(matchPage, null);
        List<Match> matches = matchPage.getRecords();
        List<MatchVo> matchVos=new LinkedList<>();
        // 对于每一个match，进一步封装成matchVo
        matches.forEach(
                match -> {
                    MatchVo matchVo = new MatchVo();
                    matchVo.setMatch(match);
                    // 根据userId查询用户名称
                    User user = userMapper.selectById(match.getUserId());
                    matchVo.setHolder(user.getName());
                    matchVos.add(matchVo);
                }
        );
        return matchVos;
    }

    @Override
    public List<MatchVo> matchMessage(Integer pageNumber, SearchMessage searchMessage) {
        LambdaQueryWrapper<Match> queryWrapper = new LambdaQueryWrapper<>();
        String keyWord = searchMessage.getKeyWord();
        queryWrapper.like(keyWord!=null,Match::getDescription,keyWord);
        queryWrapper.ge(Match::getSignUpEndTime, LocalDateTime.now());
        queryWrapper.le(searchMessage.getSignUpEndTime()!=null,Match::getSignUpEndTime,searchMessage.getSignUpEndTime());
        queryWrapper.ge(searchMessage.getMatchStartTime()!=null,Match::getMatchStartTime,searchMessage.getMatchStartTime());
        queryWrapper.ge(searchMessage.getPlayerNumber()!=null,Match::getMaxNumber,searchMessage.getPlayerNumber());
        queryWrapper.ge(searchMessage.getMinPoints()!=null,Match::getMinPoints,searchMessage.getMinPoints());

        Page<Match> matchPage = new Page<>(pageNumber,10);
        matchMapper.selectPage(matchPage,queryWrapper);
        List<MatchVo> matchVos=new LinkedList<>();
        matchPage.getRecords().forEach(
                match->{
                    MatchVo matchVo = new MatchVo();
                    matchVo.setMatch(match);
                    User user = userMapper.selectById(match.getUserId());
                    matchVo.setHolder(user.getName());
                    matchVo.setSignUp(markMatchesOfSignUp(searchMessage.getUserId(),match.getMatchId()));
                    matchVos.add(matchVo);
                }
        );
        return matchVos;
    }

    @Override
    public Long pages(SearchMessage searchMessage) {
        LambdaQueryWrapper<Match> queryWrapper = new LambdaQueryWrapper<>();
        String keyWord = searchMessage.getKeyWord();
        queryWrapper.like(keyWord!=null,Match::getDescription,keyWord);
        queryWrapper.ge(Match::getSignUpEndTime, LocalDateTime.now());
        queryWrapper.le(searchMessage.getSignUpEndTime()!=null,Match::getSignUpEndTime,searchMessage.getSignUpEndTime());
        queryWrapper.ge(searchMessage.getMatchStartTime()!=null,Match::getMatchStartTime,searchMessage.getMatchStartTime());
        queryWrapper.ge(searchMessage.getPlayerNumber()!=null,Match::getMaxNumber,searchMessage.getPlayerNumber());
        queryWrapper.ge(searchMessage.getMinPoints()!=null,Match::getMinPoints,searchMessage.getMinPoints());

        Page<Match> matchPage = new Page<>(1,10);
        matchMapper.selectPage(matchPage,queryWrapper);
        return matchPage.getPages();
    }

    /**
     * 新增赛事信息
     * @param content
     * @return
     */
    @Override
    public boolean addMatchMessage(long matchId,String content) {
        matchMapper.addMatchMessage(matchId,content);
        return true;
    }

    @Override
    public String getMatchMessage(long matchId) {
        return matchMapper.getMatchMessage(matchId);
    }

    @Override
    public void updatePlayerNumber(Long matchId) {
        Match match = matchMapper.selectById(matchId);
        match.setPlayerNumber(match.getPlayerNumber()+1);
        matchMapper.updateById(match);
    }

    @Override
    public boolean markMatchesOfSignUp(long userId,long matchId) {
        Long count = signUpMapper.selectCount(new LambdaQueryWrapper<SignUp>()
                .eq(SignUp::getMatchId, matchId)
                .eq(SignUp::getUserId, userId));
        return count!=0L;
    }

    @Transactional
    @Override
    public void postMatch(MatchDTO matchDTO) {

        Match match=matchDTO.getMatch();
        matchMapper.insert(match);
        Long matchId = match.getMatchId();

        matchMapper.addMatchMessage(matchId,matchDTO.getContent());
    }

    @Override
    public void deleteAllData(Long id) {

    }

    @Override
    public List<Match> matches(long userId) {
        List<Match> matches = matchMapper.selectList(new LambdaQueryWrapper<Match>().eq(Match::getUserId, userId));
        return matches;
    }


}
