package com.yrq.controller;

import com.yrq.dto.LiveDto;
import com.yrq.entity.GuardEntity;
import com.yrq.service.LiveService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-21 14:37
 * @Description:
 */
@RestController
@RequestMapping("/live")
public class LiveController {
    @Resource
    LiveService liveService;
    @GetMapping("/selectLiving/{id}")
    public R selectGuard(@PathVariable String id){
        List<LiveDto> lives = liveService.getResidentByBuilding(id);
        return !lives.equals(new ArrayList())?R.ok(lives):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }
}
