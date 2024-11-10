package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
@TableName("building")
public class BuildingEntity {
    @TableId(type = IdType.INPUT)
    private String buildingNumber;//楼房号

    private String perFloorRoomNumber;//每层房间数

    private String floorNumber;//总层数

    @TableField(exist = false)
    private Integer totalPerson;//当前居住总人数

    @TableField(value="header",updateStrategy=FieldStrategy.IGNORED)
    private String headerId;//楼栋长身份证号
    @TableField(exist = false)
    private String headerName;//楼栋长姓名
    @TableField(exist = false)
    private String headerRoom;//楼栋长房间号

    private String gridNumber;//网格号

    private String remark;//备注
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "0",delval = "1")
    private int isDel;
}
