package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("live")
public class LiveEntity {
    @TableId(type = IdType.INPUT)
    private String idNumber;
    private String buildingNumber;
    private String roomNumber;
    private String state;
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "0",delval = "1")
    private int isDel;

}
