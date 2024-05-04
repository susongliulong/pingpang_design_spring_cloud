package com.loong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loong.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author loong
 * @since 2024-05-04
 */
public interface UserMapper extends BaseMapper<User> {

    @Insert("insert into user_interest(user_id, interest_id) values(#{id}, #{interest})")
    void addInterest(Long id, Integer interest);

    @Update("update user_interest set interest_id = #{interest} where user_id = #{id}")
    void updateInterest(Long id, Integer interest);

}
