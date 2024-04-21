package com.loong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.entity.BasicInformation;
import com.loong.entity.Category;
import com.loong.entity.Tutorial;
import com.loong.entity.vo.TutorialVo;
import com.loong.mapper.BasicInformationMapper;
import com.loong.mapper.TutorialMapper;
import com.loong.service.ITutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-04-21
 */
@Service
public class TutorialServiceImpl extends ServiceImpl<TutorialMapper, Tutorial> implements ITutorialService {

    @Autowired
    private TutorialMapper tutorialMapper;

    @Autowired
    private BasicInformationMapper basicInformationMapper;

    @Override
    public List<Category> getCategories() {
        return tutorialMapper.getCategories();
    }

    @Override
    public List<TutorialVo> getTutorialsByUserId(long userId) {

        LambdaQueryWrapper<BasicInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicInformation::getAuthorId, userId);

        List<TutorialVo> tutorialVos = new LinkedList<>();

        List<BasicInformation> basicInformations = basicInformationMapper.selectList(queryWrapper);
        basicInformations.forEach(
                basicInformation -> {
                    TutorialVo tutorialVo = new TutorialVo();
                    tutorialVo.setBasicInformation(basicInformation);
                    tutorialVo.setLevelName(tutorialMapper.getLevelName(basicInformation.getCategoryId()));
                    tutorialVo.setSuitableObject(tutorialMapper.getSuitableObject(basicInformation.getId()));
                    tutorialVo.setTutorialId(basicInformation.getId());
                    tutorialVos.add(tutorialVo);
                }
        );
        return tutorialVos;
    }

    @Override
    @Transactional
    public void addTutorial(Tutorial tutorial) {

        BasicInformation target = tutorialMapper.getBasicInformationById(tutorial.getTutorialId());
        BasicInformation basicInformation = tutorial.getBasicInformation();
        basicInformation.setCategoryId(tutorial.getLevel());
        basicInformation.setPublishTime(LocalDateTime.now());
        if (target == null) {
            basicInformationMapper.insert(basicInformation);
            // 跟新Tutorial表内容
            tutorialMapper.insertTurorial(tutorial.getBasicInformation().getId(), tutorial.getLevel(), tutorial.getSuitableObject(), tutorial.getContent());
        }else{
            basicInformationMapper.updateById(basicInformation);
            tutorialMapper.updateTutorial(tutorial.getBasicInformation().getId(), tutorial.getLevel(), tutorial.getSuitableObject(), tutorial.getContent());
        }
    }

    @Override
    public Tutorial getByTutorialId(long tutorialId) {
        return tutorialMapper.getByTutorialId(tutorialId);
    }
}
