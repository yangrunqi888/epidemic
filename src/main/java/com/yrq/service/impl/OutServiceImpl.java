package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.OutSearchDto;
import com.yrq.entity.*;
import com.yrq.mapper.GuardMapper;
import com.yrq.mapper.OutMapper;
import com.yrq.service.OutService;
import com.yrq.service.ResidentService;
import com.yrq.utils.ConvertItem;
import com.yrq.utils.QueryPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OutServiceImpl extends ServiceImpl<OutMapper, OutEntity> implements OutService {
    @Resource
    OutMapper outMapper;
    @Resource
    ResidentService residentService;
    @Resource
    GuardMapper guardMapper;

    public IPage getPage(OutSearchDto outSearchDto){
        Integer currentPage=outSearchDto.getPagination().getCurrentPage();
        Integer pageSize=outSearchDto.getPagination().getPageSize();
        Page<OutEntity> outEntityIPage = new Page<>(currentPage,pageSize);
        IPage iPage=null;
        LambdaQueryWrapper<OutEntity> qw=new LambdaQueryWrapper<>();
        if(outSearchDto.getIdNumber()!=null&&!outSearchDto.getIdNumber().equals("")){
            qw.like(OutEntity::getIdNumber,outSearchDto.getIdNumber());
        }
        if(outSearchDto.getName()!=null&&!outSearchDto.getName().equals("")){
            List<String> residentNumbers2=new ArrayList<>();
            residentService.list(new LambdaQueryWrapper<ResidentEntity>().like(ResidentEntity::getName,outSearchDto.getName())).forEach(
                    resident -> residentNumbers2.add(resident.getIdNumber()));
            qw.in(OutEntity::getIdNumber,residentNumbers2);
        }
        if (outSearchDto.getOutTime()!=null&&outSearchDto.getOutTime().size() > 0){
            qw.between(OutEntity::getOutTime,outSearchDto.getOutTime().get(0),outSearchDto.getOutTime().get(1));
        }
        if (outSearchDto.getBackTime()!=null&&outSearchDto.getBackTime().size() > 0){
            qw.between(OutEntity::getBackTime,outSearchDto.getBackTime().get(0),outSearchDto.getBackTime().get(1));
        }
        if(outSearchDto.getGuardNumber()!=null&&!outSearchDto.getGuardNumber().equals("")){
            if(outSearchDto.getGuardNumber().equals("admin")){
                qw.and(qw2->qw2.isNull(OutEntity::getOutGuardNumber).or(qw3->qw3.isNull(OutEntity::getBackGuardNumber).eq(OutEntity::getState,"1")));
            }
            else {
                qw.and(qw2 -> qw2.like(OutEntity::getOutGuardNumber, outSearchDto.getGuardNumber()).or()
                        .like(OutEntity::getBackGuardNumber, outSearchDto.getGuardNumber()));
            }
        }
        if(outSearchDto.getGuardName()!=null&&!outSearchDto.getGuardName().equals("")){
            if(outSearchDto.getGuardName().equals("admin")){
                qw.and(qw2->qw2.isNull(OutEntity::getOutGuardNumber).or(qw3->qw3.isNull(OutEntity::getBackGuardNumber).eq(OutEntity::getState,"1")));
            }
            else {
                List<String> guardNumbers = new ArrayList<>();
                guardMapper.selectList(new LambdaQueryWrapper<GuardEntity>().like(GuardEntity::getName, outSearchDto.getGuardName())).forEach(
                        guard -> guardNumbers.add(guard.getGuardNumber())
                );
                qw.and(qw2 -> qw2.in(OutEntity::getOutGuardNumber, guardNumbers).or()
                        .in(OutEntity::getBackGuardNumber, guardNumbers));
            }
        }
        if(outSearchDto.getOutTotalTime()!=null&&(outSearchDto.getOutTotalTime().get(0)!=0||outSearchDto.getOutTotalTime().get(1)!=999)){
            if(outSearchDto.getOutTotalTime().get(1)==999){
                qw.ge(OutEntity::getOutTotalTime,outSearchDto.getOutTotalTime().get(0));
            }
            else{
                qw.between(OutEntity::getOutTotalTime,outSearchDto.getOutTotalTime().get(0),outSearchDto.getOutTotalTime().get(1));
            }
        }
        if(outSearchDto.getState()!=null&&!outSearchDto.getState().equals("")){
            qw.eq(OutEntity::getState,outSearchDto.getState());
        }
        qw.orderByAsc(OutEntity::getState);
        if(outSearchDto.getPagination().getQueryString()!=null&&!outSearchDto.getPagination().getQueryString().toString().equals("")){
            List<String> residentNumbers=new ArrayList<>();
            residentService.getlist(outSearchDto.getPagination().getQueryString().toString()).forEach(resident -> residentNumbers.add(resident.getIdNumber()));
            qw.in(OutEntity::getIdNumber,residentNumbers);
        }
        iPage = outMapper.selectPage(outEntityIPage,qw);
        setOthers((List<OutEntity>) iPage.getRecords());
        return iPage;
    }
    public boolean update(OutEntity out){
        LambdaQueryWrapper<OutEntity> qw=new QueryWrapper<OutEntity>().lambda();
        qw.eq(OutEntity::getIdNumber,out.getIdNumber());
        qw.eq(OutEntity::getOutTime,out.getOutTime());
        return outMapper.update(out,qw)>0;
    }
    public boolean delete(OutEntity out){
        LambdaQueryWrapper<OutEntity> qw=new QueryWrapper<OutEntity>().lambda();
        qw.eq(OutEntity::getIdNumber,out.getIdNumber());
        qw.eq(OutEntity::getOutTime,out.getOutTime());
        return outMapper.delete(qw)>0;
    }
    private void setOthers(List<OutEntity> entities){
        entities.forEach(entity->{
           entity.setName(residentService.getById(entity.getIdNumber()).getName());
           if(entity.getOutGuardNumber()!=null&&entity.getOutGuardNumber().equals("")){
               entity.setOutGuardName(guardMapper.selectByIdWithOutLogic(entity.getOutGuardNumber()).getName());
           }
           else{
               entity.setOutGuardNumber("admin");
               entity.setOutGuardName("admin");
           }
           if(entity.getState().equals("0")){
               entity.setState("外出中");
           }
           else{
               entity.setState("已返回");
               if(entity.getBackGuardNumber()!=null){
                   entity.setBackGuardName(guardMapper.selectByIdWithOutLogic(entity.getBackGuardNumber()).getName());
               }
               else{
                   entity.setBackGuardNumber("admin");
                   entity.setBackGuardName("admin");
               }
               entity.setOutTotalTime(Double.parseDouble(String.format("%.2f", entity.getOutTotalTime())));
           }
        });
    }
}
