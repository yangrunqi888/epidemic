package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.StaffSearchDto;
import com.yrq.entity.StaffEntity;
import com.yrq.utils.QueryPageBean;

public interface StaffService extends IService<StaffEntity> {
    public IPage getPage(StaffSearchDto staffSearchDto);
    public boolean resetPassword(String ID);
}
