package com.yrq.config;

import com.yrq.filter.LoginCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 将 LoginCheckFilter 注册为一个 Spring 管理的 Bean
    @Bean
    public LoginCheckFilter loginCheckFilter() {
        return new LoginCheckFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF，因为我们使用的是 JWT 认证，不是基于 Session 的认证机制
                .csrf().disable()
                // 设置为无状态的会话管理，避免使用 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 配置不需要认证的路径（比如登录、测试路径）
                .authorizeRequests()
                .antMatchers("/login/**", "/test/**", "/ws/**").permitAll()  // 这些路径允许不登录访问
                .anyRequest().authenticated()  // 其他请求必须经过认证
                .and()
                // 添加自定义的 JWT 验证过滤器，确保在用户名密码过滤器之前运行
                .addFilterBefore(loginCheckFilter(), UsernamePasswordAuthenticationFilter.class); // 调用 loginCheckFilter() 方法
    }

    // 配置密码编码器，使用 BCrypt 实现
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}