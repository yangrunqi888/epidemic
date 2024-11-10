package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@TableName("resident")
public class ResidentEntity implements Serializable {

    @TableId(type = IdType.INPUT)
    private String idNumber;

    private String name;

    private String gender;
    @TableField(exist = false)
    private Integer age;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    private Date birthDay;

    private String nation;

    private String politicsStatus;

    private String job;
    @TableField(exist = false)
    private String nativePlace;

    private String nativeProvince;

    private String nativeCity;

    private String permanentResidentAddress;

    private String phone;

    private String email;
    @JsonIgnore
    private String password;

    private String state;
    @JsonIgnore
    private String prohibitPath;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "0",delval = "1")
    private int isDel;


    @TableField(exist = false)
    private String buildingNumber;

    @TableField(exist = false)
    private String roomNumber;

    @TableField(exist = false)
    private String buildingRoomNumber;

    @TableField(exist = false)
    private String housingState;
}
