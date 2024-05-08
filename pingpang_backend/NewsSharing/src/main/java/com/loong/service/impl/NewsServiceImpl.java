package com.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.entity.BasicInformation;
import com.loong.entity.LinkItem;
import com.loong.entity.News;
import com.loong.entity.NewsCategory;
import com.loong.entity.dto.NewsDTO;
import com.loong.entity.vo.NewsVo;
import com.loong.mapper.BasicInformationMapper;
import com.loong.mapper.NewsMapper;
import com.loong.service.INewsService;
import com.loong.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${news.expire.time}")
    private Integer NEWS_EXPIRE_TIME;

    @Override
    public String category(Long categoryId) {
        return newsMapper.getCategoryNameById(categoryId);
    }

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
    public List<BasicInformation> getNewsByKeyWord(LocalDateTime startTime, LocalDateTime endTime, Long category, String keyWord, String activeName, Integer pageNumber) {
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
    public BasicInformation saveNews(NewsDTO newsDTO) {

        // 判断是否修改文章
        if (newsDTO.getId() != null) {
            basicInformationMapper.deleteById(newsDTO.getId());
            newsMapper.deleteById(newsDTO.getId());
        }

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
        return basicInformation;
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

    /**
     * 根据关键词进行查询
     * @param startTime
     * @param endTime
     * @param category
     * @param searchContent
     * @param activeName
     * @return
     */
    @Override
    public List<NewsVo> getNewsByPage(LocalDateTime startTime, LocalDateTime endTime, Long category, String searchContent, String activeName,Integer pageNumber) {
        Integer state = switch (activeName) {
            case "tabs0" -> 0;
            case "tabs1" -> 1;
            case "tabs2" -> 2;
            case "tabs3" -> 3;
            case "tabs4" -> 4;
            default -> 5;
        };
        Page<BasicInformation> page = new Page<>(pageNumber, 10);
        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        if(state<5){
            queryWrapper.eq(BasicInformation::getState, state);
        }
        if(startTime!=null){
            queryWrapper.ge(BasicInformation::getPublishTime, startTime);
        }
        if(endTime!=null){
            queryWrapper.le(BasicInformation::getPublishTime, endTime);
        }
        if (category != null) {
            queryWrapper.eq(BasicInformation::getCategoryId, category);
        }
        if (searchContent != null) {
            queryWrapper.like(BasicInformation::getTitle, searchContent);
        }
        basicInformationMapper.selectPage(page, queryWrapper);
        List<BasicInformation> basicInformation = page.getRecords();
        List<NewsVo> newsVoList = new LinkedList<>();
        basicInformation.forEach(news->{
            NewsVo newsVo = new NewsVo();
            newsVo.setBasicInformation(news);
            newsVo.setId(news.getId());
            newsVo.setName(newsMapper.getCategoryNameById(news.getCategoryId()));
            newsVoList.add(newsVo);
        });
        return newsVoList;
    }

    @Override
    public NewsDTO getNews(Long newsId, Long userId) {

        // 根据newsId查询指定的文章信息
        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicInformation::getId, newsId);

        BasicInformation basicInformation = basicInformationMapper.selectOne(queryWrapper);
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(basicInformation.getId());
        newsDTO.setAuthorId(basicInformation.getAuthorId());
        newsDTO.setCategoryId(basicInformation.getCategoryId());
        newsDTO.setTitle(basicInformation.getTitle());
        newsDTO.setContent(newsMapper.selectById(newsId).getContent());
        newsDTO.setImageUrl(basicInformation.getCoverImage());
        return newsDTO;
    }

    @Override
    public Boolean deleteNews(Long newsId) {
        BasicInformation basicInformation = basicInformationMapper.selectById(newsId);
        basicInformation.setState(5);
        basicInformationMapper.updateById(basicInformation);

        // 设置键值过期时间，到时候在真正从数据库中删除文章,测试环境下默认保存的时间为1分钟
        redisUtil.set("newsId_expire"+newsId,newsId,NEWS_EXPIRE_TIME);

        return true;
    }

    /**
     * 真正删除谋篇文章
     * @param newsId
     */
    @Override
    public void deleteNewsById(Long newsId) {
        basicInformationMapper.deleteById(newsId);
        newsMapper.deleteById(newsId);
    }

    @Override
    public Long [] tabs() {
        Long [] integers = new Long [5];
        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        for(int i=0;i<5;i++){
            queryWrapper.clear();
            integers[i]=basicInformationMapper.selectCount(queryWrapper.eq(BasicInformation::getState,i));
        }
        return integers;
    }


}
