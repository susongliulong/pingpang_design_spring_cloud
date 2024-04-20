package com.loong.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
@TableName("sign_up")
@Data
@NoArgsConstructor
public class SignUp implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long matchId;

    /**
     * 报名时间
     */
    private LocalDateTime signUpTime;

    /**
     * 1 是已报名；2 以参赛
     */
    private Integer state;

    /**
     * 成绩排名
     */
    @TableField(value="rankness")
    private Integer rankness;


}
