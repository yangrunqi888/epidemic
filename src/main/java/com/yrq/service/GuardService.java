package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.GuardSearchDto;
import com.yrq.dto.StaffSearchDto;
import com.yrq.entity.GuardEntity;
import com.yrq.utils.QueryPageBean;

public interface GuardService extends IService<GuardEntity> {
    public IPage getPage(GuardSearchDto guardSearchDto);
    public boolean resetPassword(String ID);
}
