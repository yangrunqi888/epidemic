package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.yrq.dto.QuarantineDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.BackHomeEntity;
import com.yrq.entity.QuarantineEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.mapper.AntigenMapper;
import com.yrq.mapper.QuarantineMapper;
import com.yrq.service.QuarantineService;
import com.yrq.service.ResidentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-24 12:39
 * @Description:
 */
@Service
public class QuarantineServiceImpl extends ServiceImpl<QuarantineMapper, QuarantineEntity> implements QuarantineService {
    @Resource
    ResidentService residentService;
    @Resource
    QuarantineMapper quarantineMapper;

    @Override
    public IPage getlist(QuarantineDto quarantineDto) {
        //设置分页
        Integer currentPage = quarantineDto.getPagination().getCurrentPage();
        Integer pageSize = quarantineDto.getPagination().getPageSize();
        Page<QuarantineEntity> AntigenEntityIpage = new Page<>(currentPage, pageSize);
        IPage iPage = null;
        List<String> residentNumbers = new ArrayList<>();
        residentService.getlist(quarantineDto.getPagination().getQueryString().toString()).forEach(resident -> residentNumbers.add(resident.getIdNumber()));
        LambdaQueryWrapper<QuarantineEntity> qw = new LambdaQueryWrapper<>();
        qw.in(QuarantineEntity::getIdNumber, residentNumbers);
        if (quarantineDto.getIdNumber()!=null&&!quarantineDto.getIdNumber() .equals(""))
            qw.like(QuarantineEntity::getIdNumber, quarantineDto.getIdNumber());
        if (quarantineDto.getName()!=null&&!quarantineDto.getName() .equals("")) {
            List<String> residentNumbers2 = new ArrayList<>();
            residentService.list(new LambdaQueryWrapper<ResidentEntity>().like(ResidentEntity::getName, quarantineDto.getName())).forEach(
                    resident -> residentNumbers2.add(resident.getIdNumber()));
            if(residentNumbers2.size() == 0)
                return new Page();
            qw.in(QuarantineEntity::getIdNumber, residentNumbers2);
        }
        if(quarantineDto.getType()!=null&&!quarantineDto.getType().equals("")){
            qw.eq(QuarantineEntity::getType,quarantineDto.getType());
        }
        if (quarantineDto.getStartTime().size() > 0)
            qw.between(QuarantineEntity::getStartTime, quarantineDto.getStartTime().get(0), quarantineDto.getStartTime().get(1));
        if (quarantineDto.getEndTime() .size() > 0)
            qw.between(QuarantineEntity::getEndTime, quarantineDto.getEndTime().get(0), quarantineDto.getEndTime().get(1));
        if (quarantineDto.getDays().size() != 0) {
            qw.apply("(DATEDIFF(end_time, start_time)) BETWEEN {0} AND {1}", quarantineDto.getDays().get(0), quarantineDto.getDays().get(1));
        }
        if (quarantineDto.getState() != null) {
            switch (quarantineDto.getState()) {
                case "0":
                    qw.gt(QuarantineEntity::getStartTime, new Date());
                    break;//未隔离
                case "1":
                    qw.le(QuarantineEntity::getStartTime, new Date()).gt(QuarantineEntity::getEndTime, new Date());
                    break;//正在隔离
                case "2":
                    qw.le(QuarantineEntity::getEndTime, new Date());
                    break;//结束隔离
            }
        }
        iPage = quarantineMapper.selectPage(AntigenEntityIpage, qw);
        setOthers((List<QuarantineEntity>) iPage.getRecords());
        return iPage;

    }

    @Override
    public boolean remove(QuarantineEntity entity) {
        LambdaQueryWrapper<QuarantineEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(QuarantineEntity::getIdNumber,entity.getIdNumber());
        qw.eq(QuarantineEntity::getStartTime,entity.getStartTime());
        return quarantineMapper.delete(qw)>0;
    }

    public void setOthers(List<QuarantineEntity> quarantineEntities) {
        quarantineEntities.forEach(entity->{
            entity.setName(residentService.getById(entity.getIdNumber()).getName());

            entity.setType(entity.getType().equals("0")?"居家隔离":"集中隔离");
            LocalDate start = entity.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = entity.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            entity.setDays((int) ChronoUnit.DAYS.between(start, end));



            if(entity.getStartTime().compareTo(new Date())>0){
                entity.setState("未开始隔离");
            }
            else if(entity.getEndTime().compareTo(new Date())<0){
                entity.setState("结束隔离");
            }
            else{
                entity.setState("正在隔离");
            }
        });
    }
}