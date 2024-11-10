package com.yrq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrq.entity.GuardEntity;
import com.yrq.entity.StaffEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GuardMapper extends BaseMapper<GuardEntity> {
    @Select("select * from guard where guard_number=#{guard_number}")
    GuardEntity selectByIdWithOutLogic(@Param("guard_number") String guard_number);
    @Delete("delete from guard where guard_number=#{guard_number}")
    int deleteByIdWithOutLogic(@Param("guard_number") String guardNumber);
}
