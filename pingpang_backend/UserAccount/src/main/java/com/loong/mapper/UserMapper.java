package com.loong.mapper;

import com.loong.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author loong
 * @since 2024-04-07
 */
public interface UserMapper extends BaseMapper<User> {

    void insertInterest(Long userId, Integer interestId);

    List<Integer> selectInterests(long userId);

    @Delete("delete from user_interest where user_id = #{userId}")
    void deleteInterestsById(long userId);
}
