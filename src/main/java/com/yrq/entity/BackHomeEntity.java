package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author:YangRunqi
 * @create: 2023-02-23 10:06
 * @Description:
 */
@Data
@NoArgsConstructor
@TableName("back_home")
public class BackHomeEntity {
    String idNumber;
    @TableField(exist = false)
    String name;
    String currentLocation;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    Date backDate;
    String healthCodePath;
    String travelCodePath;
    String state;
    String employerNumber;
    @TableField(exist = false)
    String employerName;
    String demand;
}
