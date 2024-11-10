package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.ComplaintSearchDto;
import com.yrq.entity.BackHomeEntity;
import com.yrq.entity.ComplaintEntity;
import com.yrq.entity.QuarantineEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.mapper.ComplaintMapper;
import com.yrq.mapper.QuarantineMapper;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.ComplaintService;
import com.yrq.service.ResidentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-25 12:01
 * @Description:
 */
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, ComplaintEntity> implements ComplaintService {

    @Resource
    ResidentService residentService;
    @Resource
    ComplaintMapper complaintMapper;
    @Resource
    StaffMapper staffMapper;
    @Override
    public IPage getlist(ComplaintSearchDto complaintSearchDto) {
        //设置分页
        Integer currentPage = complaintSearchDto.getPagination().getCurrentPage();
        Integer pageSize = complaintSearchDto.getPagination().getPageSize();
        Page<ComplaintEntity> complaintEntityPage = new Page<>(currentPage, pageSize);
        IPage iPage = null;
        List<String> residentNumbers = new ArrayList<>();
        residentService.getlist(complaintSearchDto.getPagination().getQueryString().toString()).forEach(resident -> residentNumbers.add(resident.getIdNumber()));
        if (residentNumbers.size() == 0) {
            return new Page();
        }
        LambdaQueryWrapper<ComplaintEntity> qw = new LambdaQueryWrapper<>();
        qw.in(ComplaintEntity::getComplaintId,residentNumbers);
        if(complaintSearchDto.getType()!=null&&!complaintSearchDto.getType().equals(""))
            qw.eq(ComplaintEntity::getType,complaintSearchDto.getType());
        if(complaintSearchDto.getTitle()!=null && !complaintSearchDto.getTitle().equals(""))
            qw.like(ComplaintEntity::getTitle,complaintSearchDto.getTitle());
        if(complaintSearchDto.getComplaintTime().size() > 0)
            qw.between(ComplaintEntity::getComplaintTime,complaintSearchDto.getComplaintTime().get(0),complaintSearchDto.getComplaintTime().get(1));
        if(complaintSearchDto.getHandleTime().size() > 0)
            qw.between(ComplaintEntity::getHandleTime,complaintSearchDto.getHandleTime().get(0),complaintSearchDto.getHandleTime().get(1));
        if(complaintSearchDto.getEmployeeId()!=null&&!complaintSearchDto.getEmployeeId().equals(""))
            qw.like(ComplaintEntity::getId,complaintSearchDto.getEmployeeId());
        if(complaintSearchDto.getEmployeeName()!=null&&!complaintSearchDto.getEmployeeName().equals("")){
            if(complaintSearchDto.getEmployeeName()=="匿名"){
                qw.eq(ComplaintEntity::getAnonymous,1);
            }
            else {
                List<String> staffs=new ArrayList<>();
                staffMapper.selectList(new LambdaQueryWrapper<StaffEntity>().like(StaffEntity::getName,complaintSearchDto.getEmployeeName()))
                        .forEach(staff->staffs.add(staff.getEmployeeNumber()));
                if(staffs.size()==0)
                    return new Page();
                qw.in(ComplaintEntity::getEmployeeId,staffs);
            }
        }
        if(!complaintSearchDto.getState().equals(""))
            qw.eq(ComplaintEntity::getState,complaintSearchDto.getState());
        if(complaintSearchDto.getIdNumber()!=null&&!complaintSearchDto.getIdNumber().equals(""))
            qw.eq(ComplaintEntity::getComplaintId,complaintSearchDto.getIdNumber());
        qw.orderByDesc(ComplaintEntity::getComplaintTime);
        iPage=complaintMapper.selectPage(complaintEntityPage,qw);
        setOthers((List<ComplaintEntity>) iPage.getRecords());
        return iPage;
    }

    private void setOthers(List<ComplaintEntity> complaints) {
        complaints.forEach(complaint->{
            if(complaint.getAnonymous().equals(0))
                complaint.setComplaintName(residentService.getById(complaint.getComplaintId()).getName());
            else {
                complaint.setComplaintName("匿名");
                complaint.setComplaintId(null);
            }
            if(complaint.getEmployeeId()!=null)
                complaint.setEmployeeName(staffMapper.selectByIdWithOutLogic(complaint.getEmployeeId()).getName());
            if(complaint.getType().equals("0"))
                complaint.setType("建议");
            else if (complaint.getType().equals("1"))
                complaint.setType("投诉");
            if(complaint.getState().equals("0"))
                complaint.setState("未处理");
            else if (complaint.getState().equals("1"))
                complaint.setState("已处理");
        });
    }


}
