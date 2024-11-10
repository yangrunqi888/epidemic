package com.yrq.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthorityMapper {
    @Select("select menu_id from authority where user_type=#{userType}")
    public List<String> getMenus(@Param("userType") String userType);
}
