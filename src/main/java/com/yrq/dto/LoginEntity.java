package com.yrq.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:YangRunqi
 * @create: 2023-01-26 21:43
 * @Description: 登录对象
 */
@Data
@NoArgsConstructor
public class LoginEntity {
    private String idNumber;
    private String password;
}
