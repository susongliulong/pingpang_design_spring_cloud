package com.loong.mapper;

import com.loong.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author loong
 * @since 2024-04-09
 */
public interface NewsMapper extends BaseMapper<News> {

    @Select("select name from user where id = #{authorId}")
    String getNewsAuthor(long authorId);

    @Select("select name from pingpang_news_db.interest where id = #{categoryId}")
    String getCategoryNameById(Long categoryId);

    @Select("select basic_information.*,news.content from news ,basic_information where news.news_id=#{newsId} and news.news_id=basic_information.id")
    Object getNews(long newsId, long userId);

}
