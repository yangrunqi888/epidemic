package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.TemperatureSearchDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.TemperatureEntity;
import com.yrq.mapper.AntigenMapper;
import com.yrq.mapper.TemperatureMapper;
import com.yrq.service.ResidentService;
import com.yrq.service.TemperatureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemperatureServiceImpl extends ServiceImpl<TemperatureMapper,TemperatureEntity> implements TemperatureService {

    @Resource
    ResidentService residentService;
    @Resource
    TemperatureMapper temperatureMapper;
    @Override
    public IPage getlist(TemperatureSearchDto temperatureSearchDto) {
        //设置分页
        Integer currentPage=temperatureSearchDto.getPagination().getCurrentPage();
        Integer pageSize=temperatureSearchDto.getPagination().getPageSize();
        Page<TemperatureEntity> temperatureEntityPage = new Page<>(currentPage,pageSize);
        IPage iPage=null;
        List<String> residentNumbers=new ArrayList<>();
        residentService.getlist(temperatureSearchDto.getPagination().getQueryString().toString()).forEach(resident -> residentNumbers.add(resident.getIdNumber()));
        LambdaQueryWrapper<TemperatureEntity> qw=new LambdaQueryWrapper<>();
        qw.in(TemperatureEntity::getIdNumber,residentNumbers);
        if(temperatureSearchDto.getIdNumber()!=null&&!temperatureSearchDto.getIdNumber().equals(""))
            qw.like(TemperatureEntity::getIdNumber,temperatureSearchDto.getIdNumber());
        if(temperatureSearchDto.getName()!=null&&!temperatureSearchDto.getName().equals("")){
            List<String> residentNumbers2=new ArrayList<>();
            residentService.list(new LambdaQueryWrapper<ResidentEntity>().like(ResidentEntity::getName,temperatureSearchDto.getName())).forEach(
                    resident -> residentNumbers2.add(resident.getIdNumber()));
            if(residentNumbers2.size()==0)
                return new Page();
            qw.in(TemperatureEntity::getIdNumber,residentNumbers2);
        }
        if(temperatureSearchDto.getCollectStartTime()!=null)
            qw.ge(TemperatureEntity::getCollectTime,temperatureSearchDto.getCollectStartTime());
        if(temperatureSearchDto.getCollectEndTime()!=null)
            qw.le(TemperatureEntity::getCollectTime,temperatureSearchDto.getCollectEndTime());
        iPage = temperatureMapper.selectPage(temperatureEntityPage, qw);
        setOthers((List<TemperatureEntity>) iPage.getRecords());
        return iPage;
    }

    @Override
    public boolean remove(TemperatureEntity entity) {
        LambdaQueryWrapper<TemperatureEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(TemperatureEntity::getIdNumber,entity.getIdNumber());
        qw.eq(TemperatureEntity::getCollectTime,entity.getCollectTime());
        return temperatureMapper.delete(qw)>0;
    }

    public void setOthers(List<TemperatureEntity> temperatureEntities){
        temperatureEntities.forEach(entity -> entity.setName(residentService.getById(entity.getIdNumber()).getName()));
    }
}
