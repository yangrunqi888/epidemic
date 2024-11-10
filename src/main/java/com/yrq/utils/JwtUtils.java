//package com.yrq.utils;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.yrq.entity.GuardEntity;
//import com.yrq.entity.ResidentEntity;
//import com.yrq.entity.StaffEntity;
//import com.yrq.service.GuardService;
//import com.yrq.service.ResidentService;
//import com.yrq.service.StaffService;
//import io.jsonwebtoken.*;
//import org.springframework.scheduling.support.SimpleTriggerContext;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JwtUtils {
//
//    @Resource
//    private ResidentService residentService;
//    @Resource
//    private StaffService staffService;
//    @Resource
//    private GuardService guardService;
//    /**
//     * 过期时间12小时
//     */
//    private static final long EXPIRE_TIME=12*60*60*1000;
//    /**
//     * 加密密钥
//     */
//    private static final String KEY = "yangrunqi";
//
//    /**
//     * 生成token
//     * @param id    用户id
//     * @return
//     */
//    public String createToken(String id,String right){
//        Map<String,Object> header = new HashMap();
//        //指定了其Token类型和所使用的加密算法
//        header.put("typ","JWT");
//        header.put("alg","HS256");
//        //setID:用户ID
//        //setExpiration:token过期时间  当前时间+有效时间
//        //setSubject:用户名
//        //setIssuedAt:token创建时间
//        //signWith:加密方式
//        JwtBuilder builder = Jwts.builder().setHeader(header)
//                .setId(id).claim("right",right)
//                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_TIME))
//                .setIssuedAt(new Date())
//                .signWith(SignatureAlgorithm.HS256,KEY);
//        return builder.compact();
//    }
//
//    /**
//     * 验证token是否有效
//     * @param token  请求头中携带的token
//     * @return  token验证结果  2-token过期；1-token认证通过；0-token认证失败
//     */
//    public int verify(String token){
//        Claims claims = null;
//        String id;
//        String right;
//        try {
//            //token过期后，会抛出ExpiredJwtException 异常，通过这个来判定token过期，
//            claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
//            id = claims.getId();
//            right= claims.get("right").toString();
//        }catch (Exception e){
//            return 2;
//        }
//        //从token中获取用户id，查询该Id的用户是否存在，存在则token验证通过
//
//        Object entity=null;
//        if(right.equals("0")||right.equals("1")||right.equals("2")||right.equals("3")){
//             entity = staffService.getById(id);
//        }//社区工作人员
//        else if(right.equals("4")||right.equals("5")){
//             entity = guardService.getById(id);
//        }//保安
//        else if(right.equals("6")){
//             entity = residentService.getById(id);
//        }//居民
//        else{
//            return 0;
//        }
//        if(entity != null){
//            return 1;
//        }else{
//            return 0;
//        }
//
//    }
//}
//
package com.yrq.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yrq.entity.GuardEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.service.GuardService;
import com.yrq.service.ResidentService;
import com.yrq.service.StaffService;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class JwtUtils {

    @Resource
    private ResidentService residentService;
    @Resource
    private StaffService staffService;
    @Resource
    private GuardService guardService;

    /**
     * 过期时间12小时
     */
    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000;

    /**
     * 加密密钥
     */
    private static final String KEY = "yangrunqi";

    /**
     * 生成token
     *
     * @param id    用户id
     * @param right 用户权限
     * @return token 字符串
     */
    public String createToken(String id, String right) {
        Map<String, Object> header = new HashMap<>();
        //指定了其Token类型和所使用的加密算法
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //setID:用户ID
        //setExpiration:token过期时间  当前时间+有效时间
        //setSubject:用户名
        //setIssuedAt:token创建时间
        //signWith:加密方式
        JwtBuilder builder = Jwts.builder().setHeader(header)
                .setId(id).claim("right", right)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, KEY);
        return builder.compact();
    }

    /**
     * 验证token是否有效
     *
     * @param token 请求头中携带的token
     * @return token验证结果 2-token过期；1-token认证通过；0-token认证失败
     */
    public int verify(String token) {
        try {
            //token过期后，会抛出ExpiredJwtException 异常，通过这个来判定token过期，
            Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            String id = claims.getId();
            String right = claims.get("right").toString();

            // 从token中获取用户id，查询该Id的用户是否存在，存在则token验证通过
            Object entity = null;
            if (right.equals("0") || right.equals("1") || right.equals("2") || right.equals("3")) {
                entity = staffService.getById(id);
            } else if (right.equals("4") || right.equals("5")) {
                entity = guardService.getById(id);
            } else if (right.equals("6")) {
                entity = residentService.getById(id);
            } else {
                return 0;  // 用户不存在
            }

            if (entity != null) {
                return 1;  // 验证通过
            } else {
                return 0;  // 用户不存在
            }
        } catch (ExpiredJwtException e) {
            return 2;  // token过期
        } catch (Exception e) {
            return 0;  // 其他验证失败
        }
    }

    /**
     * 根据token获取Authentication对象，用于Spring Security的认证
     *
     * @param token JWT token
     * @return Authentication 对象
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        String id = claims.getId();
        String right = claims.get("right").toString();

        // 获取用户的权限信息
        List<GrantedAuthority> authorities = getAuthoritiesByRight(right);

        // 使用用户ID作为用户名
        return new UsernamePasswordAuthenticationToken(id, null, authorities);
    }

    /**
     * 根据用户权限信息构建用户的角色/权限集合
     *
     * @param right 权限字符串
     * @return 权限集合
     */
    private List<GrantedAuthority> getAuthoritiesByRight(String right) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (right) {
            case "0":
            case "1":
            case "2":
            case "3":
                authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
                break;
            case "4":
            case "5":
                authorities.add(new SimpleGrantedAuthority("ROLE_GUARD"));
                break;
            case "6":
                authorities.add(new SimpleGrantedAuthority("ROLE_RESIDENT"));
                break;
            default:
                authorities.add(new SimpleGrantedAuthority("ROLE_UNKNOWN"));
        }
        return authorities;
    }
}
