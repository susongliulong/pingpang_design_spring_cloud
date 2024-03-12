package com.sun.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author sun
 * @since 2024-03-12
 */
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String desc;

    private Integer size;

    private String location;

    private Long articleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "Media{" +
            "id = " + id +
            ", desc = " + desc +
            ", size = " + size +
            ", location = " + location +
            ", articleId = " + articleId +
        "}";
    }
}
