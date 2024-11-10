package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.GridSearchDto;
import com.yrq.entity.*;
import com.yrq.mapper.BuildingMapper;
import com.yrq.mapper.GridManageMapper;
import com.yrq.mapper.GridMapper;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.GridService;
import com.yrq.utils.ConvertItem;
import com.yrq.utils.QueryPageBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GridServiceImpl extends ServiceImpl<GridMapper, GridEntity> implements GridService {
    @Resource
    GridMapper gridMapper;
    @Resource
    StaffMapper staffMapper;
    @Resource
    GridManageMapper gridManageMapper;
    @Resource
    BuildingMapper buildingMapper;

    public IPage getPage(GridSearchDto gridSearchDto){
        Integer currentPage=gridSearchDto.getPagination().getCurrentPage();
        Integer pageSize=gridSearchDto.getPagination().getPageSize();
        Page<GridEntity> GridEntityIPage = new Page<>(currentPage,pageSize);

        LambdaQueryWrapper<GridEntity> qw=new LambdaQueryWrapper<>();
        if(!gridSearchDto.getGridNumber().equals("")&&gridSearchDto.getGridNumber()!=null)
            qw.like(GridEntity::getGridNumber,gridSearchDto.getGridNumber());
        if(!gridSearchDto.getLeadership().equals("")&&gridSearchDto.getLeadership() != null)
            qw.like(GridEntity::getLeadership,gridSearchDto.getLeadership());
        if(!gridSearchDto.getLeadershipName().equals("")&&gridSearchDto.getLeadershipName() != null){
            List<String> staffNumbers=new ArrayList<>();
            staffMapper.selectList(new LambdaQueryWrapper< StaffEntity >().like(StaffEntity::getName,gridSearchDto.getLeadershipName())).forEach(
                    item->{
                        staffNumbers.add(item.getEmployeeNumber());
                    }
            );
            if(staffNumbers.size() == 0)
                return new Page();
            qw.in(GridEntity::getLeadership,staffNumbers);
        }
        if(gridSearchDto.getStaffs().size()>0){
            List<String> gridNumbers = new ArrayList<>();
            gridSearchDto.getStaffs().forEach(item->{
                gridManageMapper.selectList(new LambdaQueryWrapper<GridManageEntity>().eq(GridManageEntity::getEmployeeNumber,item)).forEach(
                        item2->gridNumbers.add(item2.getGridNumber())
                );
            });
            if(gridNumbers.size() == 0)
                return new Page();
            qw.in(GridEntity::getGridNumber,gridNumbers);
        }

        IPage iPage = gridMapper.selectPage(GridEntityIPage, qw);
        setOthers((List<GridEntity>) iPage.getRecords());
        return iPage;
    }
    private void setOthers(List<GridEntity> entities){
        entities.forEach(entity->{
            if(entity.getLeadership()!=null)
                entity.setLeadershipName(staffMapper.selectByIdWithOutLogic(entity.getLeadership()).getName());
            gridManageMapper.selectList(new LambdaQueryWrapper<GridManageEntity>().eq(GridManageEntity::getGridNumber, entity.getGridNumber())).forEach(
                    item->{
                       entity.getStaffs().add(staffMapper.selectByIdWithOutLogic( item.getEmployeeNumber()));
                    }
            );

        });
    }

    @Override
    @Transactional
    public boolean update(GridEntity entity) {
        LambdaQueryWrapper<GridManageEntity> qw=new LambdaQueryWrapper<>();
        qw.eq(GridManageEntity::getGridNumber,entity.getGridNumber());
        gridManageMapper.delete(qw);
        for (int i = 0; i < entity.getStaffNumbers().size(); i++) {
            GridManageEntity gridManageEntity=new GridManageEntity();
            gridManageEntity.setGridNumber(entity.getGridNumber());
            gridManageEntity.setEmployeeNumber(entity.getStaffNumbers().get(i));
            gridManageMapper.insert(gridManageEntity);
        }
        if(!entity.getStaffNumbers().contains(entity.getLeadership())){
            entity.setLeadership(null);
        }
        return updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        if(buildingMapper.selectCount(new LambdaQueryWrapper<BuildingEntity>().eq(BuildingEntity::getGridNumber,id))>0){
            try {
                throw new SQLIntegrityConstraintViolationException("fk_live_grid_1");
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new RuntimeException(e);
            }
        }
        return super.removeById(id);
    }
}
