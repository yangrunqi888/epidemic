package com.yrq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:YangRunqi
 * @create: 2023-02-28 14:29
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    String employeeNumber;
    String idNumber;
    String oldPassword;
    String newPassword;
    String position;
}
