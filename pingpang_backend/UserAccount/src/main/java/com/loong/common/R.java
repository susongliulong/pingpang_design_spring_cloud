package com.loong.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器端返回给客户端数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    
    private enum RHttpState{
        
        SUCCESS(200,"请求成功"),
        WARNING(300,"警告信息"),
        NOTFOUND(400,"请求资源不存在"),
        ERROR(500,"错误信息");
        int code;
        String message;
        RHttpState(int code, String message) {
            this.code = code;
            this.message = message;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getMessage() {
            return message;
        }
    }

    /**
     *标识返回状态
     */
    private Integer code;

    /**
     * 标识返回内容
     */
    private Object data;
    /**
     * 标识返回消息
     */
    private String message;
    
    public static R success(Object data,String message){
        return new R(RHttpState.SUCCESS.getCode(),data,message);
    }

    public static R success(Object data){
        return new R(RHttpState.SUCCESS.getCode(),data,null);
    }

    public static R success(String  message){
        return new R(RHttpState.SUCCESS.getCode(),null,message);
    }
    
    public static R warn(Object data,String message){
        return new R(RHttpState.WARNING.getCode(),data,message);
    }
    
    
    public static R warn(String message){
        return new R(RHttpState.WARNING.getCode(),null,message);//可以优化
    }
    
    public static R error(Object data,String message){
        return new R(RHttpState.ERROR.getCode(),data,message);
    }

    public static R error(String message){
        return new R(RHttpState.ERROR.getCode(),null,message);
    }
    
}


