package com.yrq.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrq.dto.BackHomeSearchDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.BackHomeEntity;
import com.yrq.entity.NucleicAcidEntity;
import com.yrq.service.BackHomeService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author:YangRunqi
 * @create: 2023-02-23 10:42
 * @Description:
 */
@RestController
@RequestMapping("/backHome")
public class BackHomeController {
    @Resource
    BackHomeService backHomeService;
    @Value("${User.backHome.health}")
    String healthBasePath;
    @Value("${User.backHome.travel}")
    String travelBasePath;
    @PostMapping("/selectBackHome")
    public R selectPage(@RequestBody BackHomeSearchDto queryPageBean){
        IPage page = backHomeService.getlist(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @PostMapping("/insertBackHome")
    public R insertBackHome( @RequestPart String entity, @RequestPart MultipartFile healthFile,@RequestPart MultipartFile travelFile) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BackHomeEntity backHomeEntity = objectMapper.readValue(entity, BackHomeEntity.class);

        //原文件名
        String healthOriginalFilename = healthFile.getOriginalFilename();
        String travelOriginalFilename = travelFile.getOriginalFilename();
        String healthSuffix = healthOriginalFilename.substring(healthOriginalFilename.lastIndexOf("."));
        String travelSuffix=travelOriginalFilename.substring(travelOriginalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件重复造成文件覆盖
        String healthFilename = UUID.randomUUID().toString()+healthSuffix;
        String travelFilename = UUID.randomUUID().toString()+travelSuffix;
        File file2 = new File(healthBasePath + healthFilename);
        healthFile.transferTo(file2);
        backHomeEntity.setHealthCodePath(healthFilename);
        file2 = new File(travelBasePath + travelFilename);
        travelFile.transferTo(file2);
        backHomeEntity.setTravelCodePath(travelFilename);

        boolean insert = backHomeService.save(backHomeEntity);

        return insert?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PutMapping("/verify")
    public R verify(@RequestBody BackHomeEntity entity){
        boolean update = backHomeService.verify(entity);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }
    @DeleteMapping("/deleteBack")
    public R deleteBack(@RequestBody BackHomeEntity entity){
        String orignPath= entity.getTravelCodePath();
        new File(travelBasePath+orignPath).delete();//删除图片
        orignPath= entity.getHealthCodePath();
        new File(healthBasePath+orignPath).delete();//删除图片
        boolean delete = backHomeService.remove(entity);
        return delete?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }

    @PostMapping("/getHealthPicture")
    public void getHealthPicture(@RequestBody BackHomeEntity back, HttpServletResponse response) throws IOException {
        BackHomeEntity entity = backHomeService.getOne(new LambdaQueryWrapper<BackHomeEntity>()
                .eq(BackHomeEntity::getIdNumber, back.getIdNumber())
                .eq(BackHomeEntity::getBackDate, back.getBackDate()));
        FileInputStream fileInputStream =new FileInputStream(new File(healthBasePath+entity.getHealthCodePath()));
        ServletOutputStream outputStream= response.getOutputStream();
        response.setContentType("image/jpeg");
        int len=0;
        byte[] bytes=new byte[1024];
        while ((len=fileInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        //关闭资源
        outputStream.close();
        fileInputStream.close();
    }
    @PostMapping("/getTravelPicture")
    public void getTravelPicture(@RequestBody BackHomeEntity back, HttpServletResponse response) throws IOException {
        BackHomeEntity entity = backHomeService.getOne(new LambdaQueryWrapper<BackHomeEntity>()
                .eq(BackHomeEntity::getIdNumber, back.getIdNumber())
                .eq(BackHomeEntity::getBackDate, back.getBackDate()));
        FileInputStream fileInputStream =new FileInputStream(new File(travelBasePath+entity.getTravelCodePath()));
        ServletOutputStream outputStream= response.getOutputStream();
        response.setContentType("image/jpeg");
        int len=0;
        byte[] bytes=new byte[1024];
        while ((len=fileInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        //关闭资源
        outputStream.close();
        fileInputStream.close();
    }
}
