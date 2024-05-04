package com.loong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user/view")
public class ViewController {

    @GetMapping("/privacy")
    public String privacy(){
        return "privacy";
    }
}
