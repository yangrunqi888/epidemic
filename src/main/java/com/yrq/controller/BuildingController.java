package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.BuildingSearchDto;
import com.yrq.entity.ResidentEntity;
import com.yrq.service.BuildingService;
import com.yrq.utils.QueryPageBean;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;
import com.yrq.entity.BuildingEntity;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/building")
public class BuildingController {
    @Resource
    BuildingService buildingService;

    @GetMapping("/selectBuilding/{id}")
    public R selectBuilding(@PathVariable String id){
        List<BuildingEntity> buildings = buildingService.list(id);
        return !buildings.equals(new ArrayList())?R.ok(buildings):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }

    @GetMapping("/selectOneBuilding/{id}")
    public R selectOneBuilding(@PathVariable String id){
        BuildingEntity building = buildingService.getById(id);
        return building!=null?R.ok(building):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }
    @PostMapping("/selectPage")
    public R selectPage(@RequestBody BuildingSearchDto queryPageBean){
        IPage page = buildingService.getPage(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }

    @PostMapping("/insertBuilding")
    public R insertResident(@RequestBody BuildingEntity building){
        boolean save = buildingService.save(building);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PutMapping("/updateBuilding")
    public R updateResident(@RequestBody BuildingEntity building){
        boolean update = buildingService.updateById(building);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }

    @DeleteMapping("/deleteBuilding/{id}")
    public R deleteResident(@PathVariable String id) {
        boolean delete = buildingService.removeById(id);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
}
