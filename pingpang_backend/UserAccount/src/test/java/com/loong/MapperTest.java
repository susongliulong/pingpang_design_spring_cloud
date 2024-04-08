package com.loong;

import com.loong.entity.dto.UserDto;
import com.loong.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = UserAccountApplication.class)
@RunWith(SpringRunner.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

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
}
