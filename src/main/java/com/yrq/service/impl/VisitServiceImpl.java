package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.VisitSearchDto;
import com.yrq.entity.*;
import com.yrq.mapper.*;
import com.yrq.service.ResidentService;
import com.yrq.service.VisitService;
import com.yrq.utils.ConvertItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class VisitServiceImpl extends ServiceImpl<VisitMapper, VisitEntity> implements VisitService {

    @Resource
    VisitMapper visitMapper;
    @Resource
    VisitorMapper visitorMapper;
    @Resource
    ResidentMapper residentMapper;
    @Resource
    LiveMapper liveMapper;
    @Resource
    GuardMapper guardMapper;
    @Resource
    ResidentService residentService;
    @Override
    public IPage getPage(VisitSearchDto visitSearchDto){
        Integer currentPage=visitSearchDto.getPagination().getCurrentPage();
        Integer pageSize=visitSearchDto.getPagination().getPageSize();
        Page<VisitEntity> visitEntityIPage = new Page<>(currentPage,pageSize);

        LambdaQueryWrapper<VisitEntity> qw = new LambdaQueryWrapper<>();


        if(visitSearchDto.getPagination().getQueryString()!=null&&!visitSearchDto.getPagination().getQueryString().toString().equals("")){
            List<String> residentNumbers=new ArrayList<>();
            residentService.getlist(visitSearchDto.getPagination().getQueryString().toString()).forEach(resident -> residentNumbers.add(resident.getIdNumber()));
            qw.in(VisitEntity::getIdNumber,residentNumbers);
        }
        if(visitSearchDto.getVisitNumber()!=null&&!visitSearchDto.getVisitNumber().equals("")){
            qw.like(VisitEntity::getVisitNumber,visitSearchDto.getVisitNumber());
        }
        if(visitSearchDto.getVisitName()!=null&&!visitSearchDto.getVisitName().equals("")){
            List<String> visitNumbers=new ArrayList<>();
            visitorMapper.selectList(new LambdaQueryWrapper<VisitorEntity>().like(VisitorEntity::getName,visitSearchDto.getVisitName())).forEach(
                    visitor -> visitNumbers.add(visitor.getIdNumber()));
            qw.in(VisitEntity::getVisitNumber,visitNumbers);
        }
        if(visitSearchDto.getIdNumber()!=null&&!visitSearchDto.getIdNumber().equals("")){
            qw.like(VisitEntity::getIdNumber,visitSearchDto.getIdNumber());
        }
        if(visitSearchDto.getResidentName()!=null&&!visitSearchDto.getResidentName().equals("")){
            List<String> residentNumbers2=new ArrayList<>();
            residentService.list(new LambdaQueryWrapper<ResidentEntity>().like(ResidentEntity::getName,visitSearchDto.getResidentName())).forEach(
                    resident -> residentNumbers2.add(resident.getIdNumber()));
            qw.in(VisitEntity::getIdNumber,residentNumbers2);
        }
        if(visitSearchDto.getGuardNumber()!=null&&!visitSearchDto.getGuardNumber().equals("")){
            if(visitSearchDto.getGuardNumber().equals("admin")){
                qw.and(qw2->qw2.isNull(VisitEntity::getInGuardNumber).or(qw3->qw3.isNull(VisitEntity::getOutGuardNumber).eq(VisitEntity::getState,"1")));
            }
            else{
                qw.and(qw2->qw2.like(VisitEntity::getOutGuardNumber,visitSearchDto.getGuardNumber()).or()
                        .like(VisitEntity::getInGuardNumber,visitSearchDto.getGuardNumber()));
            }
        }
        if(visitSearchDto.getGuardName()!=null&&!visitSearchDto.getGuardName().equals("")){
            if(visitSearchDto.getGuardName().equals("admin")){
                qw.and(qw2->qw2.isNull(VisitEntity::getInGuardNumber).or(qw3->qw3.isNull(VisitEntity::getOutGuardNumber).eq(VisitEntity::getState,"1")));
            }
            else {
                List<String> guardNumbers = new ArrayList<>();
                guardMapper.selectList(new LambdaQueryWrapper<GuardEntity>().like(GuardEntity::getName, visitSearchDto.getGuardName())).forEach(
                        guard -> guardNumbers.add(guard.getGuardNumber())
                );
                qw.and(qw2 -> qw2.in(VisitEntity::getOutGuardNumber, guardNumbers).or()
                        .in(VisitEntity::getInGuardNumber, guardNumbers));
            }
        }
        if(visitSearchDto.getState()!=null&&!visitSearchDto.getState().equals("")){
            qw.eq(VisitEntity::getState,visitSearchDto.getState());
        }
        if (visitSearchDto.getEntryTime()!=null&&visitSearchDto.getEntryTime().size() > 0){
            qw.between(VisitEntity::getEntryTime,visitSearchDto.getEntryTime().get(0),visitSearchDto.getEntryTime().get(1));
        }
        if (visitSearchDto.getLeaveTime()!=null&&visitSearchDto.getLeaveTime().size() > 0){
            qw.between(VisitEntity::getLeaveTime,visitSearchDto.getLeaveTime().get(0),visitSearchDto.getLeaveTime().get(1));
        }
        qw.orderByAsc(VisitEntity::getState);
        IPage iPage = visitMapper.selectPage(visitEntityIPage, qw);

        iPage.getRecords().forEach(visit-> convert((VisitEntity) visit));

        return iPage;
    }
    @Override
    public String getVisitRecords(String id){
        AtomicReference<String> records= new AtomicReference<>("");
        visitMapper.selectList(new LambdaQueryWrapper<VisitEntity>().eq(VisitEntity::getVisitNumber,id).orderByDesc(VisitEntity::getEntryTime)).forEach(
                record->{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String entryTime=dateFormat.format(record.getEntryTime());
                    String name=residentService.getById(record.getIdNumber()).getName();
                    LiveEntity liveEntity = liveMapper.selectOne(new LambdaQueryWrapper<LiveEntity>().eq(LiveEntity::getIdNumber, record.getIdNumber()));
                    String address=liveEntity.getBuildingNumber()+"号"+liveEntity.getRoomNumber()+"室";
                    if(record.getState().equals("0")){
                       records.set(records + "于" + entryTime + "访问" + address + "的" + name + "，无出小区记录或至今未出。\n");
                    }
                    else {
                        String leaveTime=dateFormat.format(record.getLeaveTime());
                        records.set(records+"于"+entryTime+"至"+leaveTime+"访问"+address+"的"+name+"\n");
                    }

                }
        );
        return records.get();
    }
    @Override
    @Transactional
    public boolean save(VisitEntity entity) {
        VisitorEntity visitorEntity=new VisitorEntity();
        visitorEntity.setIdNumber(entity.getVisitNumber());
        visitorEntity.setName(entity.getVisitName());
        visitorEntity.setGender(entity.getGender());
        visitorEntity.setPhone(entity.getPhone());

        LambdaQueryWrapper<VisitorEntity> qw=new LambdaQueryWrapper<>();
        qw.eq(VisitorEntity::getIdNumber,entity.getVisitNumber());
        if(visitorMapper.selectOne(qw)==null){
            visitorMapper.insert(visitorEntity);
        }
        else{
            visitorMapper.updateById(visitorEntity);
        }
        entity.setEntryTime(new Date());
        return super.save(entity);
    }
    @Override
    public boolean update(VisitEntity entity) {
        entity.setState("1");
        entity.setLeaveTime(new Date());
        double diff=(entity.getLeaveTime().getTime()-entity.getEntryTime().getTime())/60000.0;
        entity.setVisitTotalTime(diff);
        LambdaQueryWrapper<VisitEntity> qw=new LambdaQueryWrapper<>();
        qw.eq(VisitEntity::getVisitNumber,entity.getVisitNumber());
        qw.eq(VisitEntity::getEntryTime,entity.getEntryTime());
        return update(entity,qw);
    }
    @Override
    public boolean remove(VisitEntity entity) {
        LambdaQueryWrapper<VisitEntity> qw=new LambdaQueryWrapper<>();
        qw.eq(VisitEntity::getVisitNumber,entity.getVisitNumber());
        qw.eq(VisitEntity::getEntryTime,entity.getEntryTime());
        return remove(qw);
    }
    private void convert(VisitEntity visit){
        VisitorEntity visitor = visitorMapper.selectById(visit.getVisitNumber());
        visit.setVisitName(visitor.getName());
        visit.setGender(ConvertItem.convertGender(visitor.getGender()));
        visit.setPhone(visitor.getPhone());
        visit.setResidentName(residentMapper.selectById(visit.getIdNumber()).getName());

        LambdaQueryWrapper<LiveEntity> qw3=new LambdaQueryWrapper<>();
        qw3.eq(LiveEntity::getIdNumber,visit.getIdNumber());
        LiveEntity live = liveMapper.selectOne(qw3);
        visit.setBuildingNumber(live.getBuildingNumber());
        visit.setRoomNumber(live.getRoomNumber());
        visit.setBuildingRomNumber(live.getBuildingNumber()+"号"+live.getRoomNumber()+"室");

        if(visit.getInGuardNumber()!=null&&!visit.getInGuardNumber().equals("")) {
            visit.setInGuardName(guardMapper.selectByIdWithOutLogic(visit.getInGuardNumber()).getName());
        }
        else{
            visit.setInGuardNumber("admin");
            visit.setInGuardName("admin");
        }
        if(visit.getState().equals("1")){
           if(visit.getOutGuardNumber()!=null&&!visit.getOutGuardNumber().equals("")){
               visit.setOutGuardName(guardMapper.selectByIdWithOutLogic(visit.getOutGuardNumber()).getName());
           }
           else{
               visit.setOutGuardNumber("admin");
               visit.setOutGuardName("admin");
           }
            visit.setVisitTotalTime(Double.parseDouble(String.format("%.2f", visit.getVisitTotalTime())));
        }

        switch(visit.getState()){
            case "0":visit.setState("未离开");break;
            case "1":visit.setState("已离开");break;
        }

    }
}
