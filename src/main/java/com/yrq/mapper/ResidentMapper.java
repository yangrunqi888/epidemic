package com.yrq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.StaffEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ResidentMapper extends BaseMapper<ResidentEntity> {
    @Select("select * from resident where id_number=#{id_number} and is_del=1")
    ResidentEntity selectByIdWithOutLogic(@Param("id_number") String idNumber);
    @Delete("delete from resident where id_number=#{id_number}")
    int deleteByIdWithOutLogic(@Param("id_number") String idNumber);
}
