package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:YangRunqi
 * @create: 2023-02-21 16:54
 * @Description:
 */
@Data
@NoArgsConstructor
@TableName("nucleic_acid")
public class NucleicAcidEntity implements Serializable {
    @TableId(type = IdType.INPUT)
    String idNumber;
    @TableField(exist = false)
    String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    Date collectTime;
    String type;
    String result;
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    String picturePath;
}
