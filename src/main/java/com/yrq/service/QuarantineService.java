package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.AntigenSearchDto;
import com.yrq.dto.QuarantineDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.QuarantineEntity;

/**
 * @author:YangRunqi
 * @create: 2023-02-24 12:37
 * @Description:
 */
public interface QuarantineService extends IService<QuarantineEntity> {
    public IPage getlist(QuarantineDto quarantineDto);
    public boolean remove(QuarantineEntity entity);
}
