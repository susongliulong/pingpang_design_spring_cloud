package com.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.entity.BasicInformation;
import com.loong.entity.LinkItem;
import com.loong.entity.News;
import com.loong.entity.NewsCategory;
import com.loong.entity.dto.NewsDTO;
import com.loong.mapper.BasicInformationMapper;
import com.loong.mapper.NewsMapper;
import com.loong.service.INewsService;
import com.loong.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-04-09
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

    @Autowired
    private BasicInformationMapper basicInformationMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private NewsMapper newsMapper;

    @Override
    public List<BasicInformation> recommendArticles(Integer pageIndex) {

        List<BasicInformation> articles = new LinkedList<>();
        // 获取所有感兴趣的话题
        List<Object> topics = redisUtil.lGet("topics", 0, -1);
        if (topics == null || topics.isEmpty()) {
            redisUtil.lSet("topics", basicInformationMapper.getAllTopics());
        }
        topics = redisUtil.lGet("topics", 0, -1);

        // 从上述话题中随机选取4个，各推荐该话题下的5篇文章，不足5篇时直接推荐
        Collections.shuffle(topics);
        topics = topics.subList(0, 4);

        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        topics.forEach(topic -> {
            queryWrapper.clear();
            queryWrapper.eq(BasicInformation::getCategoryId, ((NewsCategory) topic).getId());
            Page<BasicInformation> page;
            if (pageIndex == null) {
                page = new Page<>(1, 5);
            } else {
                page = new Page<>(pageIndex, 5);
            }
            basicInformationMapper.selectPage(page, queryWrapper);
            articles.addAll(page.getRecords());
        });
        // 再次打乱顺序
        Collections.shuffle(articles);

        return articles;

    }

    /**
     * 根据用户感兴趣话题推荐
     * @param interests
     * @param pageIndex
     * @return
     */
    @Override
    public List<BasicInformation> recommendArticles(List<Integer> interests, Integer pageIndex) {

        List<BasicInformation> articles = new LinkedList<>();
        // 根据用户感兴趣话题进行推荐
        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        interests.forEach(topic -> {
            queryWrapper.clear();
            queryWrapper.eq(BasicInformation::getCategoryId, topic);
            Page<BasicInformation> page;
            if (pageIndex == null) {
                page = new Page<>(1, 5);
            } else {
                page = new Page<>(pageIndex, 10);
            }
            basicInformationMapper.selectPage(page, queryWrapper);
            articles.addAll(page.getRecords());
        });
        // 再次打乱顺序
        Collections.shuffle(articles);
        return articles;

    }

    /**
     * 跟新新闻的描述性信息
     * @param basicInformation
     */
    @Override
    public void updateNewsMessage(BasicInformation basicInformation) {
        basicInformationMapper.updateById(basicInformation);
    }

    @Override
    public News getNewsMessage(long newsId) {

        // 首先在redis数据库中查找新闻的信息，没有找到存入对应的键值
        Object o = redisUtil.get("news_" + newsId);
        if(o==null){
            redisUtil.set("news_" + newsId, newsMapper.selectById(newsId));
        }

        return (News)redisUtil.get("news_" + newsId);
    }

    @Override
    public String getNewsAuthor(long authorId) {
        return newsMapper.getNewsAuthor(authorId);
    }

    @Override
    public List<BasicInformation> getNewsByKeyWord(String keyWord) {
        // 根据keyword模糊查询表basic_information中的数据
        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(BasicInformation::getTitle, keyWord);
        queryWrapper.orderByDesc(BasicInformation::getPublishTime);
        Page<BasicInformation> page = new Page<>(1, 20);
        basicInformationMapper.selectPage(page, queryWrapper);
        // 在搜索到的数据中返回最新的前20条数据
        return page.getRecords();
    }

    @Override
    public List<LinkItem> getKeyWords(String keyWord) {
        List<LinkItem> keyWords = basicInformationMapper.getKeyWords(keyWord);
        Collections.shuffle(keyWords);
        return keyWords.subList(0, Math.min(keyWords.size(), 10));
    }

    @Transactional
    @Override
    public void saveNews(NewsDTO newsDTO) {

        // 更新新闻数据
        BasicInformation basicInformation = new BasicInformation();
        basicInformation.setTitle(newsDTO.getTitle());
        basicInformation.setCategoryId(newsDTO.getCategoryId());
        basicInformation.setAuthorId(newsDTO.getAuthorId());
        basicInformation.setCoverImage(newsDTO.getImageUrl());
        String content = newsDTO.getContent();
        basicInformation.setMainContent(content.substring(0, Math.min(content.length(), 100)));
        basicInformation.setPublishTime(LocalDateTime.now());

        basicInformationMapper.insert(basicInformation);
        Long newsId = basicInformation.getId();

        // 更新新闻详细信息
        News news = new News();
        news.setNewsId(newsId);
        news.setCategoryId(newsDTO.getCategoryId());
        news.setContent(content);
        newsMapper.insert(news);
    }

    /**
     * 用户注销账号之后删除所有的数据
     * @param id
     */
    @Override
    public void deleteAllData(Long id) {

        basicInformationMapper.selectList(new LambdaQueryWrapper<BasicInformation>().eq(BasicInformation::getAuthorId, id))
                .forEach(
                        news->{
                            newsMapper.deleteById(news.getId());
                            basicInformationMapper.deleteById(news);
                        }
                );
    }
}
