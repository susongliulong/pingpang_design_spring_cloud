package com.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.entity.BasicInformation;
import com.loong.entity.News;
import com.loong.entity.NewsCategory;
import com.loong.mapper.BasicInformationMapper;
import com.loong.mapper.NewsMapper;
import com.loong.service.INewsService;
import com.loong.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
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
    public List<BasicInformation> recommendArticles() {

        List<BasicInformation> articles=new LinkedList<>();
        // 获取所有感兴趣的话题
        List<Object> topics = redisUtil.lGet("topics",0,-1);
        if(topics==null){
            redisUtil.lSet("topics",basicInformationMapper.getAllTopics());
        }
        topics=redisUtil.lGet("topics",0,-1);

        // 从上述话题中随机选取4个，各推荐该话题下的5篇文章
        Collections.shuffle(topics);
        topics=topics.subList(0,4);

        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        topics.forEach(topic->{
            queryWrapper.clear();
            queryWrapper.eq(BasicInformation::getCategoryId,((NewsCategory) topic).getId());
            Page<BasicInformation> page = new Page<>(1, 5);
            basicInformationMapper.selectPage(page, queryWrapper);
            articles.addAll(page.getRecords());
        });

        return articles;

    }

    @Override
    public List<BasicInformation> recommendArticles(List<Integer> integers) {
        return null;
    }
}
