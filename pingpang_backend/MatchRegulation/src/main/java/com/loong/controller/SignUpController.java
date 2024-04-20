package com.loong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.loong.common.R;
import com.loong.entity.SignUp;
import com.loong.entity.dto.SignUpDTO;
import com.loong.service.IMatchService;
import com.loong.service.ISignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
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

    @PostMapping("/sign")
    @Transactional
    public R signUp(@RequestBody SignUpDTO signUp){

        SignUp sign = new SignUp();
        sign.setSignUpTime(LocalDateTime.now());
        sign.setUserId(signUp.getUserId());
        sign.setMatchId(signUp.getMatchId());

        matchService.updatePlayerNumber(signUp.getMatchId());
        // 更新报名人数
        signUpService.save(sign);
        return R.success(null,"报名成功");
    }

    @GetMapping("/isSignUp")
    public R isSignUp(Integer matchId,Integer userId){

        LambdaQueryWrapper<SignUp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SignUp::getMatchId,matchId).eq(SignUp::getUserId,userId);
        SignUp sign = signUpService.getOne(queryWrapper);
        return R.success(sign!=null);

    }
}
