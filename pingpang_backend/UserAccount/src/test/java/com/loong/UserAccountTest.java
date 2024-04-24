package com.loong;

import com.loong.entity.User;
import com.loong.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = {UserAccount.class})
@RunWith(SpringRunner.class)
public class UserAccountTest {


    @Resource
    private UserMapper userMapper;

    @Test
    public void test1(){}{
        String path="http:http://localhost:12345/avatar/download?filename=";

        // 查询id，根据id匹配对应的图像
        List<User> users = userMapper.selectList(null);
        users.forEach(user -> {
            user.setAvatar(path+"test"+user.getId()%6+".jpg");
            user.setDescription("这个人很懒什么都没有留下！");
            userMapper.updateById(user);
        });
    }
}
