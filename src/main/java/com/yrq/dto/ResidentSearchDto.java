package com.yrq.dto;

import com.yrq.utils.QueryPageBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-20 12:44
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentSearchDto {
    QueryPageBean pagination;
    String idNumber;
    String name;
    String gender;
    Integer maxAge;
    Integer minAge;
    List<String> nation;
    List<String> politicsStatus;
    String state;
    List<String> buildingNumber;
    String housingState;
}
