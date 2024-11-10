package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("visit")
public class VisitEntity {
    @TableId(type = IdType.INPUT)
    private String visitNumber;
    @TableField(exist = false)
    private String visitName;
    @TableField(exist = false)
    private String gender;
    @TableField(exist = false)
    private String phone;

    private String idNumber;
    @TableField(exist = false)
    private String residentName;
    @TableField(exist = false)
    private String buildingNumber;
    @TableField(exist = false)
    private String roomNumber;
    @TableField(exist = false)
    private String buildingRomNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    private Date entryTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    private Date leaveTime;

    private Double visitTotalTime;

    private String reason;

    private String outGuardNumber;
    @TableField(exist = false)
    private String outGuardName;
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    private String inGuardNumber;
    @TableField(exist = false)
    private String inGuardName;

    private String state;




}
