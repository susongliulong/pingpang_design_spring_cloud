package com.loong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loong.entity.BasicInformation;
import com.loong.entity.Category;
import com.loong.entity.Tutorial;
import com.loong.entity.menu.Menu;
import com.loong.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author loong
 * @since 2024-04-21
 */
public interface TutorialMapper extends BaseMapper<Tutorial> {

    @Select("select * from tutorial_category")
    List<Category> getCategories();

    @Select("select category from tutorial_category where id = #{categoryId}")
    String getLevelName(Long categoryId);

    @Insert("insert into tutorial(tutorial_id, level, suitable_object, content) values(#{id}, #{level}, #{suitableObject}, #{content})")
    void insertTurorial(Long id, Long level, String suitableObject, String content);

    @Select("select suitable_object from tutorial where tutorial_id = #{id}")
    String getSuitableObject(Long id);


    Tutorial getByTutorialId(long tutorialId);

    BasicInformation getBasicInformationById(long id);

    @Update("update tutorial set level = #{level}, suitable_object = #{suitableObject}, content = #{content} where tutorial_id = #{id}")
    void updateTutorial(Long id, Long level, String suitableObject, String content);

    List<Menu> getMenus();

    User getAuthor(long userId);
}
