package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.OutSearchDto;
import com.yrq.entity.GridEntity;
import com.yrq.entity.OutEntity;
import com.yrq.service.OutService;
import com.yrq.utils.QueryPageBean;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/out")
public class OutController {
    @Resource
    OutService outService;
    @PostMapping("/selectOut")
    public R selectOut(@RequestBody OutSearchDto queryPageBean){
        IPage page = outService.getPage(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @PostMapping("/insertOut")
    public R insertOut(@RequestBody OutEntity out){
        out.setOutTime(new Date());
        boolean save = outService.save(out);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PutMapping("/updateOut")
    public R updateGrid(@RequestBody OutEntity out){
        out.setBackTime(new Date());
        out.setState("1");
        double diff=(out.getBackTime().getTime()-out.getOutTime().getTime())/60000.0;
        out.setOutTotalTime(diff);
        if(out.getOutGuardNumber().equals("admin")){
            out.setOutGuardNumber(null);
        }
        boolean update = outService.update(out);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }

    @DeleteMapping("/deleteOut")
    public R deleteGrid(@RequestBody OutEntity out) {
        boolean delete = outService.delete(out);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
}
