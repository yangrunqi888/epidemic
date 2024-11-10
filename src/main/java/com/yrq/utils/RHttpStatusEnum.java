package com.yrq.utils;



public enum RHttpStatusEnum {

    SUCCESS(200,"success"),





    TOKEN_EMPTY(300,"token缺失"),
    TOKEN_LATE(301,"token已过期"),
    TOKEN_OTHER(302,"用户信息验证失败"),


    HTTP_NOT_FOUND(404,"未找到相关内容"),
    INSERT_FAILED(414,"插入失败"),
    DELETE_FAILED(424,"删除失败"),
    UPDATE_FAILED(434,"修改失败"),

    SERVER_ERROR( 500, "服务器忙，请稍后在试"),
    UNKNOWN_ERROR(600,"哎哟，发生了个神秘错误呀");
    private final int code;
    private final String message;
    RHttpStatusEnum(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}


