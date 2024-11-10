package com.yrq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:YangRunqi
 * @create: 2023-02-17 19:10
 * @Description:
 */
@Data
@NoArgsConstructor
@TableName("menu")
public class MenuEntity {
    @TableId(type = IdType.INPUT)
    String menuId;
    String name;
    String icon;
    String label;
    String path;
    String parentId;
    String url;
}
