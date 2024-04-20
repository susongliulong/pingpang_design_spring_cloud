package com.loong.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
@TableName("user_ability")
@Data
@NoArgsConstructor
public class UserAbility implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    /**
     * 发球能力
     */
    private Integer serve;

    /**
     * 接球能力
     */
    private Integer receive;

    /**
     * 进攻能力
     */
    private Integer assault;

    /**
     * 防守能力
     */
    private Integer defend;

    private Integer score;

}
