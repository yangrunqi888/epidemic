package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.VisitSearchDto;
import com.yrq.entity.OutEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.entity.VisitEntity;
import com.yrq.service.VisitService;
import com.yrq.utils.QueryPageBean;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/visit")
public class VisitController {
    @Resource
    VisitService visitService;
    @GetMapping ("/getRecords/{id}")
    public R selectPage(@PathVariable String id){
        String records = visitService.getVisitRecords(id);
        records=records.replaceAll("\n", "<br><br>");
        return !records.equals("")?R.ok(records):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,records);
    }
    @PostMapping ("/selectVisit")
    public R selectPage(@RequestBody VisitSearchDto queryPageBean){
        IPage page = visitService.getPage(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @PostMapping("/insertVisit")
    public R insertVisit(@RequestBody VisitEntity visit){
        boolean save = visitService.save(visit);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PutMapping("/updateVisit")
    public R updateVisit(@RequestBody VisitEntity visit){
        if(visit.getInGuardNumber().equals("admin")){
        visit.setInGuardNumber(null);
    }
        boolean update = visitService.update(visit);

        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }
    @DeleteMapping("/deleteVisit")
    public R deleteVisit(@RequestBody VisitEntity visit) {
        boolean delete = visitService.remove(visit);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
}
