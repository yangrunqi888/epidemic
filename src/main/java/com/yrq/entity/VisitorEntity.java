package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("visitor")
public class VisitorEntity {
    @TableId(type = IdType.INPUT)
    private String idNumber;

    private String name;

    private String gender;

    private String phone;
}
