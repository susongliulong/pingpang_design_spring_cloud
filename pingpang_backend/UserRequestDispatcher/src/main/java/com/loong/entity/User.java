package com.loong.entity;

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

    private Long id;

    private String name;

    private String telephone;

    private String email;

    private String description;

    private String password;

    private String avatar;

    private Integer state;

    private LocalDateTime lastLoginTime;

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
