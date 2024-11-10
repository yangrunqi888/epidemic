package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("staff")
public class StaffEntity {
    @TableId(type = IdType.INPUT)
    private String employeeNumber;

    private String idNumber;

    private String name;

    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    private Date birthday;

    @TableField(exist = false)
    private Integer age;

    private String politicsStatus;

    private String education;

    private String position;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    private Date   employmentStart;

    private String phone;

    private String email;
    @JsonIgnore
    private String password;

    private String remark;
    @JsonIgnore
    private String prohibitPath;
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "0",delval = "1")
    private int isDel;
}
