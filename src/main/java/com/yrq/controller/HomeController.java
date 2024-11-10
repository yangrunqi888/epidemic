package com.yrq.controller;

import com.yrq.dto.HomeCardDto;
import com.yrq.dto.HomeInformationSearchDto;
import com.yrq.service.HomeService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:YangRunqi
 * @create: 2023-02-26 20:51
 * @Description:
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    HomeService homeService;

    @PostMapping ("/getHomeInformation")
    public R getHomeInformation(@RequestBody HomeInformationSearchDto dto){
        List<HomeCardDto> cards=new ArrayList<>();
       switch(dto.getPosition()){
           case "0":case"1":case"2":case"3": cards = homeService.getHomeStaffInformation(dto.getId()); break;
           case "4":case"5": cards=homeService.getHomeGuardInformation(dto.getId()); break;
       }
        return cards.size()>0?R.ok(cards):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,cards);
    }
}
