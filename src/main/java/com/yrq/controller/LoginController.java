package com.yrq.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.net.httpserver.HttpServer;
import com.yrq.dto.LoginEntity;
import com.yrq.dto.MenuDto;
import com.yrq.entity.GuardEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.mapper.ResidentMapper;
import com.yrq.service.LoginService;
import com.yrq.service.MenuService;
import com.yrq.service.ResidentService;
import com.yrq.utils.JwtUtils;
import com.yrq.utils.LoginR;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    JwtUtils jwtUtils;
    @Resource
    LoginService loginService;
    @Resource
    MenuService menuService;
    @PostMapping("/residentLogin")
    public R residentLogin(@RequestBody LoginEntity login){
        ResidentEntity resident = loginService.residentLogin(login);
        if(resident==null){
            return R.error(RHttpStatusEnum.HTTP_NOT_FOUND,"登录失败");
        }
        List<MenuDto> menus = menuService.getMenus("6");
        String token = jwtUtils.createToken(resident.getIdNumber(),"6");
        return LoginR.ok(resident,"登录成功！",token,menus);
    }
    @PostMapping("/staffLogin")
    public R staffLogin(@RequestBody LoginEntity login){
        StaffEntity staff = loginService.staffLogin(login);
        if(staff==null){
            return R.error(RHttpStatusEnum.HTTP_NOT_FOUND,"登录失败");
        }
        List<MenuDto> menus = menuService.getMenus(staff.getPosition());
        String token = jwtUtils.createToken(staff.getEmployeeNumber(),staff.getPosition());
        LoginR ok = LoginR.ok(staff, "登录成功！", token, menus);
        return ok;
    }
    @PostMapping("/guardLogin")
    public R guardLogin(@RequestBody LoginEntity login){
        GuardEntity guard = loginService.guardLogin(login);
        if(guard==null){
            return R.error(RHttpStatusEnum.HTTP_NOT_FOUND,"登录失败");
        }
        List<MenuDto> menus = menuService.getMenus(guard.getPosition());
        String token = jwtUtils.createToken(guard.getGuardNumber(),guard.getPosition());
        LoginR ok = LoginR.ok(guard, "登录成功！", token, menus);
        return ok;
    }
}
