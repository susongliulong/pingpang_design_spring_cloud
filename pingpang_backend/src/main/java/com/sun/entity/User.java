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

    private String avator;

    private Integer state;

    private LocalDateTime llastLoginTime;

    private Integer playYears;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public LocalDateTime getLlastLoginTime() {
        return llastLoginTime;
    }

    public void setLlastLoginTime(LocalDateTime llastLoginTime) {
        this.llastLoginTime = llastLoginTime;
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
            ", avator = " + avator +
            ", state = " + state +
            ", llastLoginTime = " + llastLoginTime +
            ", playYears = " + playYears +
        "}";
    }
}
