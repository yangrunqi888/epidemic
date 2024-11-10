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
 * @create: 2023-02-24 12:32
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuarantineDto {
    QueryPageBean pagination;
    String idNumber;
    String name;
    String type;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    List<Date> startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    List<Date> endTime;
    List<Integer> days;
    String state;
}
