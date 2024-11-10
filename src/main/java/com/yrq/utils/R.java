package com.yrq.utils;

public class R {

    private Integer code;


    private Object data;

    private String message;



    public static R ok(Object data){
        return new R(RHttpStatusEnum.SUCCESS.getCode(),data,RHttpStatusEnum.SUCCESS.getMessage());
    }


    public static R ok(Object data,String message){
        return new R(RHttpStatusEnum.SUCCESS.getCode(),data,message);
    }
    public static R ok(){
        return new R(RHttpStatusEnum.SUCCESS.getCode(),null,RHttpStatusEnum.SUCCESS.getMessage());
    }

    public static R error(RHttpStatusEnum rHttpStatusEnum){
        return new R(rHttpStatusEnum.getCode(),null,rHttpStatusEnum.getMessage());
    }

    public static R error(Integer code,String message){
        R r = new R();
        r.code(code);
        r.data(null);
        r.message(message);
        return r;
    }
    public static R error(RHttpStatusEnum rHttpStatusEnum,String message){
        R r = new R();
        r.code(rHttpStatusEnum.getCode());
        r.data(null);
        r.message(message);
        return r;
    }
    public static R error(RHttpStatusEnum rHttpStatusEnum,Object data){
        R r = new R();
        r.code(rHttpStatusEnum.getCode());
        r.data(data);
        r.message(rHttpStatusEnum.getMessage());
        return r;
    }

    public R() {

    }

    public R(Integer code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public R code(Integer code) {
        this.code = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public R data(Object data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }
}

