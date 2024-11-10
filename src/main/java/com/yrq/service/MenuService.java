package com.yrq.service;

import com.yrq.dto.MenuDto;

import java.util.List;

public interface MenuService {
    public List<MenuDto> getMenus(String groupId);
}
