package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.NucleicAcidDto;
import com.yrq.entity.NucleicAcidEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.mapper.NucleicAcidMapper;
import com.yrq.service.NucleicAcidService;
import com.yrq.service.ResidentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.html.parser.Entity;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-21 17:38
 * @Description:
 */
@Service
public class NucleicAcidServiceImpl extends ServiceImpl<NucleicAcidMapper, NucleicAcidEntity> implements NucleicAcidService {
    @Resource
    ResidentService residentService;
    @Resource
    NucleicAcidMapper nucleicAcidMapper;
    @Override
    public IPage getlist(NucleicAcidDto nucleicAcidDto){
        //设置分页
        Integer currentPage=nucleicAcidDto.getPagination().getCurrentPage();
        Integer pageSize=nucleicAcidDto.getPagination().getPageSize();
        Page<NucleicAcidEntity> NucleicAcidEntityIpage = new Page<>(currentPage,pageSize);
        IPage iPage=null;
        List<String> residentNumbers=new ArrayList<>();
        residentService.getlist(nucleicAcidDto.getPagination().getQueryString().toString()).forEach(resident -> residentNumbers.add(resident.getIdNumber()));
        if (residentNumbers.size() == 0) {
            return new Page();
        }
        LambdaQueryWrapper<NucleicAcidEntity> qw=new LambdaQueryWrapper<>();
        qw.in(NucleicAcidEntity::getIdNumber,residentNumbers);
        if(nucleicAcidDto.getIdNumber()!=null&&!nucleicAcidDto.getIdNumber().equals(""))
            qw.like(NucleicAcidEntity::getIdNumber,nucleicAcidDto.getIdNumber());
        if(nucleicAcidDto.getName()!=null&&!nucleicAcidDto.getName().equals("")){
            List<String> residentNumbers2=new ArrayList<>();
            residentService.list(new LambdaQueryWrapper<ResidentEntity>().like(ResidentEntity::getName,nucleicAcidDto.getName())).forEach(
                    resident -> residentNumbers2.add(resident.getIdNumber()));
            if(residentNumbers2.size()==0)
                return new Page();
            qw.in(NucleicAcidEntity::getIdNumber,residentNumbers2);
        }
        if(nucleicAcidDto.getCollectStartTime()!=null)
            qw.ge(NucleicAcidEntity::getCollectTime,nucleicAcidDto.getCollectStartTime());
        if(nucleicAcidDto.getCollectEndTime()!=null)
            qw.le(NucleicAcidEntity::getCollectTime,nucleicAcidDto.getCollectEndTime());
        if(nucleicAcidDto.getType()!=null&&nucleicAcidDto.getType().size()>0)
            qw.in(NucleicAcidEntity::getType,nucleicAcidDto.getType());
        if(nucleicAcidDto.getResult()!=null&&nucleicAcidDto.getResult().size() > 0)
            qw.in(NucleicAcidEntity::getResult,nucleicAcidDto.getResult());
        qw.orderByDesc(NucleicAcidEntity::getCollectTime);
        iPage = nucleicAcidMapper.selectPage(NucleicAcidEntityIpage, qw);
        setOthers((List<NucleicAcidEntity>) iPage.getRecords());
        return iPage;
    }

    @Override
    public boolean remove(NucleicAcidEntity entity) {
        LambdaQueryWrapper<NucleicAcidEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(NucleicAcidEntity::getIdNumber,entity.getIdNumber());
        qw.eq(NucleicAcidEntity::getCollectTime,entity.getCollectTime());
        return nucleicAcidMapper.delete(qw)>0;
    }

    @Override
    public boolean update(NucleicAcidEntity entity) {
        LambdaQueryWrapper<NucleicAcidEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(NucleicAcidEntity::getIdNumber,entity.getIdNumber());
        qw.eq(NucleicAcidEntity::getCollectTime,entity.getCollectTime());
        return nucleicAcidMapper.update(entity,qw)>0;
    }

    public void setOthers(List<NucleicAcidEntity> nucleicAcidEntities){
        nucleicAcidEntities.forEach(entity -> {
            entity.setName(residentService.getById(entity.getIdNumber()).getName());
            if(entity.getType()!=null){
                switch (entity.getType()){
                    case "0":entity.setType("返乡核酸");break;
                    case "1":entity.setType("隔离核酸");break;
                    case "2":entity.setType("通勤人员核酸");break;
                    case "3":entity.setType("其他");break;
                }
            }
            if(entity.getResult()!=null) {
                switch (entity.getResult()) {
                    case "0":
                        entity.setResult("阳性");
                        break;
                    case "1":
                        entity.setResult("阴性");
                        break;
                    case "2":
                        entity.setResult("未完成");
                        break;
                }
            }
        });
    }

}
