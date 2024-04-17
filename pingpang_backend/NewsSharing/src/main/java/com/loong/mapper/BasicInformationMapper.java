package com.loong.mapper;

import com.loong.entity.BasicInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loong.entity.LinkItem;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author loong
 * @since 2024-04-09
 */
public interface BasicInformationMapper extends BaseMapper<BasicInformation> {

    List<Object> getAllTopics();

    List<LinkItem> getKeyWords(String keyWord);
}
