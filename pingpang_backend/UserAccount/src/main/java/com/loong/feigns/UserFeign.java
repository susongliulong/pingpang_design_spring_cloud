package com.loong.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UserAccount")
public interface UserFeign {

    @GetMapping("/code/check")
    public boolean checkCode(@RequestParam("checkCode") String checkCode);
}
