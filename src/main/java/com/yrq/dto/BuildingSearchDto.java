package com.yrq.dto;

import com.yrq.utils.QueryPageBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-20 20:09
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingSearchDto {
    QueryPageBean pagination;
    String buildingNumber;
    List<String> grid;
}
