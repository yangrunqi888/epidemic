package com.yrq.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.entity.NoticeEntity;
import com.yrq.entity.NoticeEntity2;
import com.yrq.utils.QueryPageBean;

public interface NoticeService2 {
    IPage<NoticeEntity2> getlist(QueryPageBean queryPageBean);
}
