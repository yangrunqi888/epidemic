package com.yrq.service.impl;

import com.yrq.dto.*;
import com.yrq.entity.StaffEntity;
import com.yrq.service.*;
import com.yrq.utils.QueryPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-26 20:57
 * @Description:
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    ResidentService residentService;
    @Resource
    BuildingService buildingService;
    @Resource
    NucleicAcidService nucleicAcidService;
    @Resource
    AntigenService antigenService;
    @Resource
    TemperatureService temperatureService;
    @Resource
    BackHomeService backHomeService;
    @Resource
    OutService outService;
    @Resource
    VisitService visitService;
    @Override
    public List<HomeCardDto> getHomeStaffInformation(String id) {
        List<HomeCardDto> cards=new ArrayList<>();

        QueryPageBean pageBean=new QueryPageBean();
        pageBean.setCurrentPage(1);
        pageBean.setPageSize(10);
        pageBean.setQueryString(id);

        // 获取当前时间
        Date now = new Date();
        // 创建一个 Calendar 实例
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        // 设置时间为当天的 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();
        // 设置时间为当天的 23:59:59
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date todayEnd = calendar.getTime();

        cards.add(new HomeCardDto("网格内居民人数",residentService.getlist(id).size(),"user-solid","#2ec7c9"));

        List state=new ArrayList<String>();
        NucleicAcidDto nucleicAcidDto=new NucleicAcidDto();
        nucleicAcidDto.setPagination(pageBean);
        nucleicAcidDto.setCollectStartTime(todayStart);
        nucleicAcidDto.setCollectEndTime(todayEnd);
        state.clear();
        state.add("0");
        state.add("1");
        nucleicAcidDto.setResult(state);
        cards.add(new HomeCardDto("今日提交核酸人数",(int) nucleicAcidService.getlist(nucleicAcidDto).getTotal(),"s-promotion","#5ab1ef"));

        TemperatureSearchDto temperatureSearchDto=new TemperatureSearchDto();
        temperatureSearchDto.setPagination(pageBean);
        temperatureSearchDto.setCollectStartTime(todayStart);
        temperatureSearchDto.setCollectEndTime(todayEnd);
        cards.add(new HomeCardDto("今日提交体温人数",(int) temperatureService.getlist(temperatureSearchDto).getTotal(),"s-order","#ffb980"));

        cards.add(new HomeCardDto("网格内楼栋数",buildingService.list(id).size(),"location","#2ec7c9"));

        AntigenSearchDto antigenSearchDto=new AntigenSearchDto();
        antigenSearchDto.setPagination(pageBean);
        antigenSearchDto.setCollectStartTime(todayStart);
        antigenSearchDto.setCollectEndTime(todayEnd);
        cards.add(new HomeCardDto("今日提交抗原人数",(int) antigenService.getlist(antigenSearchDto).getTotal(),"check","#5ab1ef"));


        BackHomeSearchDto backHomeSearchDto=new BackHomeSearchDto();
        backHomeSearchDto.setPagination(pageBean);
        backHomeSearchDto.setStartBackDate(todayStart);
        backHomeSearchDto.setEndBackDate(todayEnd);
        state.clear();
        state.add("1");
        backHomeSearchDto.setState(state);
        cards.add(new HomeCardDto("今日返乡人数",(int) backHomeService.getlist(backHomeSearchDto).getTotal(),"s-flag","#ffb980"));
        return cards;
    }

    @Override
    public List<HomeCardDto> getHomeGuardInformation(String id) {
        List<HomeCardDto> cards=new ArrayList<>();

        QueryPageBean pageBean=new QueryPageBean();
        pageBean.setCurrentPage(1);
        pageBean.setPageSize(10);

        // 获取当前时间
        Date now = new Date();
        // 创建一个 Calendar 实例
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        // 设置时间为当天的 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();
        // 设置时间为当天的 23:59:59
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date todayEnd = calendar.getTime();

        List<Date> range=new ArrayList<>();
        range.add(todayStart);
        range.add(todayEnd);

        OutSearchDto outSearchDto=new OutSearchDto();
        outSearchDto.setPagination(pageBean);
        outSearchDto.setOutTime(range);

        cards.add(new HomeCardDto("今日居民外出人数",(int) outService.getPage(outSearchDto).getTotal(),"user-solid","#2ec7c9"));

        VisitSearchDto visitSearchDto=new VisitSearchDto();
        visitSearchDto.setPagination(pageBean);
        visitSearchDto.setEntryTime(range);
        cards.add(new HomeCardDto("今日访客人数",(int) visitService.getPage(visitSearchDto).getTotal(),"s-promotion","#5ab1ef"));
        return cards;
    }
}
