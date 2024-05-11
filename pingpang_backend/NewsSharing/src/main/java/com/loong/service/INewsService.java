package com.loong.service;

import com.loong.entity.BasicInformation;
import com.loong.entity.LinkItem;
import com.loong.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;
import com.loong.entity.dto.NewsDTO;
import com.loong.entity.vo.NewsVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author loong
 * @since 2024-04-09
 */
public interface INewsService extends IService<News> {

    String category(Long categoryId);

    List<BasicInformation> recommendArticles(Integer page);

    List<BasicInformation> recommendArticles(List<Integer> integers,Integer page);

    void updateNewsMessage(BasicInformation basicInformation);

    News getNewsMessage(long newsId);

    String getNewsAuthor(long authorId);

    List<BasicInformation> getNewsByKeyWord(LocalDateTime startTime, LocalDateTime endTime, Long category, String keyWord, String activeName, Integer page);

    List<LinkItem> getKeyWords(String keyWord);

    BasicInformation saveNews(NewsDTO newsDTO);

    void deleteAllData(Long id);

    Object getNews(Long newsId, Long userId);

    Boolean deleteNews(Long newsId);

    void deleteNewsById(Long newsId);

    Long [] tabs();

    List<NewsVo> getNewsByPage(LocalDateTime startTime, LocalDateTime endTime, Long category, String searchContent, String activeName, Integer page);

    BasicInformation saveDraft(NewsDTO newsDTO);
}
