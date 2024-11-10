package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.AntigenSearchDto;
import com.yrq.entity.GuardEntity;
import com.yrq.entity.NoticeEntity;
import com.yrq.service.NoticeService;
import com.yrq.service.NoticeService2;
import com.yrq.utils.QueryPageBean;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author:YangRunqi
 * @create: 2023-02-28 12:16
 * @Description:
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    NoticeService noticeService;
    @Resource
    NoticeService2 noticeService2;
    @PostMapping("/selectNotice")
    public R selectPage(@RequestBody QueryPageBean queryPageBean){
        IPage page = noticeService2.getlist(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @PostMapping("/insertNotice")
    public R insertGuard(@RequestBody NoticeEntity notice){
        notice.setAnnounceTime(new Date());
        boolean save = noticeService.save(notice);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @DeleteMapping("/deleteNotice/{id}")
    public R insertGuard(@PathVariable String id){
        boolean delete = noticeService.removeById(id);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
}
