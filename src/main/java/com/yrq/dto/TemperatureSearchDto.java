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
 * @create: 2023-02-22 15:52
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureSearchDto {
    QueryPageBean pagination;
    String idNumber;
    String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    Date collectStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    Date collectEndTime;
    List<String> result;
}
