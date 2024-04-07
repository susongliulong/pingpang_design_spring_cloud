package com.loong.feigns;


import com.loong.common.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("UserAccount")
public interface UserFeign {

    @GetMapping("/user/test")
    public String test();

    @GetMapping("/user/login")
    public R login(@RequestParam("account") String account, @RequestParam("password") String password);

    @GetMapping("/code/check")
    boolean checkCode(@RequestParam("checkCode") String checkCode);
}
