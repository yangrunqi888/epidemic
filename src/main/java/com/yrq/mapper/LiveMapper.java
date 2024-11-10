package com.yrq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrq.entity.LiveEntity;
import com.yrq.entity.StaffEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LiveMapper extends BaseMapper<LiveEntity> {

}
