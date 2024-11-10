package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.entity.GridManageEntity;
import com.yrq.entity.LiveEntity;
import com.yrq.mapper.GridManageMapper;
import com.yrq.mapper.GridMapper;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.GridManageService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GridManageServiceImpl extends ServiceImpl<GridManageMapper, GridManageEntity> implements GridManageService {
    @Resource
    GridManageMapper gridManageMapper;
    @Resource
    StaffMapper staffMapper;
    @Resource
    GridMapper gridMapper;
    @Override
    public List<GridManageEntity> selectByGrid(String gridNumber){
        LambdaQueryWrapper<GridManageEntity> qw=new QueryWrapper<GridManageEntity>().lambda();
        qw.eq(GridManageEntity::getGridNumber,gridNumber);
        List<GridManageEntity> list = gridManageMapper.selectList(qw);
        return list;
    }

    @Override
    public List<String> selectByEmployee(String employeeNumber){
        String position = staffMapper.selectById(employeeNumber).getPosition();
        List<String> grids=new ArrayList<>();
        if(position.equals("1")||position.equals("2")){
            LambdaQueryWrapper<GridManageEntity> qw=new QueryWrapper<GridManageEntity>().lambda();
            qw.eq(GridManageEntity::getEmployeeNumber,employeeNumber);
            gridManageMapper.selectList(qw).forEach(m->grids.add(m.getGridNumber()));
            return grids;
        }
        else{
            gridMapper.selectList(null).forEach(m->grids.add(m.getGridNumber()));
            return grids;
        }
    }

    @Override
    public boolean delete(GridManageEntity gridManageEntity){
        LambdaQueryWrapper<GridManageEntity> qw=new QueryWrapper<GridManageEntity>().lambda();
        qw.eq(GridManageEntity::getEmployeeNumber,gridManageEntity.getEmployeeNumber()).eq(GridManageEntity::getGridNumber,gridManageEntity.getGridNumber());
        int delete = gridManageMapper.delete(qw);
        return delete>0?true:false;
    }
}
