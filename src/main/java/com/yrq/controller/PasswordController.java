package com.yrq.controller;

import com.yrq.dto.ChangePasswordDto;
import com.yrq.entity.BuildingEntity;
import com.yrq.service.PasswordService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author:YangRunqi
 * @create: 2023-02-28 14:53
 * @Description:
 */
@RestController
@RequestMapping("/password")
public class PasswordController {
    @Resource
    PasswordService passwordService;
    @PutMapping("/change")
    public R changePassword(@RequestBody ChangePasswordDto password){
        boolean update = passwordService.changePassword(password);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED,"原密码不正确！");
    }
}
