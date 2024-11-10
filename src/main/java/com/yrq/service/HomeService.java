package com.yrq.service;

import com.yrq.dto.HomeCardDto;

import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-26 20:57
 * @Description:
 */
public interface HomeService {
    public List<HomeCardDto> getHomeStaffInformation(String id);
    public List<HomeCardDto> getHomeGuardInformation(String id);
}
