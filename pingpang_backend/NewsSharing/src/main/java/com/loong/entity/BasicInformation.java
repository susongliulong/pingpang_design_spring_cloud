package com.loong.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author loong
 * @since 2024-04-09
 */
@TableName("basic_information")
public class BasicInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long authorId;

    private Long id;

    private String title;

    private String coverImage;

    private LocalDateTime publishTime;

    private String abstract;

    private Integer likes;

    private Integer comments;

    private Integer collects;

    private Integer pageView;

    private Integer state;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public String getAbstract() {
        return abstract;
    }

    public void setAbstract(String abstract) {
        this.abstract = abstract;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getCollects() {
        return collects;
    }

    public void setCollects(Integer collects) {
        this.collects = collects;
    }

    public Integer getPageView() {
        return pageView;
    }

    public void setPageView(Integer pageView) {
        this.pageView = pageView;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "BasicInformation{" +
            "authorId = " + authorId +
            ", id = " + id +
            ", title = " + title +
            ", coverImage = " + coverImage +
            ", publishTime = " + publishTime +
            ", abstract = " + abstract +
            ", likes = " + likes +
            ", comments = " + comments +
            ", collects = " + collects +
            ", pageView = " + pageView +
            ", state = " + state +
        "}";
    }
}
