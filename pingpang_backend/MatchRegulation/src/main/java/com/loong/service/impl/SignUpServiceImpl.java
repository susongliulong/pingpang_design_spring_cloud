package com.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.common.R;
import com.loong.entity.Match;
import com.loong.entity.SignUp;
import com.loong.entity.UserAbility;
import com.loong.entity.dto.SignUpDTO;
import com.loong.entity.vo.SignUpVo;
import com.loong.mapper.MatchMapper;
import com.loong.mapper.SignUpMapper;
import com.loong.mapper.UserAbilityMapper;
import com.loong.service.ISignUpService;
import com.loong.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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
public class SignUpServiceImpl extends ServiceImpl<SignUpMapper, SignUp> implements ISignUpService {

    @Autowired
    private SignUpMapper signUpMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserAbilityMapper userAbilityMapper;

    @Autowired
    private MatchMapper matchMapper;

    @Override
    public void signUp(SignUpDTO signUp) {
        SignUp sign = new SignUp();
        sign.setSignUpTime(LocalDateTime.now());
        sign.setUserId(signUp.getUserId());
        sign.setMatchId(signUp.getMatchId());
        sign.setContact(signUp.getContact());
        sign.setNickname(signUp.getNickname());

        signUpMapper.insert(sign);
    }

    /**
     * 生成每一个小组的比赛名单
     * @param userAbilities
     * @return
     */
    private List<SignUp> generatePlayers(List<UserAbility>userAbilities,Long matchId){
        LinkedList<SignUp> signUps = new LinkedList<>();
        userAbilities.forEach(userAbility -> {
            SignUp signUp = signUpMapper.selectOne(new LambdaQueryWrapper<SignUp>()
                    .eq(SignUp::getUserId, userAbility.getUserId())
                    .eq(SignUp::getMatchId, matchId));
            signUps.add(signUp);
        });
        return signUps;
    }

    // 完成小组赛分组
    @Override
    public R generateGroup(Long matchId) {

        List<List<SignUp>>result=new LinkedList<>();

        List<Object> objects = redisUtil.lGet(matchId + "_generate_group", 0, -1);
        if(objects!=null&& !objects.isEmpty()){
            return R.success(objects);// 返回数据的格式为一组数组，数组中的每一个元素又是一个列表
        }
        // 查询所有报名参赛的选手
        LambdaQueryWrapper<SignUp> signUpLambdaQueryWrapper = new LambdaQueryWrapper<>();
        signUpLambdaQueryWrapper.eq(SignUp::getMatchId, matchId);

        List<SignUp> signUps = signUpMapper.selectList(signUpLambdaQueryWrapper);

        // 根据报名列表查询参赛选手的能力
        List<UserAbility>userAbilities=new LinkedList<>();
        for (SignUp signUp : signUps) {
            Long userId = signUp.getUserId();
            UserAbility userAbility=userAbilityMapper.getUserAbility(userId);
            if(userAbility!=null) userAbilities.add(userAbility);
        }
        Collections.shuffle(userAbilities);

        // 分成若干个小组，每个小组内6名选手
        int groups=signUps.size()/6;

        // 对每个小组进行校验
        int repeatGroup=0;// 重新分组的数量
        List<UserAbility>repeatGrouplist=new LinkedList<>();// 重新分组
        for (int i = 0; i < groups; i++) {
            List<UserAbility> subList = userAbilities.subList(i * 6, Math.min(i * 6 + 6, userAbilities.size()));
            // 按照策略从小到大排序
            comparatorOfUserAbility(subList);
            // 排序完成之后，进行校验
            UserAbility last = subList.get(0);
            UserAbility first = subList.get(subList.size() - 1);

            double abilityMinus=
                    (last.getServe()-first.getServe())*0.3
                            +(last.getReceive()-first.getReceive())*0.3
                            +Math.abs(last.getAssault()-first.getAssault())*0.2
                            +(last.getDefend()-first.getDefend())*0.2;

            if(abilityMinus>3){
                repeatGroup++;
                repeatGrouplist.addAll(subList);
            }else{
                // 根据sublist添加user到最后的结果中
                result.add(generatePlayers(subList,matchId));
            }
        }

        // 接下来对不满足条件的再次进行分组
        comparatorOfUserAbility(repeatGrouplist);
        // 第一个小组排名倒1，第二个小组排名倒2，依次类推
        for(int i=0;i<repeatGroup;i++){
            LinkedList<UserAbility> abilities = new LinkedList<>();

            for(int j=0;j<6;j++){
                if(6*j+i<repeatGrouplist.size()) abilities.add(repeatGrouplist.get(6*i+j));
            }
            result.add(generatePlayers(abilities,matchId));
        }
        List<Object> list = result.stream().map(match -> (Object) match).toList();
        redisUtil.lSet(matchId + "_generate_group", list);
        return R.success(result);

    }

    @Override
    public List<SignUpVo> getSignUpList(Long userId) {

        List<SignUpVo> signUpVos = new LinkedList<>();
        LambdaQueryWrapper<SignUp> queryWrapper = new LambdaQueryWrapper<SignUp>().eq(SignUp::getUserId, userId);
        List<SignUp> signUps = signUpMapper.selectList(queryWrapper);

        for (SignUp signUp : signUps) {
            SignUpVo signUpVo = new SignUpVo();
            signUpVo.setSignUp(signUp);
            Match match = matchMapper.selectById(signUp.getMatchId());
            signUpVo.setMatch(match);

            // 判断选手的奖励
            if(signUp.getState()==2){
                // 选手以参赛
                signUpVo.setAwards(match.getAwards()*(9-signUp.getRankness())/8);
            }
            signUpVos.add(signUpVo);
        }
        return signUpVos;
    }

    @Override
    public void cancelSignUp(Long matchId, Long userId) {

        // 更新sign_up表
        signUpMapper.delete(new LambdaQueryWrapper<SignUp>().eq(SignUp::getMatchId, matchId).eq(SignUp::getUserId, userId));

        // 更新match表
        Match match = matchMapper.selectById(matchId);
        match.setPlayerNumber(match.getPlayerNumber()-1);
        matchMapper.updateById(match);

    }

    private void comparatorOfUserAbility(List<UserAbility> repeatGrouplist) {
        repeatGrouplist.sort((ability1,ability2)->{
            int serve=ability1.getServe()-ability2.getServe();
            if(serve!=0)return serve;
            else{
                int returnValue=ability1.getReceive()-ability2.getReceive();
                if(returnValue!=0)return returnValue;
                else{
                    int assault=ability1.getAssault()-ability2.getAssault();
                    if(assault!=0)return assault;
                    else{
                        return ability1.getDefend()-ability2.getDefend();
                    }
                }
            }
        });
    }
}
