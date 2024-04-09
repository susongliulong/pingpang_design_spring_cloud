package com.loong.controller;

import com.loong.common.R;
import com.loong.entity.BasicInformation;
import com.loong.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author loong
 * @since 2024-04-09
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    /**
     * 根基用户刚兴趣的话题返回相关资讯信息
     *
     * @param integers
     * @return
     */
    @Autowired
    private INewsService iNewsService;

    @GetMapping("/overview")
    public R overview(List<Integer> integers) {
        // 处理为空的情况
        List<BasicInformation> articles;
        if (integers == null || integers.isEmpty()) {
            articles = iNewsService.recommendArticles();
        } else {
            // 处理不为空的情况
            articles = iNewsService.recommendArticles(integers);
        }
        return R.success(articles);
    }
}
