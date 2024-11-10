package com.yrq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:YangRunqi
 * @create: 2023-03-02 12:32
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeCardDto {
    String name;
    Integer value;
    String icon;
    String color;
}
