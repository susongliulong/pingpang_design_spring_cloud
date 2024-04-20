package com.loong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.loong.common.R;
import com.loong.entity.BasicInformation;
import com.loong.entity.dto.NewsDTO;
import com.loong.service.IBasicInformationService;
import com.loong.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/newsMessage")
@RestController
public class NewsMessageController {

    @Autowired
    private INewsService iNewsService;

    @Autowired
    private IBasicInformationService iBasicInformationService;


    @PostMapping("/post")
    public R newsMessage(@RequestBody NewsDTO newsDTO){
        iNewsService.saveNews(newsDTO);
        return R.success("资讯发布成功");
    }

    @GetMapping("/newsMessage")
    public R newsMessage(long userId){
        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicInformation::getAuthorId,userId);
        return R.success(iBasicInformationService.list(queryWrapper));
    }
}
