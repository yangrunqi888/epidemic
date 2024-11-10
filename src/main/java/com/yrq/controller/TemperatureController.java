package com.yrq.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.AntigenSearchDto;
import com.yrq.dto.TemperatureSearchDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.TemperatureEntity;
import com.yrq.service.AntigenService;
import com.yrq.service.TemperatureService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author:YangRunqi
 * @create: 2023-02-22 15:14
 * @Description:
 */
@RestController
@RequestMapping("/temperature")
public class TemperatureController {
    @Resource
    TemperatureService temperatureService;
    @PostMapping("/selectTemperature")
    public R selectPage(@RequestBody TemperatureSearchDto queryPageBean){
        IPage page = temperatureService.getlist(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @DeleteMapping("/deleteTemperature")
    public R deleteTemperature(@RequestBody TemperatureEntity entity) {
        boolean delete = temperatureService.remove(entity);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
    @PostMapping ("/insertTemperature")
    public R insertTemperature(@RequestBody TemperatureEntity entity) {
        boolean save = temperatureService.save(entity);
        return save ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }

}
