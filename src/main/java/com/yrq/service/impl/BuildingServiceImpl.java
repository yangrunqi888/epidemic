package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.core.injector.methods.SelectOne;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.Select;
import com.yrq.dto.BuildingSearchDto;
import com.yrq.entity.*;
import com.yrq.mapper.*;
import com.yrq.service.BuildingService;
import com.yrq.utils.ConvertItem;
import com.yrq.utils.QueryPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, BuildingEntity> implements BuildingService {
    @Resource
    ResidentMapper residentMapper;
    @Resource
    BuildingMapper buildingMapper;
    @Resource
    StaffMapper staffMapper;
    @Resource
    LiveMapper liveMapper;
    @Resource
    GridManageMapper gridManageMapper;
    public IPage getPage(BuildingSearchDto queryPageBean){
        //设置分页
        Integer currentPage=queryPageBean.getPagination().getCurrentPage();
        Integer pageSize=queryPageBean.getPagination().getPageSize();
        Page<BuildingEntity> buildingEntityPage = new Page<>(currentPage,pageSize);
        IPage iPage=null;
        List<String> gridNumbers = getGridNumber(queryPageBean.getPagination().getQueryString().toString());
        //根据网格号查询对于楼栋号
        if(gridNumbers!=null){
            //分页查找网格内所有楼栋号
            if(gridNumbers.size() ==0)
                return new Page();
            LambdaQueryWrapper<BuildingEntity> qw = new LambdaQueryWrapper<>();
            qw.in(BuildingEntity::getGridNumber,gridNumbers);
            if(queryPageBean.getBuildingNumber()!="")
                qw.eq(BuildingEntity::getBuildingNumber,queryPageBean.getBuildingNumber());
            if(queryPageBean.getGrid().size()>0)
                qw.in(BuildingEntity::getGridNumber,queryPageBean.getGrid());
            iPage = buildingMapper.selectPage(buildingEntityPage, qw);
        }
        //如果是片长或管理员，查询所有用户信息
        else{
            LambdaQueryWrapper<BuildingEntity> qw = new LambdaQueryWrapper<>();
            if(queryPageBean.getBuildingNumber()!="")
                qw.eq(BuildingEntity::getBuildingNumber,queryPageBean.getBuildingNumber());
            if(queryPageBean.getGrid().size()>0)
                qw.in(BuildingEntity::getGridNumber,queryPageBean.getGrid());
            iPage = buildingMapper.selectPage(buildingEntityPage, qw);
        }

        iPage.getRecords().forEach(building -> setOthers((BuildingEntity) building));
        return iPage;
    }


    public List<BuildingEntity> list(String id) {
        List<String> gridNumbers = getGridNumber(id);
        List<BuildingEntity> buildings=null;
        if(gridNumbers!=null){
            //分页查找网格内所有楼栋号
            if(gridNumbers.size() == 0)
                return new ArrayList<>();
            LambdaQueryWrapper<BuildingEntity> qw = new LambdaQueryWrapper<>();
            qw.in(BuildingEntity::getGridNumber,gridNumbers);
            buildings = buildingMapper.selectList(qw);
        }
        //如果是片长或管理员，查询所有楼栋号
        else{
            buildings = super.list();
        }
        if(buildings!=null){
            buildings.forEach(building-> setOthers(building));
        }
        return buildings;

    }

    @Override
    public BuildingEntity getById(Serializable id) {
        BuildingEntity building = super.getById(id);
        if(building!=null){
            setOthers(building);
        }
        return building;
    }

    @Override
    public boolean save(BuildingEntity entity) {
        if(buildingMapper.selectByIdWithOutLogic(entity.getBuildingNumber())!=null){
            buildingMapper.deleteByIdWithOutLogic(entity.getBuildingNumber());
        }
        return super.save(entity);
    }

    public void setOthers(BuildingEntity building){
        //设置楼栋的总居住人数
        LambdaQueryWrapper<LiveEntity> qw=new LambdaQueryWrapper<>();
        qw.eq(LiveEntity::getBuildingNumber,building.getBuildingNumber());
        building.setTotalPerson(liveMapper.selectCount(qw));
        //设置楼栋长姓名和房间号
        if(building.getHeaderId()!=null){
            //查询楼栋长姓名
            LambdaQueryWrapper<ResidentEntity> qw1=new LambdaQueryWrapper<>();
            qw1.eq(ResidentEntity::getIdNumber,building.getHeaderId());
            building.setHeaderName(residentMapper.selectOne(qw1).getName());
            //查询楼栋长房间号
            LambdaQueryWrapper<LiveEntity> qw2=new LambdaQueryWrapper<>();
            qw2.eq(LiveEntity::getIdNumber,building.getHeaderId());
            building.setHeaderRoom(liveMapper.selectOne(qw2).getRoomNumber());
        }
    }

    @Override
    public boolean removeById(Serializable id){
        LambdaQueryWrapper<LiveEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(LiveEntity::getBuildingNumber,id);
        if(liveMapper.selectCount(qw)>0)
            try {
                throw new SQLIntegrityConstraintViolationException("fk_live_building_1");
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new RuntimeException(e);
            }
        return super.removeById(id);
    }

    public List<String> getGridNumber(String queryString){
        //查询管理人员职位
        LambdaQueryWrapper<StaffEntity> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(StaffEntity::getEmployeeNumber,queryString);
        Integer position=Integer.parseInt(staffMapper.selectOne(qw1).getPosition());
        //如果是网格员（长）查询所管理区域的楼栋信息
        if(position==1||position==2){
            //查找网格员管理的所有网格
            List<String> gridNumbers = new ArrayList<>();
            LambdaQueryWrapper<GridManageEntity> qw2 = new LambdaQueryWrapper<>();
            qw2.eq(GridManageEntity::getEmployeeNumber,queryString);
            gridManageMapper.selectList(qw2).forEach(manage->gridNumbers.add(manage.getGridNumber()));
            return gridNumbers;
        }
        //如果是片长或管理员，查询所有用户信息
        else{
            return null;
        }
    }

}
