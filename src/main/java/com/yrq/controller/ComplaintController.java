package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.BackHomeSearchDto;
import com.yrq.dto.ComplaintSearchDto;
import com.yrq.entity.BackHomeEntity;
import com.yrq.entity.ComplaintEntity;
import com.yrq.entity.QuarantineEntity;
import com.yrq.service.ComplaintService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author:YangRunqi
 * @create: 2023-02-25 12:24
 * @Description:
 */
@RestController
@RequestMapping("/complaint")
public class ComplaintController {
    @Resource
    ComplaintService complaintService;
    @PostMapping("/selectComplaint")
    public R selectPage(@RequestBody ComplaintSearchDto queryPageBean){
        IPage page = complaintService.getlist(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @PostMapping("/insertComplaint")
    public R insertResident(@RequestBody ComplaintEntity entity){
        entity.setComplaintTime(new Date());
        boolean save = complaintService.save(entity);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @DeleteMapping("/deleteComplaint/{id}")
    public R deleteComplaint(@PathVariable String id){
        boolean delete = complaintService.removeById(id);
        return delete?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PutMapping("/copeComplaint")
    public R verify(@RequestBody ComplaintEntity entity){
        entity.setHandleTime(new Date());
        entity.setState("1");
        boolean update = complaintService.updateById(entity);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }
    @PutMapping("/commentComplaint")
    public R commentComplaint(@RequestBody ComplaintEntity entity){
        boolean update = complaintService.updateById(entity);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }
}
