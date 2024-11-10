package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.LiveDto;
import com.yrq.entity.LiveEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.mapper.LiveMapper;
import com.yrq.mapper.ResidentMapper;
import com.yrq.service.LiveService;
import com.yrq.service.ResidentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class LiveServiceImpl  extends ServiceImpl<LiveMapper, LiveEntity> implements LiveService {
    @Resource
    LiveMapper liveMapper;
    @Resource
    ResidentMapper residentMapper;
    @Override
    public List<LiveDto> getResidentByBuilding(String id) {
        List<LiveDto> lives=new ArrayList<>();
        liveMapper.selectList(new LambdaQueryWrapper<LiveEntity>().eq(LiveEntity::getBuildingNumber,id)).forEach(
                resident-> lives.add(new LiveDto(resident.getIdNumber(),null)));
        lives.forEach(live->live.setName(residentMapper.selectById(live.getIdNumber()).getName()));
        return lives;
    }
}
