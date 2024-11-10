package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.AntigenSearchDto;
import com.yrq.dto.QuarantineDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.NucleicAcidEntity;
import com.yrq.entity.QuarantineEntity;
import com.yrq.service.AntigenService;
import com.yrq.service.QuarantineService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author:YangRunqi
 * @create: 2023-02-24 13:27
 * @Description:
 */
@RestController
@RequestMapping("/quarantine")
public class QuarantineController {
    @Resource
    QuarantineService quarantineService;
    @PostMapping("/selectQuarantine")
    public R selectPage(@RequestBody QuarantineDto queryPageBean){
        IPage page = quarantineService.getlist(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @PostMapping("/insertQuarantine")
    public R insertResident(@RequestBody QuarantineEntity entity){
        boolean save = quarantineService.save(entity);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @DeleteMapping("/deleteQuarantine")
    public R deleteResident(@RequestBody QuarantineEntity entity) {
        boolean delete = quarantineService.remove(entity);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
}
