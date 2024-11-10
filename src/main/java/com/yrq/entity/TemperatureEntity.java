package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author:YangRunqi
 * @create: 2023-02-22 15:45
 * @Description:
 */
@Data
@NoArgsConstructor
@TableName("temperature")
public class TemperatureEntity {
    @TableId(type = IdType.INPUT)
    String idNumber;
    @TableField(exist = false)
    String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    Date collectTime;
    Double result;
}
