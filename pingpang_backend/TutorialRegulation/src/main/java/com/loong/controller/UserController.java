package com.loong.controller;

import com.loong.common.R;
import com.loong.entity.dto.UserDto;
import com.loong.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loong
 * @since 2024-05-04
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @PostMapping("/add")
    public R addUser(@RequestBody UserDto userDTO){
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
        return R.success("success");
    }
}
