package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.AntigenSearchDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.NucleicAcidEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.mapper.AntigenMapper;
import com.yrq.mapper.NucleicAcidMapper;
import com.yrq.service.AntigenService;
import com.yrq.service.ResidentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-22 13:07
 * @Description:
 */
@Service
public class AntigenServiceImpl extends ServiceImpl<AntigenMapper, AntigenEntity> implements AntigenService {

    @Resource
    ResidentService residentService;
    @Resource
    AntigenMapper antigenMapper;
    @Override
    public IPage getlist(AntigenSearchDto antigenSearchDto) {
        //设置分页
        Integer currentPage=antigenSearchDto.getPagination().getCurrentPage();
        Integer pageSize=antigenSearchDto.getPagination().getPageSize();
        Page<AntigenEntity> AntigenEntityIpage = new Page<>(currentPage,pageSize);
        IPage iPage=null;
        List<String> residentNumbers=new ArrayList<>();
        residentService.getlist(antigenSearchDto.getPagination().getQueryString().toString()).forEach(resident -> residentNumbers.add(resident.getIdNumber()));
        LambdaQueryWrapper<AntigenEntity> qw=new LambdaQueryWrapper<>();
        if (residentNumbers.size() == 0) {
            return new Page();
        }
        qw.in(AntigenEntity::getIdNumber,residentNumbers);
        if(antigenSearchDto.getIdNumber()!=null&&!antigenSearchDto.getIdNumber().equals(""))
            qw.like(AntigenEntity::getIdNumber,antigenSearchDto.getIdNumber());
        if(antigenSearchDto.getName()!=null&&!antigenSearchDto.getName().equals("")){
            List<String> residentNumbers2=new ArrayList<>();
            residentService.list(new LambdaQueryWrapper<ResidentEntity>().like(ResidentEntity::getName,antigenSearchDto.getName())).forEach(
                    resident -> residentNumbers2.add(resident.getIdNumber()));
            if(residentNumbers2.size()==0)
                return new Page();
            qw.in(AntigenEntity::getIdNumber,residentNumbers2);
        }
        if(antigenSearchDto.getCollectStartTime()!=null)
            qw.ge(AntigenEntity::getCollectTime,antigenSearchDto.getCollectStartTime());
        if(antigenSearchDto.getCollectEndTime()!=null)
            qw.le(AntigenEntity::getCollectTime,antigenSearchDto.getCollectEndTime());
        if(antigenSearchDto.getResult()!=null&&antigenSearchDto.getResult().size() > 0)
            qw.in(AntigenEntity::getResult,antigenSearchDto.getResult());
        qw.orderByDesc(AntigenEntity::getCollectTime);
        iPage = antigenMapper.selectPage(AntigenEntityIpage, qw);
        setOthers((List<AntigenEntity>) iPage.getRecords());
        return iPage;
    }

    @Override
    public boolean remove(AntigenEntity entity) {
        LambdaQueryWrapper<AntigenEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(AntigenEntity::getIdNumber,entity.getIdNumber());
        qw.eq(AntigenEntity::getCollectTime,entity.getCollectTime());
        return antigenMapper.delete(qw)>0;
    }

    public void setOthers(List<AntigenEntity> antigenEntities){
        antigenEntities.forEach(entity -> {
            entity.setName(residentService.getById(entity.getIdNumber()).getName());
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
