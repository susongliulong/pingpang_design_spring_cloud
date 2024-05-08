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
        // 现根据id查询
        BasicInformation basicInformation = iNewsService.saveNews(newsDTO);
        return R.success(basicInformation,"发布资讯成功");
    }

    @GetMapping("/newsMessages")
    public R newsMessage(long userId){
        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicInformation::getAuthorId,userId);
        return R.success(iBasicInformationService.list(queryWrapper));
    }

    @GetMapping("/newsMessage")
    public R newsMessage(Long newsId,@RequestParam(required = false) Long userId){
        return R.success(iNewsService.getNews(newsId,userId));
    }
}
