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
 * @create: 2023-02-25 11:55
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintSearchDto {
    QueryPageBean pagination;
    String type;
    String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    List<Date> complaintTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    List<Date> handleTime;
    String employeeId;
    String employeeName;
    String state;

    String idNumber;
}
