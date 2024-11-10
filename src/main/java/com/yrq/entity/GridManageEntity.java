package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("grid_manage")

public class GridManageEntity {
    @TableId(type = IdType.INPUT)
    String gridNumber;
    String employeeNumber;
}
