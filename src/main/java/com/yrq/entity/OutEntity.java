package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("go_out")
public class OutEntity {
    @TableId(type = IdType.INPUT)
    String idNumber;
    @TableField(exist = false)
    String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    Date outTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    Date backTime;
    Double outTotalTime;
    String reason;

    String outGuardNumber;
    @TableField(exist = false)
    String outGuardName;
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    String backGuardNumber;
    @TableField(exist = false)
    String backGuardName;
    String state;

}
