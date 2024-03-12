package com.sun.common.state;

/**
 * 和相册处理相关的提示信息
 */
public enum RofAlbum {
    ALBUM_ADD_SUCCESS("相册创建成功"),
    ALBUM_DELETE_SUCCESS("相册删除成功"),
    ALBUM_UPDATE_SUCCESS("相册更新成功"),
    ALBUM_EMPTY("空相册");

    String message;


    RofAlbum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
