package com.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.common.R;
import com.loong.common.state.RofAccount;
import com.loong.entity.User;
import com.loong.entity.dto.UserDto;
import com.loong.entity.vo.UserVo;
import com.loong.mapper.UserMapper;
import com.loong.service.UserService;
import com.loong.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
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

    @Override
    public void register(UserDto userDto) {
        // 跟新用户数据
        userMapper.insert(userDto);
        userDto.getInterests().forEach(interest -> {
            userMapper.insertInterest(userDto.getId(), interest);
        });
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
    public R login(String account, String password) {
        if(redisUtil.get(account+"suspend_login") != null){
            return R.error("失败次数超过三次，将暂停一分钟登录");
        }else{
            if(redisUtil.get(account+"login_fail_count") != null && (Integer)redisUtil.get(account+"login_fail_count") >= 3){
                redisUtil.del(account+"login_fail_count");
            }
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getTelephone, account).or()
                .eq(User::getEmail, account);

        User user = userMapper.selectOne(queryWrapper);
        if (user!=null &&user.getPassword().equals(password) && user.getState() == 1) {
            //登录成功，删除键值
            redisUtil.del(account+"login_fail_count",account+"suspend_login");
            // 查询用户感兴趣话题
            List<Integer>interests = this.interests(user.getId());
            UserVo userVo = new UserVo();
            userVo.setUser(user);userVo.setInterests(interests);
            return R.success(userVo, RofAccount.LOGIN_SUCCESS.getMessage());
        }
        // 超过三次登录失败此时应当暂停登录一分钟
        Object count = redisUtil.get(account+"login_fail_count");
        if (count == null) {
            redisUtil.set(account+"login_fail_count", 1);
        } else {
            redisUtil.set(account+"login_fail_count", (Integer) count + 1);
            if ((Integer) count + 1 >= 3) {
                // 暂停登录一分钟
                redisUtil.set(account+"suspend_login", "true", 60);
            }
        }
        if(user==null){
            return R.warn("当前用户不存在");
        }else{
            if (!user.getPassword().equals(password)) {
                return R.warn(RofAccount.LOGIN_ERROR.getMessage());
            }

            if(user.getState() != 1){ // 1表示允许登录
                return R.warn("用户已经被禁止登录");
            }
        }
        return R.warn("用户登录失败");
    }

}
