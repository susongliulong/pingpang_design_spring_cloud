package com.loong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.loong.common.annotations.Desensitization;
import com.loong.common.enums.DesensitizationTypeEnum;
import lombok.AllArgsConstructor;
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
 * @since 2024-04-07
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @Desensitization(type = DesensitizationTypeEnum.MOBILE_PHONE)
    private String telephone;

    @Desensitization(type = DesensitizationTypeEnum.EMAIL)
    private String email;

    private String description;

    private String password;

    private String avatar;

    private Integer state;

    private LocalDateTime lastLoginTime;

    private LocalDateTime registerTime;

    private Integer playYears;

    @Override
    public String toString() {
        return "User{" +
            "id = " + id +
            ", avator = " + avatar +
            ", state = " + state +
            ", lastLoginTime = " + lastLoginTime +
            ", playYears = " + playYears +
        "}";
    }
}
