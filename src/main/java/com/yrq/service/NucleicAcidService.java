package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.NucleicAcidDto;
import com.yrq.entity.NucleicAcidEntity;
import com.yrq.entity.ResidentEntity;

import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-21 17:37
 * @Description:
 */
public interface NucleicAcidService extends IService<NucleicAcidEntity> {
    public IPage getlist(NucleicAcidDto nucleicAcidDto);
    public boolean remove(NucleicAcidEntity entity);
    public boolean update(NucleicAcidEntity entity);
}
