package com.yrq.controller;

import com.yrq.entity.GridManageEntity;
import com.yrq.mapper.GridManageMapper;
import com.yrq.service.GridManageService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/gridManage")
public class GridManageController {
    @Resource
    GridManageService gridManageService;

    @GetMapping("/selectByGrid")
    public R selectByGrid(@PathVariable String id){
        List<GridManageEntity> list = gridManageService.selectByGrid(id);
        return !list.equals(new ArrayList())?R.ok(list):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }
    @GetMapping("/selectByEmployee/{id}")
    public R selectByEmployee(@PathVariable String id){
        List<String> list = gridManageService.selectByEmployee(id);
        return !list.equals(new ArrayList())?R.ok(list):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }
    @PostMapping("/insertManage")
    public R insertManage(@RequestBody GridManageEntity gridManageEntity){
        boolean save = gridManageService.save(gridManageEntity);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @DeleteMapping("/deleteManage")
    public R deleteManage(@RequestBody GridManageEntity gridManageEntity){
        boolean delete = gridManageService.delete(gridManageEntity);
        return delete?R.ok():R.error(RHttpStatusEnum.DELETE_FAILED);
    }
}
