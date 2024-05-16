package com.loong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loong.entity.UserAbility;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
public interface UserAbilityMapper extends BaseMapper<UserAbility> {

    @Select("select name from ability where category = #{name}")
    String[] getAbilities(String name);

    @Select("select * from user_ability where user_id = #{userId}")
    UserAbility getUserAbility(Long userId);
}
