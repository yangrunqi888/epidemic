package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yrq.dto.MenuDto;
import com.yrq.entity.MenuEntity;
import com.yrq.mapper.AuthorityMapper;
import com.yrq.mapper.MenuMapper;
import com.yrq.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author:YangRunqi
 * @create: 2023-02-17 20:42
 * @Description:
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    AuthorityMapper authorityMapper;
    @Resource
    MenuMapper menuMapper;
    @Override
    public List<MenuDto> getMenus(String groupId) {
        List<String> menuIds = authorityMapper.getMenus(groupId);
        LambdaQueryWrapper<MenuEntity> qw=new LambdaQueryWrapper<>();
        qw.in(MenuEntity::getMenuId,menuIds);
        List<MenuEntity> menus = menuMapper.selectList(qw);

        List<MenuDto> menuDtoList=new ArrayList<>();
        menus.forEach(menu ->{
            if(menu.getParentId()!=null){
                MenuEntity parentMenu = menuMapper.selectById(menu.getParentId());
                Optional<MenuDto> parent = menuDtoList.stream().filter(item -> item.getMenuId().equals(parentMenu.getMenuId())).findFirst();
                if(parent.isPresent()){
                    parent.get().getChildren().add(convertToDto(menu));
                }
                else {
                    MenuDto menuDto =convertToDto(parentMenu);
                    menuDto.setChildren(new ArrayList<>());
                    menuDto.getChildren().add(convertToDto(menu));
                    menuDtoList.add(menuDto);
                }
            }
            else{
                MenuDto menuDto=convertToDto(menu);
                menuDtoList.add(menuDto);
            }
        } );
        return menuDtoList;
    }
    public MenuDto convertToDto(MenuEntity menu){
        MenuDto menuDto=new MenuDto();
        menuDto.setPath(menu.getPath());
        menuDto.setName(menu.getName());
        menuDto.setIcon(menu.getIcon());
        menuDto.setLabel(menu.getLabel());
        menuDto.setUrl(menu.getUrl());
        menuDto.setMenuId(menu.getMenuId());
        return menuDto;
    }
}
