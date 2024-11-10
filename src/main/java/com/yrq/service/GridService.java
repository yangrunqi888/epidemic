package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.GridSearchDto;
import com.yrq.entity.GridEntity;
import com.yrq.utils.QueryPageBean;

import java.util.List;

public interface GridService extends IService<GridEntity> {
    public IPage getPage(GridSearchDto gridSearchDto);
    public boolean update(GridEntity entity);
}
