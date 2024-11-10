package com.yrq.utils;

import com.yrq.dto.MenuDto;
import jdk.nashorn.internal.parser.Token;

import java.util.List;

public class LoginR extends R{
    private String token;
    private List<MenuDto> menus;

    public static LoginR ok(Object data,String token,List<MenuDto> menus){
        return new LoginR(RHttpStatusEnum.SUCCESS.getCode(),data,RHttpStatusEnum.SUCCESS.getMessage(),token,menus);
    }


    public static LoginR ok(Object data,String message,String token,List<MenuDto> menus){
        return new LoginR(RHttpStatusEnum.SUCCESS.getCode(),data,message,token,menus);
    }




    public LoginR() {

    }

    public LoginR(Integer code, Object data, String message,String token,List<MenuDto> menus) {
        super(code,data,message);
        this.token=token;
        this.menus=menus;
    }

    /**
     * 获取
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }
    public List<MenuDto> getMenus(){
        return menus;
    }
    public void setMenus(List<MenuDto> menus){
        this.menus=menus;
    }

}
