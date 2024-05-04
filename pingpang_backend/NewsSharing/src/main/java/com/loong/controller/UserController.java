package com.loong.controller;

import com.loong.common.R;
import com.loong.entity.dto.UserDto;
import com.loong.service.impl.NewsServiceImpl;
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

    @Autowired
    private NewsServiceImpl newsService;
    @PostMapping("/add")
    public R addUser( @RequestBody UserDto userDTO){
        userService.save(userDTO);
        userService.addInterests(userDTO.getId(),userDTO.getInterests());
        return R.success("success");
    }

    @PutMapping("/update")
    public R updateUser(@RequestBody UserDto userDTO){
        userService.saveOrUpdate(userDTO);
        userService.updateInterests(userDTO.getId(),userDTO.getInterests());
        return R.success("success");
    }

    @DeleteMapping("/delete/{id}")
    public R deleteUser(@PathVariable("id") Long id){
        userService.removeById(id);
        newsService.deleteAllData(id);

        return R.success("success");
    }
}
