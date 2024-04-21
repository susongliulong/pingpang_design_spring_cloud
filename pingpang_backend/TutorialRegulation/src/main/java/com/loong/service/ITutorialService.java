package com.loong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loong.entity.Category;
import com.loong.entity.Tutorial;
import com.loong.entity.vo.TutorialVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author loong
 * @since 2024-04-21
 */
public interface ITutorialService extends IService<Tutorial> {

    List<Category> getCategories();

    List<TutorialVo> getTutorialsByUserId(long userId);

    void addTutorial(Tutorial tutorial);

    Tutorial getByTutorialId(long tutorialId);
}
