package com.loong;

import com.loong.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {UserAccount.class})
@RunWith(SpringRunner.class)
public class UserAccountTest {


    @Autowired
    private UserMapper userMapper;


//    @Test
//    public void test1(){}{
//        String path="http:http://localhost:12345/avatar/download?filename=";
//
//        // 查询id，根据id匹配对应的图像
//        List<User> users = userMapper.selectList(null);
//        users.forEach(user -> {
//            user.setAvatar(path+"test"+user.getId()%6+".jpg");
//            user.setDescription("这个人很懒什么都没有留下！");
//            userMapper.updateById(user);
//        });
//    }

    @Test
    public void test2(){

        System.out.println(userMapper.selectById(6L));
    }
}
