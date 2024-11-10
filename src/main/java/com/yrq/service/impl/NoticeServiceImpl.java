package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.entity.NoticeEntity;
import com.yrq.entity.NucleicAcidEntity;
import com.yrq.mapper.NoticeMapper;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.NoticeService;
import com.yrq.utils.QueryPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-28 12:14
 * @Description:
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {
    @Resource
    NoticeMapper noticeMapper;
    @Resource
    StaffMapper staffMapper;
    @Override
    public IPage getlist(QueryPageBean queryPageBean) {
        Integer currentPage=queryPageBean.getCurrentPage();
        Integer pageSize=queryPageBean.getPageSize();
        Page<NoticeEntity> noticePage = new Page<>(currentPage,pageSize);
        IPage iPage=null;
        iPage = noticeMapper.selectPage(noticePage, new LambdaQueryWrapper<NoticeEntity>().orderByDesc(NoticeEntity::getAnnounceTime));
        setOthers((List<NoticeEntity>) iPage.getRecords());
        return iPage;
    }

    private void setOthers(List<NoticeEntity> entities){
        entities.forEach(entity->{
            entity.setEmployeeName(staffMapper.selectByIdWithOutLogic(entity.getEmployeeId()).getName());
        });
    }
}
