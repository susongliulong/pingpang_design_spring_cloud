package com.loong.controller;

import com.loong.common.R;
import com.loong.entity.dto.UserDto;
import com.loong.service.impl.MatchServiceImpl;
import com.loong.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MatchServiceImpl matchService;
    @PostMapping("/add")
    public R addUser( @RequestBody UserDto userDTO){
        userService.save(userDTO);
        return R.success("success");
    }

    @PutMapping("/update")
    public R updateUser(@RequestBody UserDto userDTO){
        userService.saveOrUpdate(userDTO);
        return R.success("success");
    }

    @DeleteMapping("/delete/{id}")
    public R deleteUser(@PathVariable("id") Long id){
        userService.removeById(id);
        // 删除用户所有数据(配置了外键，使用了级联删除)
        matchService.deleteAllData(id);
        return R.success("success");
    }
}
