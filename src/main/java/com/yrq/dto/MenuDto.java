package com.yrq.dto;

import com.yrq.entity.MenuEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-05 00:45
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto extends MenuEntity {

//    String menuId;
//    String name;
//    String icon;
//    String label;
//    String path;
//    String parentId;
//    String component;
    List<MenuDto> children;
}
