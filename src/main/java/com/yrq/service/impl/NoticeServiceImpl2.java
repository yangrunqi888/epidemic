package com.yrq.service.impl;

//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.yrq.entity.NoticeEntity;
//import com.yrq.entity.NucleicAcidEntity;
//import com.yrq.mapper.NoticeMapper;
//import com.yrq.mapper.StaffMapper;
//import com.yrq.service.NoticeService;
//import com.yrq.utils.QueryPageBean;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author:YangRunqi
// * @create: 2023-02-28 12:14
// * @Description:
// */
//@Service
//public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {
//    @Resource
//    NoticeMapper noticeMapper;
//    @Resource
//    StaffMapper staffMapper;
//    @Override
//    public IPage getlist(QueryPageBean queryPageBean) {
//        Integer currentPage=queryPageBean.getCurrentPage();
//        Integer pageSize=queryPageBean.getPageSize();
//        Page<NoticeEntity> noticePage = new Page<>(currentPage,pageSize);
//        IPage iPage=null;
//        iPage = noticeMapper.selectPage(noticePage, new LambdaQueryWrapper<NoticeEntity>().orderByDesc(NoticeEntity::getAnnounceTime));
//        setOthers((List<NoticeEntity>) iPage.getRecords());
//        return iPage;
//    }
//
//    private void setOthers(List<NoticeEntity> entities){
//        entities.forEach(entity->{
//            entity.setEmployeeName(staffMapper.selectByIdWithOutLogic(entity.getEmployeeId()).getName());
//        });
//    }
//}
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrq.entity.NoticeEntity;
import com.yrq.entity.NoticeEntity2;
import com.yrq.mapper.NoticeRepository;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.NoticeService;
import com.yrq.service.NoticeService2;
import com.yrq.utils.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl2 implements NoticeService2 {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private StaffMapper staffMapper;

    @Override
    public IPage<NoticeEntity2> getlist(QueryPageBean queryPageBean) {
        // 获取当前页和每页条数
        int currentPage = queryPageBean.getCurrentPage() - 1; // 注意：Spring Data 页码从0开始
        int pageSize = queryPageBean.getPageSize();

        // 创建分页请求，按照 announceTime 降序排列
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "announceTime"));

        // 从 Elasticsearch 查询公告数据
        org.springframework.data.domain.Page<NoticeEntity2> noticePage = noticeRepository.findAll(pageable);

        // 转换 Elasticsearch 返回的 Page 为 MyBatis 的 IPage
        IPage<NoticeEntity2> iPage = new Page<>(queryPageBean.getCurrentPage(), pageSize, noticePage.getTotalElements());
        iPage.setRecords(noticePage.getContent());

        // 设置公告发布者名称
        setOthers(iPage.getRecords());

        return iPage;
    }

    private void setOthers(List<NoticeEntity2> entities) {
        entities.forEach(entity -> {
            entity.setEmployeeName(staffMapper.selectByIdWithOutLogic(entity.getEmployee_id()).getName());
        });
    }
}