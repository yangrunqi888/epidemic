package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.GuardSearchDto;
import com.yrq.entity.GridEntity;
import com.yrq.entity.GuardEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.mapper.GridMapper;
import com.yrq.mapper.GuardMapper;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.GuardService;
import com.yrq.utils.ConvertItem;
import com.yrq.utils.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Service
public class GuardServiceImpl extends ServiceImpl<GuardMapper, GuardEntity> implements GuardService {
    @Resource
    GuardMapper guardMapper;
    @Value("${User.portrait-path.guard}")
    String basePath;
    @Autowired
    private StringRedisTemplate redisTemplate;
    public IPage getPage(GuardSearchDto guardSearchDto){
        Integer currentPage=guardSearchDto.getPagination().getCurrentPage();
        Integer pageSize=guardSearchDto.getPagination().getPageSize();
        Page<GuardEntity> GridEntityIPage = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<GuardEntity> qw = new LambdaQueryWrapper<>();
        if(guardSearchDto.getGuardNumber()!=null&&!guardSearchDto.getGuardNumber().equals(""))
            qw.like(GuardEntity::getGuardNumber,guardSearchDto.getGuardNumber());
        if(guardSearchDto.getIdNumber()!=null&&!guardSearchDto.getIdNumber().equals(""))
            qw.like(GuardEntity::getIdNumber,guardSearchDto.getIdNumber());
        if(guardSearchDto.getName()!=null&&!guardSearchDto.getName() .equals(""))
            qw.like(GuardEntity::getName,guardSearchDto.getName());
        if(guardSearchDto.getGender()!=null&&!guardSearchDto.getGender().equals(""))
            qw.eq(GuardEntity::getGender,guardSearchDto.getGender());
        if(guardSearchDto.getAge().size()>0){
            qw.between(GuardEntity::getBirthday,java.sql.Date.valueOf(LocalDate.now().minusYears(guardSearchDto.getAge().get(1)+1).plusDays(1))
                    ,java.sql.Date.valueOf(LocalDate.now().minusYears(guardSearchDto.getAge().get(0))));
        }
        if(guardSearchDto.getPoliticsStatus().size()>0)
            qw.in(GuardEntity::getPoliticsStatus,guardSearchDto.getPoliticsStatus());
        if(guardSearchDto.getPosition().size()>0)
            qw.in(GuardEntity::getPosition,guardSearchDto.getPosition());
        if(guardSearchDto.getGuardStart().size()>0)
            qw.between(GuardEntity::getGuardStart,guardSearchDto.getGuardStart().get(0),guardSearchDto.getGuardStart().get(1));
        IPage iPage = guardMapper.selectPage(GridEntityIPage, qw);
        setOthers((List<GuardEntity>) iPage.getRecords());
        return iPage;
    }

    @Override
    public boolean save(GuardEntity entity) {
        GuardEntity guard = guardMapper.selectByIdWithOutLogic(entity.getGuardNumber());
        if(guard!=null&&guard.getIsDel()==1){
            guardMapper.deleteByIdWithOutLogic(entity.getGuardNumber());
        }
        entity.setPassword( DigestUtils.md5DigestAsHex("666666".getBytes()));
        entity.setProhibitPath(basePath+"default.jpg");
        return super.save(entity);
    }

    @Override
    public boolean resetPassword(String ID){
        String redisKey;
        GuardEntity entity=new GuardEntity();
        entity.setGuardNumber(ID);
        redisKey = "guard:" + ID;
        redisTemplate.delete(redisKey);
        entity.setPassword( DigestUtils.md5DigestAsHex("666666".getBytes()));
        return guardMapper.updateById(entity)>0;
    }
    private void setOthers(List<GuardEntity> entities){
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
