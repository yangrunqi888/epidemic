package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.AntigenSearchDto;
import com.yrq.dto.TemperatureSearchDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.TemperatureEntity;

/**
 * @author:YangRunqi
 * @create: 2023-02-22 15:47
 * @Description:
 */
public interface TemperatureService extends IService<TemperatureEntity> {
    public IPage getlist(TemperatureSearchDto temperatureSearchDto);
    public boolean remove(TemperatureEntity entity);
}
