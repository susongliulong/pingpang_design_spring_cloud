package com.loong.controller;

import com.loong.common.R;
import com.loong.entity.dto.UserDto;
import com.loong.service.impl.CommentServiceImpl;
import com.loong.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CommentServiceImpl commentService;

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

        // 删除用户所有的评论数据
        commentService.deleteAllDataByUserId(id);
        return R.success("success");
    }
}
