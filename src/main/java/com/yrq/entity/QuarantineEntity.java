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
 * @create: 2023-02-24 12:24
 * @Description:
 */
@Data
@NoArgsConstructor
@TableName("quarantine")
public class QuarantineEntity {
    String idNumber;
    @TableField(exist = false)
    String name;
    String type;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    Date endTime;
    @TableField(exist = false)
    Integer days;
    @TableField(exist = false)
    String state;
}
