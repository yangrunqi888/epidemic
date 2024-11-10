package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.AntigenSearchDto;
import com.yrq.dto.NucleicAcidDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.NucleicAcidEntity;

public interface AntigenService extends IService<AntigenEntity> {
    public IPage getlist(AntigenSearchDto antigenSearchDto);
    public boolean remove(AntigenEntity entity);

}
