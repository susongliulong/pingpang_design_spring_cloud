package com.loong;

import com.loong.entity.User;
import com.loong.entity.dto.UserDto;
import com.loong.mapper.UserMapper;
import com.loong.service.UserService;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MapperTest {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    /**
     * 测试mp插入时是否自动排除表中没有的属性，测试通过
     */
    @Test
    public void test(){
        UserDto userDto = new UserDto();
        userDto.setEmail("123");
        userDto.setPassword("123");
        userDto.setConfirm("123");
        userMapper.insert(userDto);
    }

    @Test
    public void test1(){
        System.out.println(userMapper.selectInterests(1));
    }

    // 账号默认的头像数据
    @Test
    public void test3(){}{

        String path="http://localhost:12345/avatar/download?filename=";

        // 查询id，根据id匹配对应的图像
        List<User> users = userMapper.selectList(null);
        users.forEach(user -> {
            user.setAvatar(path+"test"+user.getId()%6+".jpg");
            user.setDescription("这个人很懒什么都没有留下！");
            userMapper.updateById(user);
        });
    }

}
