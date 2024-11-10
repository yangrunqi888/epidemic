package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.GridSearchDto;
import com.yrq.entity.BuildingEntity;
import com.yrq.entity.GridEntity;
import com.yrq.service.BuildingService;
import com.yrq.service.GridService;
import com.yrq.utils.QueryPageBean;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/grid")
public class GridController {
    @Resource
    GridService gridService;

    @GetMapping("/selectGrid")
    public R selectGrid(){
        List<GridEntity> grids = gridService.list();
        return !grids.equals(new ArrayList())?R.ok(grids):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }

    @GetMapping ("/selectOneGrid/{id}")
    public R selectOneGrid(@PathVariable String id){
        GridEntity grid = gridService.getById(id);
        return grid!=null?R.ok(grid):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }

    @PostMapping("/selectPage")
    public R selectPage(@RequestBody GridSearchDto queryPageBean){
        IPage page = gridService.getPage(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }

    @PostMapping("/insertGrid")
    public R insertGrid(@RequestBody GridEntity grid){
        boolean save = gridService.save(grid);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PutMapping("/updateGrid")
    public R updateGrid(@RequestBody GridEntity grid){
        boolean update = gridService.update(grid);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }

    @DeleteMapping("/deleteGrid/{id}")
    public R deleteGrid(@PathVariable String id) {
        boolean delete = gridService.removeById(id);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
}
