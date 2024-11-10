package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.ResidentSearchDto;
import com.yrq.entity.ResidentEntity;
import com.yrq.utils.QueryPageBean;

import java.util.List;

public interface ResidentService extends IService<ResidentEntity> {
    public IPage getPage(ResidentSearchDto queryPageBean);
    public boolean resetPassword(String ID);
    public List<ResidentEntity> getlist(String id);
}
