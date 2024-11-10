package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.VisitSearchDto;
import com.yrq.entity.VisitEntity;
import com.yrq.utils.QueryPageBean;

public interface VisitService extends IService<VisitEntity> {
    public IPage getPage(VisitSearchDto visitSearchDto);
    public boolean update(VisitEntity entity);
    public boolean remove(VisitEntity entity);
    public String getVisitRecords(String id);
}
