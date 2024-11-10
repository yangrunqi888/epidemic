package com.yrq.dto;

import com.yrq.entity.StaffEntity;
import com.yrq.utils.QueryPageBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-27 14:18
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GridSearchDto {
    QueryPageBean pagination;
    String gridNumber;
    String leadership;
    String leadershipName;
    List<String> staffs;
}
