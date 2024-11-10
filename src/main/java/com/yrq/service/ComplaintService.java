package com.yrq.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrq.dto.ComplaintSearchDto;
import com.yrq.dto.QuarantineDto;
import com.yrq.entity.ComplaintEntity;

public interface ComplaintService extends IService<ComplaintEntity> {
    public IPage getlist(ComplaintSearchDto complaintSearchDto);
}
