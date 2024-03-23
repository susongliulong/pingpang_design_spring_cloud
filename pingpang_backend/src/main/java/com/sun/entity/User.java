package com.sun.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author sun
 * @since 2024-03-12
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String avatar;

    private Integer state;

    private LocalDateTime lastLoginTime;

    private Integer playYears;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getPlayYears() {
        return playYears;
    }

    public void setPlayYears(Integer playYears) {
        this.playYears = playYears;
    }

    @Override
    public String toString() {
        return "User{" +
            "id = " + id +
            ", avator = " + avatar +
            ", state = " + state +
            ", llastLoginTime = " + lastLoginTime +
            ", playYears = " + playYears +
        "}";
    }
}
