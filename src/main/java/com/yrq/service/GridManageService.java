package com.yrq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.entity.GridManageEntity;

import java.util.List;

public interface GridManageService extends IService<GridManageEntity> {
    public List<GridManageEntity> selectByGrid(String gridNumber);
    public List<String> selectByEmployee(String employeeNumber);
    public boolean delete(GridManageEntity gridManageEntity);
}
