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
 * @create: 2023-02-25 11:44
 * @Description:
 */
@Data
@NoArgsConstructor
@TableName("complaint")
public class ComplaintEntity {
    @TableId(type = IdType.ASSIGN_ID)
    String id;
    String type;
    String title;
    String text;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    Date complaintTime;
    String complaintId;
    @TableField(exist = false)
    String complaintName;
    String employeeId;
    @TableField(exist = false)
    String employeeName;
    String result;
    String state;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    Date handleTime;
    Double comment;
    Integer anonymous;
}
