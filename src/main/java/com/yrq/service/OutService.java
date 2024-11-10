package com.yrq.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.OutSearchDto;
import com.yrq.entity.OutEntity;
import com.yrq.utils.QueryPageBean;

public interface OutService extends IService<OutEntity> {
    public IPage getPage(OutSearchDto outSearchDto);
    public boolean update(OutEntity out);
    public boolean delete(OutEntity out);
}
