package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.BackHomeSearchDto;
import com.yrq.entity.BackHomeEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.mapper.BackHomeMapper;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.BackHomeService;
import com.yrq.service.ResidentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author:YangRunqi
 * @create: 2023-02-23 10:20
 * @Description:
 */
@Service
public class BackHomeServiceImpl extends ServiceImpl<BackHomeMapper, BackHomeEntity> implements BackHomeService {
    @Resource
    ResidentService residentService;
    @Resource
    BackHomeMapper backHomeMapper;
    @Resource
    StaffMapper staffMapper;
    @Resource
    MyMailService myMailService;
    @Override
    public IPage getlist(BackHomeSearchDto backHomeSearchDto) {
        //设置分页
        Integer currentPage=backHomeSearchDto.getPagination().getCurrentPage();
        Integer pageSize=backHomeSearchDto.getPagination().getPageSize();
        Page<BackHomeEntity> backHomeEntityPage = new Page<>(currentPage,pageSize);
        IPage iPage=null;
        List<String> residentNumbers=new ArrayList<>();
        residentService.getlist(backHomeSearchDto.getPagination().getQueryString().toString()).forEach(resident -> residentNumbers.add(resident.getIdNumber()));
        if (residentNumbers.size() == 0) {
            return new Page();
        }
        LambdaQueryWrapper<BackHomeEntity> qw=new LambdaQueryWrapper<>();
        qw.in(BackHomeEntity::getIdNumber,residentNumbers);
        if(backHomeSearchDto.getIdNumber()!=null&&!backHomeSearchDto.getIdNumber().equals(""))
            qw.like(BackHomeEntity::getIdNumber,backHomeSearchDto.getIdNumber());
        if(backHomeSearchDto.getName()!=null&&!backHomeSearchDto.getName().equals("")){
            List<String> residentNumbers2=new ArrayList<>();
            residentService.list(new LambdaQueryWrapper<ResidentEntity>().like(ResidentEntity::getName,backHomeSearchDto.getName())).forEach(
                    resident -> residentNumbers2.add(resident.getIdNumber()));
            if(residentNumbers2.size()==0)
                return new Page();
            qw.in(BackHomeEntity::getIdNumber,residentNumbers2);
        }
        if(backHomeSearchDto.getStartBackDate()!=null)
            qw.between(BackHomeEntity::getBackDate,backHomeSearchDto.getStartBackDate(),backHomeSearchDto.getEndBackDate());
        if(backHomeSearchDto.getState()!=null&&backHomeSearchDto.getState().size() > 0)
            qw.in(BackHomeEntity::getState,backHomeSearchDto.getState());
        if(backHomeSearchDto.getEmployerNumber()!=null&&!backHomeSearchDto.getEmployerNumber().equals(""))
            qw.like(BackHomeEntity::getEmployerNumber,backHomeSearchDto.getEmployerNumber());
        if(backHomeSearchDto.getEmployerName()!=null&&!backHomeSearchDto.getEmployerName().equals("")){
            List<String> staffs=new ArrayList<>();
            staffMapper.selectList(new LambdaQueryWrapper<StaffEntity>().like(StaffEntity::getName,backHomeSearchDto.getEmployerName()))
                    .forEach(staff->staffs.add(staff.getEmployeeNumber()));
            if(staffs.size()==0)
                return new Page();
            qw.in(BackHomeEntity::getEmployerNumber,staffs);
        }
        qw.orderByDesc(BackHomeEntity::getState);
        iPage=backHomeMapper.selectPage(backHomeEntityPage,qw);
        setOthers((List<BackHomeEntity>) iPage.getRecords());
        return iPage;
    }

    @Override
    public boolean verify(BackHomeEntity back) {
        LambdaQueryWrapper<BackHomeEntity> qw=new LambdaQueryWrapper<>();
        qw.eq(BackHomeEntity::getIdNumber,back.getIdNumber());
        qw.eq(BackHomeEntity::getBackDate,back.getBackDate());
        boolean update = backHomeMapper.update(back, qw) > 0;
        ResidentEntity resident = residentService.getById(back.getIdNumber());
        String email =resident.getEmail();
        if(email!=null&&!email.equals("")) {
            boolean matches = Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
            if(matches){
                String name=resident.getName();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                String backDate=dateFormat.format(back.getBackDate());
                String state,demand;
                if(back.getState().equals("1")){
                    state="已通过";
                    demand="返乡要求";
                }
                else{
                    state="未通过";
                    demand="原因";
                }
                String text=String.format("亲爱的居民%s，您%s的返乡申请审核%s，请及时进入社区疫情系统查看具体%s",name,backDate,state,demand);
                myMailService.sendMail(
                        "250244033@qq.com",
                        email,
                        "关于返乡申请审核完成的通知",
                        text);

            }
        }
        return update;
    }

    @Override
    public boolean remove(BackHomeEntity entity) {
        LambdaQueryWrapper<BackHomeEntity> qw=new LambdaQueryWrapper<>();
        qw.eq(BackHomeEntity::getIdNumber,entity.getIdNumber());
        qw.eq(BackHomeEntity::getBackDate,entity.getBackDate());
        return remove(qw);
    }

    public void setOthers(List<BackHomeEntity> entities){
        entities.forEach(entity->{
            entity.setName(residentService.getById(entity.getIdNumber()).getName());
            if(entity.getEmployerNumber()!=null)
                entity.setEmployerName(staffMapper.selectByIdWithOutLogic(entity.getEmployerNumber()).getName());
            switch (entity.getState()){
                case "0": entity.setState("未通过");break;
                case "1": entity.setState("已通过");break;
                case "2": entity.setState("未审核");break;
            }
        });
    }
}
