package com.yrq.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrq.dto.ChangePasswordDto;

public interface PasswordService {
    public boolean changePassword(ChangePasswordDto passwordDto);
}
