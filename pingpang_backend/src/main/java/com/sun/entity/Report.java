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
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long authorId;

    private Long articleId;

    private Long sourceId;

    private String desc;

    private LocalDateTime reportTime;

    private Integer category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Report{" +
            "id = " + id +
            ", authorId = " + authorId +
            ", articleId = " + articleId +
            ", sourceId = " + sourceId +
            ", desc = " + desc +
            ", reportTime = " + reportTime +
            ", category = " + category +
        "}";
    }
}
