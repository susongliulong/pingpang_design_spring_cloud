package com.loong;

import com.loong.entity.dto.UserDto;
import com.loong.mapper.UserMapper;
import com.loong.service.UserService;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void test2(){
        userMapper.selectList(null).forEach(System.out::println);
    }


    @Test
    public void test6(){
        StackTraceElement[] stackTraces = new RuntimeException().getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            System.out.println(stackTrace.getClass());
        }
    }

}
