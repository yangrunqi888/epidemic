package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.BuildingSearchDto;
import com.yrq.entity.BuildingEntity;
import com.yrq.entity.LiveEntity;
import com.yrq.utils.QueryPageBean;

import java.util.List;

public interface BuildingService extends IService<BuildingEntity> {
    public IPage getPage(BuildingSearchDto queryPageBean);
    public List<BuildingEntity> list(String id);
}
