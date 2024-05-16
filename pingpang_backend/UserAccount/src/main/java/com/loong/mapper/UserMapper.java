package com.loong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loong.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 获取非脱敏之后的数据
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    com.loong.entity.nosecrect.User getUserById(Long id);
}
