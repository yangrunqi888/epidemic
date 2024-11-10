package com.yrq.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yrq.utils.QueryPageBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-27 19:45
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuardSearchDto {
    QueryPageBean pagination;
    String guardNumber;
    String idNumber;
    String name;
    String gender;
    List<Integer> age;
    List<String> politicsStatus;
    List<String> position;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    List<Date> guardStart;
}
