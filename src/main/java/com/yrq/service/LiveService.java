package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.LiveDto;
import com.yrq.entity.LiveEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.utils.QueryPageBean;

import java.util.List;

public interface LiveService extends IService<LiveEntity> {
    List<LiveDto> getResidentByBuilding(String id);
}
