package com.yrq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrq.entity.BuildingEntity;
import com.yrq.entity.ResidentEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BuildingMapper extends BaseMapper<BuildingEntity> {
    @Select("select * from building where building_number=#{building_number} and is_del=1")
    BuildingEntity selectByIdWithOutLogic(@Param("building_number") String buildingNumber);
    @Delete("delete from building where building_number=#{building_number}")
    int deleteByIdWithOutLogic(@Param("building_number") String buildingNumber);
}
