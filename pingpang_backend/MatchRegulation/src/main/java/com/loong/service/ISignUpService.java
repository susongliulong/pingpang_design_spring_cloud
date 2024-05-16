package com.loong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loong.common.R;
import com.loong.entity.SignUp;
import com.loong.entity.dto.SignUpDTO;
import com.loong.entity.vo.SignUpVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
public interface ISignUpService extends IService<SignUp> {

    void signUp(SignUpDTO signUp);

    R generateGroup(Long matchId);

    List<SignUpVo> getSignUpList(Long userId);

    void cancelSignUp(Long matchId, Long userId);
}
