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
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long matchId;

    private LocalDateTime time;

    private String address;

    private Integer playNumer;

    private String desc;

    private Integer minPoints;

    private Integer awards;

    private String contact;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPlayNumer() {
        return playNumer;
    }

    public void setPlayNumer(Integer playNumer) {
        this.playNumer = playNumer;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(Integer minPoints) {
        this.minPoints = minPoints;
    }

    public Integer getAwards() {
        return awards;
    }

    public void setAwards(Integer awards) {
        this.awards = awards;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Match{" +
            "matchId = " + matchId +
            ", time = " + time +
            ", address = " + address +
            ", playNumer = " + playNumer +
            ", desc = " + desc +
            ", minPoints = " + minPoints +
            ", awards = " + awards +
            ", contact = " + contact +
        "}";
    }
}
