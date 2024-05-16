package com.loong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.loong.common.R;
import com.loong.entity.SignUp;
import com.loong.entity.dto.SignUpDTO;
import com.loong.service.IMatchService;
import com.loong.service.ISignUpService;
import com.loong.service.impl.UserAbilityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  选手报名控制器
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
@RestController
@RequestMapping("/signUp")
public class SignUpController {
    @Autowired
    private ISignUpService signUpService;

    @Autowired
    private IMatchService matchService;

    @Autowired
    private UserAbilityServiceImpl userAbilityService;

    @PostMapping("/sign")
    @Transactional
    public R signUp(@RequestBody SignUpDTO signUp){
        signUpService.signUp(signUp);
        // 更新报名人数
        matchService.updatePlayerNumber(signUp.getMatchId());
        return R.success(null,"报名成功");
    }

    @GetMapping("/isSignUp")
    public R isSignUp(Integer matchId,Integer userId){

        LambdaQueryWrapper<SignUp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SignUp::getMatchId,matchId).eq(SignUp::getUserId,userId);
        SignUp sign = signUpService.getOne(queryWrapper);
        return R.success(sign!=null);

    }

    /**
     * 分组的策略：
     * 1. 小组内部选手按照发球能力，接发球能力，进攻能力以及防守反击能力从大到小进行排序
     * 2. 计算排名最高的选手和排名最差的选手在各项能力上的差值，
     * 各个能力的差值按照权重30%，30%，20%，20%计算得到综合能力差值
     * 3. 如果综合能力差值大于3，该小组应当重新分配。
     * 4. 对于每个综合能力差值大于三的小组，数量为n。
     * 将他们按照按照发球能力，接发球能力，进攻能力以及防守反击能力从大到小进行排序，排序完成之后。
     * 第一组选排名最低的选手，第二组选排名第2低的选手，……第n组选排名第n低的选手；接下来，
     * 第一组选排名第n+1低的选手，第一组选排名第n+2低的选手，……第n组选排名第n+n低的选手。
     * 依次类推，直到每个小组都选完。选完之后，每个小组可能还是不能满足综合能力差值条件，
     * 但是已经尽可能照顾到了综合能力较差的选手。因此在该轮分配之后，将不再第二轮分配。
     * @param matchId
     * @return
     */
    @GetMapping("/generate")
    public R generate(Long matchId){

        return signUpService.generateGroup(matchId);
    }

    @GetMapping("/signUps")
    public R getSignUpList(Long userId){
        return R.success(signUpService.getSignUpList(userId));
    }

    @DeleteMapping("/cancel")
    public R cancelSignUp(Long matchId,Long userId){
        signUpService.cancelSignUp(matchId,userId);
        return R.success(null,"取消报名成功");
    }


}
