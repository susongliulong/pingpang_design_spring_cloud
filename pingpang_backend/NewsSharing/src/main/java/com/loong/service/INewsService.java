package com.loong.service;

import com.loong.entity.BasicInformation;
import com.loong.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;

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

    List<BasicInformation> recommendArticles();

    List<BasicInformation> recommendArticles(List<Integer> integers);
}
