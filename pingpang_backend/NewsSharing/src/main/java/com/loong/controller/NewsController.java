package com.loong.controller;

import com.loong.common.R;
import com.loong.entity.BasicInformation;
import com.loong.entity.LinkItem;
import com.loong.entity.News;
import com.loong.service.IInterestService;
import com.loong.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private IInterestService iInterestService;

    /**
     * 根据关键词查找内容
     * @param keyWord
     * @return
     */
    @GetMapping("/keyWord")
    public R getNewsByKeyWord(@RequestParam(value="keyword")String keyWord){
        List<BasicInformation>articles=iNewsService.getNewsByKeyWord(keyWord);
        return R.success(articles);
    }

    @GetMapping("/simpleKeyWord")
    public R getKeyWords(String keyWord){
        List<LinkItem>links=iNewsService.getKeyWords(keyWord);
        return R.success(links);
    }

    @GetMapping("/overview")
    public R overview(@RequestParam(value = "integers", required = false) List<Integer> integers,
    @RequestParam(value = "page", required = false) Integer page) {
        // 处理为空的情况
        List<BasicInformation> articles;
        if (integers == null || integers.isEmpty()) {
            articles = iNewsService.recommendArticles(page);
        } else {
            // 处理不为空的情况
            articles = iNewsService.recommendArticles(integers,page);
        }
        return R.success(articles);
    }

    @PostMapping("/update")
    public R updateNewsMessage(@RequestBody BasicInformation basicInformation){

        iNewsService.updateNewsMessage(basicInformation);
        return R.success("更新成功");
    }


    /**
     * 根据newsId获取新闻的详细信息并且返回
     * @param newsId 新闻id
     * @return 新闻的详细信息
     */
    @GetMapping("/message/{newsId}")
    public R newsMessage(@PathVariable long newsId){
        News news = iNewsService.getNewsMessage(newsId);
        return R.success(news);
    }

    @GetMapping("/interestName/{interestId}")
    public R getInterestName(@PathVariable long interestId){
        return R.success(iInterestService.getById(interestId));
    }

    @GetMapping("/author/{authorId}")
    public R getNewsAuthor(@PathVariable long authorId){
        return R.success(iNewsService.getNewsAuthor(authorId));
    }
}
