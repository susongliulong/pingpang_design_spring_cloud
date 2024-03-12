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
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long commentId;

    private String ipAddress;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "Comment{" +
            "commentId = " + commentId +
            ", ipAddress = " + ipAddress +
        "}";
    }
}
