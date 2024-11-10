package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.BackHomeSearchDto;
import com.yrq.entity.BackHomeEntity;

/**
 * @author:YangRunqi
 * @create: 2023-02-23 10:19
 * @Description:
 */
public interface BackHomeService extends IService<BackHomeEntity> {
    public IPage getlist(BackHomeSearchDto backHomeSearchDto);
    public boolean verify(BackHomeEntity back);
    public boolean remove(BackHomeEntity entity);
}
