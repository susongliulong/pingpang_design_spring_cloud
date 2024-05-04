package com.loong.service;

import com.loong.entity.BasicInformation;
import com.loong.entity.LinkItem;
import com.loong.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;
import com.loong.entity.dto.NewsDTO;

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

    List<BasicInformation> recommendArticles(Integer page);

    List<BasicInformation> recommendArticles(List<Integer> integers,Integer page);

    void updateNewsMessage(BasicInformation basicInformation);

    News getNewsMessage(long newsId);

    String getNewsAuthor(long authorId);

    List<BasicInformation> getNewsByKeyWord(String keyWord);

    List<LinkItem> getKeyWords(String keyWord);

    void saveNews(NewsDTO newsDTO);

    void deleteAllData(Long id);
}
