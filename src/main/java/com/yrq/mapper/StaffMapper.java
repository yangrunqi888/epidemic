package com.yrq.mapper;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrq.entity.StaffEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StaffMapper extends BaseMapper<StaffEntity> {

    @Select("select * from staff where employee_number=#{employee_number}")
    StaffEntity selectByIdWithOutLogic(@Param("employee_number") String employee_number);
    @Delete("delete from staff where employee_number=#{employee_number}")
    int deleteByIdWithOutLogic(@Param("employee_number") String employeeNumber);
}
