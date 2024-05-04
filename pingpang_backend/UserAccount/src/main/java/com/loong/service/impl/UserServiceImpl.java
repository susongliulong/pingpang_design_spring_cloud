package com.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.common.R;
import com.loong.common.state.RofAccount;
import com.loong.entity.User;
import com.loong.entity.dto.UserDto;
import com.loong.entity.vo.UserVo;
import com.loong.feign.CommentFeign;
import com.loong.feign.MatchClient;
import com.loong.feign.NewsSharingClient;
import com.loong.feign.TutorialFeign;
import com.loong.mapper.UserMapper;
import com.loong.service.UserService;
import com.loong.service.checkcode.impl.CheckCodeServiceImpl;
import com.loong.util.EncryptUtil;
import com.loong.util.RSAUtil;
import com.loong.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-04-07
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CheckCodeServiceImpl checkCodeService;

    @Value("${avatar.path}")
    private  String avatarPath;

    @Value("${avatar.defaultImgNumber}")
    private Integer defaultAvatarNumber;

    private final Random random=new Random();

    @Autowired
    private CommentFeign commentFeign;


    @Autowired
    private NewsSharingClient newsSharingClient;

    @Autowired
    private MatchClient matchClient;

    @Autowired
    private TutorialFeign tutorialFeign;

    @Override
    public void register(UserDto userDto) {
        // 更新用户数据
        // 在用户注册的时候对用户密码信息进行加密，存储到后端
        // 生成用户默认图像，在后台文件中选取

        userDto.setPassword(EncryptUtil.bcrypt(userDto.getPassword()));
        userDto.setRegisterTime(LocalDateTime.now());
        userDto.setAvatar(avatarPath+"test"+random.nextInt(defaultAvatarNumber)+".jpg");
        userMapper.insert(userDto);
        userDto.getInterests().forEach(interest -> {
            userMapper.insertInterest(userDto.getId(), interest);
        });

        // 同步更新其他数据,后续可以使用多线程技术进行优化，进行后台更新
        commentFeign.addUser(userDto);
        newsSharingClient.addUser(userDto);
        matchClient.addUser(userDto);
        tutorialFeign.addUser(userDto);
    }

    @Override
    public void updateInterestsMessage(long userId, List<Integer> interests) {
        userMapper.deleteInterestsById(userId);
        interests.forEach(interest -> userMapper.insertInterest(userId, interest));
    }

    @Override
    public List<Integer> interests(long userId) {
        return userMapper.selectInterests(userId);
    }

    @Override
    public R login(String account, String password, String checkCode){

        if (redisUtil.get(account + "suspend_login") != null) {
            return R.error("失败次数超过三次，将暂停一分钟登录");
        } else {
            if (redisUtil.get(account + "login_fail_count") != null && (Integer) redisUtil.get(account + "login_fail_count") >= 3) {
                redisUtil.del(account + "login_fail_count");
            }
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getTelephone, account).or()
                .eq(User::getEmail, account);

        User user = userMapper.selectOne(queryWrapper);
        if (user != null && user.getState() == 1) {
            // 对password进行解密
            try {
                String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM00WbC52QTBZczrcKVYi/FzI/FTpxfY8/ngaGjbNXE3ZdEOvzyNTJtsefceDAQsXsTV7/lgNvP5vYTUjT5HuqVfDxZW9Clw8Z4Ia9vo30es/3luMlD9G+2wQSyqEEBPEq+RXmVc4q9ECk/KHTkinAdeqF+yf/Uu8btG2HHqoeBXAgMBAAECgYAJO2w3l7zsSlkupX8NaVTueXwp/NX9LC0Z1l9MLGSUoeXqSGwRKNhD+9bm58qpm2duGqPvx8ThKCC/K6LpJOgAOP2kEzq6tKy3pODleKM6WMzPDGuWnEHxTfnALPPGNtvsFumi7CPZVw18uWCD3kobRPcYaVrCCLUodMAhBF7wZQJBAPen4aCzs1y42K0n8MzGFdkmjU+SqrU+iAm2jnmm4anFHBFq0m4TkXnTZF9Dm7drAIzEQYVcj+OIeVdn9u6w3LsCQQDUHk/Ez9AfpVEBGGOpYwoliPiD+oILj6QVf1xyJD5V7yjk/qiHKUjc8Ke049a9SR15hCUVSliI0oRoKRaHbX8VAkEAwG9m1uRLfsTzOLlMVxGkcHOQYkBo/SR1uHZuBwExYCWVRcNnd7kk1TD5UTzLhp5Scw8cim/+LqMhBlX2fkpXeQJAdUQYp16X2ZbkNG3OUwuMxXl2okfwcwHDHZteeT+uMoXYHtjmRGq/fF2S6X737YcpQ0+ZzK5gSavQRWFJsi8HjQJAemAAdzN1N1HVZ65UwKN/vrtX4AcXy9X5CATeCgZfVIc0gc0z7B4YSdZYMtYzeADd963qZvTtnzRW/aQvQtTg6Q==";
                password= RSAUtil.decryptByPrivateKey(password,privateKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (EncryptUtil.bcryptMatch(password,user.getPassword())) {
                // 对用户输入饭日验证码进行校验
                if (checkCodeService.checkCode(checkCode, account)) {
                    //登录成功，删除键值
                    redisUtil.del(account + "login_fail_count", account + "suspend_login");
                    // 查询用户感兴趣话题
                    List<Integer> interests = this.interests(user.getId());
                    UserVo userVo = new UserVo();
                    userVo.setUser(user);
                    userVo.setInterests(interests);
                    // 跟新用户登录时间
                    user.setLastLoginTime(LocalDateTime.now());
                    userMapper.updateById(user);

                    return R.success(userVo, RofAccount.LOGIN_SUCCESS.getMessage());
                }
            }
        }
        // 超过三次登录失败此时应当暂停登录一分钟
        Object count = redisUtil.get(account + "login_fail_count");
        if (count == null) {
            redisUtil.set(account + "login_fail_count", 1);
        } else {
            redisUtil.set(account + "login_fail_count", (Integer) count + 1);
            if ((Integer) count + 1 >= 3) {
                // 暂停登录一分钟
                redisUtil.set(account + "suspend_login", "true", 60);
            }
        }
        if (user == null) {
            return R.warn("当前用户不存在");
        } else {
            if (!EncryptUtil.bcryptMatch(password, user.getPassword())) {
                return R.warn(RofAccount.LOGIN_ERROR.getMessage());
            }

            if (user.getState() != 1) { // 1表示允许登录
                return R.warn("用户已经被禁止登录");
            }
        }
        return R.warn("用户登录失败");
    }

    @Override
    public R logout(Long id) {
        userMapper.deleteById(id);
        commentFeign.deleteUser(id);
        newsSharingClient.deleteUser(id);
        matchClient.deleteUser(id);
        tutorialFeign.deleteUser(id);

        return R.success("账号注销成功");
    }

    @Override
    public R updateUser(UserDto userDto) {
        userDto.setTelephone(userMapper.selectById(userDto.getId()).getTelephone());
        userDto.setEmail(userMapper.selectById(userDto.getId()).getEmail());
        String password =EncryptUtil.bcrypt(userDto.getPassword());
        userDto.setPassword(password);
        userMapper.updateById(userDto);
        updateInterestsMessage(userDto.getId(), userDto.getInterests());

        // 同步更新其他的数据库
        commentFeign.updateUser(userDto);
        newsSharingClient.updateUser(userDto);
        matchClient.updateUser(userDto);
        tutorialFeign.updateUser(userDto);

        return R.success("账号信息修改成功");
    }

}
