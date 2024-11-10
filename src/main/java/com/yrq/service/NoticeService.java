package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.AntigenSearchDto;
import com.yrq.dto.QuarantineDto;
import com.yrq.entity.NoticeEntity;
import com.yrq.utils.QueryPageBean;

public interface NoticeService extends IService<NoticeEntity> {
    public IPage<NoticeEntity> getlist(QueryPageBean queryPageBean);
}
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.yrq.entity.NoticeEntity;
//import com.yrq.utils.QueryPageBean;
//
//public interface NoticeService {
//    IPage<NoticeEntity> getlist(QueryPageBean queryPageBean);
//}