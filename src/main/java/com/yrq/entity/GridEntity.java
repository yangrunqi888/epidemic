package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@TableName("grid")
public class GridEntity {
    @TableId(type = IdType.INPUT)
    private String gridNumber;

    private Integer area;

    private String region;
    @TableField(value="leadership",updateStrategy= FieldStrategy.IGNORED)
    private String leadership;
    @TableField(exist = false)
    private String leadershipName;

    private String remark;
    @TableField(exist = false)
    private List<StaffEntity> staffs=new ArrayList<>();
    @TableField(exist = false)
    private List<String> staffNumbers=new ArrayList<>();
}
