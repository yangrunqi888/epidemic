//package com.yrq.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.yrq.utils.JwtUtils;
//import com.yrq.utils.R;
//import com.yrq.utils.RHttpStatusEnum;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.AntPathMatcher;
//
//import javax.annotation.Resource;
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///*
//* 检查用户是否已经登录
//* */
//@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
//public class LoginCheckFilter implements Filter {
//    @Resource
//    JwtUtils jwtUtils;
//    //路径匹配器
//    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request=(HttpServletRequest) servletRequest;
//        HttpServletResponse response=(HttpServletResponse) servletResponse;
//
//        //请求获取url
//        String requestURI=request.getRequestURI();
//        //以下路径不需要拦截
//        String[] urls=new String[]{"/login/**","/test/**","/ws/**"};
//        //检查路径是否需要拦截
//        if(check(urls,requestURI)){
//            filterChain.doFilter(request,response);
//            return;
//        }
//        else {
//            String token=request.getHeader("token");
//            if(token!=null){
//                int verify= jwtUtils.verify(token);
//                if(verify==1){
//                    // 成功验证 token 后，设置认证信息到 Spring Security 上下文中
//                    SecurityContextHolder.getContext().setAuthentication(jwtUtils.getAuthentication(token));
//                    filterChain.doFilter(request, response);
//                    filterChain.doFilter(request,response);
//                    return;
//                }
//                else if(verify==0){
//                    //用户不存在
//                    ObjectMapper objectMapper=new ObjectMapper();
//                    response.getWriter().write(objectMapper.writeValueAsString(R.error(RHttpStatusEnum.TOKEN_OTHER)));
//                }
//                else{
//                    //token过期
//                    ObjectMapper objectMapper=new ObjectMapper();
//                    response.getWriter().write(objectMapper.writeValueAsString(R.error(RHttpStatusEnum.TOKEN_LATE)));
//                }
//            }
//            else {
//                //缺少token
//                ObjectMapper objectMapper=new ObjectMapper();
//                response.getWriter().write(objectMapper.writeValueAsString(R.error(RHttpStatusEnum.TOKEN_EMPTY)));
//            }
//        }
//
//    }
//    public boolean check(String[] urls,String requestURI){
//        for (int i = 0; i < urls.length; i++) {
//            boolean match=PATH_MATCHER.match(urls[i],requestURI);
//            if(match){
//                return true;
//            }
//        }
//        return false;
//    }
//}
package com.yrq.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrq.utils.JwtUtils;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经登录
 */
//@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Resource
    JwtUtils jwtUtils;

    // 路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取请求的 URL
        String requestURI = request.getRequestURI();

        // 以下路径不需要拦截
        String[] urls = new String[]{"/login/**", "/test/**", "/ws/**"};

        // 检查路径是否需要拦截
        if (check(urls, requestURI)) {
            // 如果不需要拦截，直接放行
            filterChain.doFilter(request, response);
            return;
        }

        // 从请求头获取 token
        String token = request.getHeader("token");

        // 如果请求头中没有 token
        if (token == null) {
            handleErrorResponse(response, RHttpStatusEnum.TOKEN_EMPTY);
            return;
        }

        // 验证 token
        int verify = jwtUtils.verify(token);
        if (verify == 1) {
            // 验证通过，设置认证信息到 Spring Security 上下文中
            SecurityContextHolder.getContext().setAuthentication(jwtUtils.getAuthentication(token));
            // 放行请求
            filterChain.doFilter(request, response);
        } else if (verify == 0) {
            // 用户不存在
            handleErrorResponse(response, RHttpStatusEnum.TOKEN_OTHER);
        } else {
            // token 过期
            handleErrorResponse(response, RHttpStatusEnum.TOKEN_LATE);
        }
    }

    /**
     * 根据请求 URI 检查是否在白名单中
     *
     * @param urls       不需要拦截的路径
     * @param requestURI 请求 URI
     * @return 是否在白名单中
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理错误响应
     *
     * @param response   HttpServletResponse
     * @param statusEnum 响应状态枚举
     * @throws IOException IO 异常
     */
    private void handleErrorResponse(HttpServletResponse response, RHttpStatusEnum statusEnum) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置 HTTP 响应状态码为 401 未授权
        response.setContentType("application/json;charset=UTF-8"); // 设置响应的内容类型
        response.getWriter().write(objectMapper.writeValueAsString(R.error(statusEnum)));
    }
}
