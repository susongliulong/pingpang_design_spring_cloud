package com.loong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author loong
 * @since 2024-04-21
 */
@TableName("basic_information")
public class BasicInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long authorId;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String coverImage;

    private LocalDateTime publishTime;

    private String mainContent;

    private Integer likes;

    private Integer comments;

    private Integer collects;

    private Integer pageView;

    private Integer state;

    private Long categoryId;

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

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "BasicInformation{" +
            "authorId = " + authorId +
            ", id = " + id +
            ", title = " + title +
            ", coverImage = " + coverImage +
            ", publishTime = " + publishTime +
            ", mainContent = " + mainContent +
            ", likes = " + likes +
            ", comments = " + comments +
            ", collects = " + collects +
            ", pageView = " + pageView +
            ", state = " + state +
            ", categoryId = " + categoryId +
        "}";
    }
}
