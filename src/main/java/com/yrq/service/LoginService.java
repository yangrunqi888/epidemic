package com.yrq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.LoginEntity;
import com.yrq.entity.GuardEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.StaffEntity;

public interface LoginService{
    public ResidentEntity residentLogin(LoginEntity login);
    public StaffEntity staffLogin(LoginEntity login);
    public GuardEntity guardLogin(LoginEntity login);
}
