package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.StaffSearchDto;
import com.yrq.entity.GridEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.mapper.GridMapper;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.StaffService;
import com.yrq.utils.ConvertItem;
import com.yrq.utils.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, StaffEntity> implements StaffService {
    @Resource
    StaffMapper staffMapper;
    @Value("${User.portrait-path.staff}")
    String basePath;
    @Autowired
    private StringRedisTemplate redisTemplate;
    public IPage getPage(StaffSearchDto staffSearchDto){
        Integer currentPage=staffSearchDto.getPagination().getCurrentPage();
        Integer pageSize=staffSearchDto.getPagination().getPageSize();
        Page<StaffEntity> staffEntityIPage = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<StaffEntity> qw = new LambdaQueryWrapper<>();
        qw.notIn(StaffEntity::getPosition,"0");
        if(staffSearchDto.getEmployeeNumber()!=null&&!staffSearchDto.getEmployeeNumber().equals(""))
            qw.like(StaffEntity::getEmployeeNumber,staffSearchDto.getEmployeeNumber());
        if(staffSearchDto.getIdNumber()!=null&&!staffSearchDto.getIdNumber().equals(""))
            qw.like(StaffEntity::getIdNumber,staffSearchDto.getIdNumber());
        if(staffSearchDto.getName()!=null&&!staffSearchDto.getName() .equals(""))
            qw.like(StaffEntity::getName,staffSearchDto.getName());
        if(staffSearchDto.getGender()!=null&&!staffSearchDto.getGender().equals(""))
            qw.eq(StaffEntity::getGender,staffSearchDto.getGender());
        if(staffSearchDto.getAge().size()>0){
            qw.between(StaffEntity::getBirthday,java.sql.Date.valueOf(LocalDate.now().minusYears(staffSearchDto.getAge().get(1)+1).plusDays(1))
                    ,java.sql.Date.valueOf(LocalDate.now().minusYears(staffSearchDto.getAge().get(0))));
        }
        if(staffSearchDto.getPoliticsStatus().size()>0)
            qw.in(StaffEntity::getPoliticsStatus,staffSearchDto.getPoliticsStatus());
        if(staffSearchDto.getPosition().size()>0)
            qw.in(StaffEntity::getPosition,staffSearchDto.getPosition());
        if(staffSearchDto.getEmploymentStart().size()>0)
            qw.between(StaffEntity::getEmploymentStart,staffSearchDto.getEmploymentStart().get(0),staffSearchDto.getEmploymentStart().get(1));
        IPage iPage = staffMapper.selectPage(staffEntityIPage, qw);
        setOthers((List<StaffEntity>) iPage.getRecords());
        return iPage;
    }

    @Override
    @Transactional
    public boolean save(StaffEntity entity) {
        StaffEntity staff = staffMapper.selectByIdWithOutLogic(entity.getEmployeeNumber());
        if(staff!=null&&staff.getIsDel()==1){
            staffMapper.deleteByIdWithOutLogic(entity.getEmployeeNumber());
        }
        entity.setPassword( DigestUtils.md5DigestAsHex("666666".getBytes()));
        entity.setProhibitPath("default.jpg");
        return super.save(entity);
    }

    @Override
    public boolean resetPassword(String ID){
        String redisKey;
        StaffEntity entity=new StaffEntity();
        entity.setEmployeeNumber(ID);
        redisKey = "staff:" + ID;
        redisTemplate.delete(redisKey);
        entity.setPassword( DigestUtils.md5DigestAsHex("666666".getBytes()));
        return staffMapper.updateById(entity)>0;
    }

    private void setOthers(List<StaffEntity> entities){
        entities.forEach(entity->{
            if(entity.getGender()!=null)
                entity.setGender(ConvertItem.convertGender(entity.getGender()));
            if(entity.getPosition()!=null)
                entity.setPosition(ConvertItem.convertPosition(entity.getPosition()));
            if(entity.getBirthday()!=null)
                entity.setAge(ConvertItem.convertAge(entity.getBirthday()));
        });
    }
}
