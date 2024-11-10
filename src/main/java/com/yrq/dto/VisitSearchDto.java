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
 * @create: 2023-03-03 12:48
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitSearchDto {
    QueryPageBean pagination;
    String visitNumber;
    String visitName;
    String idNumber;
    String residentName;
    String guardNumber;
    String guardName;
    String state;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    List<Date> entryTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    List<Date> leaveTime;
}
