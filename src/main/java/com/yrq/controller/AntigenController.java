package com.yrq.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrq.dto.AntigenSearchDto;
import com.yrq.dto.NucleicAcidDto;
import com.yrq.entity.AntigenEntity;
import com.yrq.entity.NucleicAcidEntity;
import com.yrq.service.AntigenService;
import com.yrq.service.NucleicAcidService;
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
 * @create: 2023-02-22 15:14
 * @Description:
 */
@RestController
@RequestMapping("/antigen")
public class AntigenController {
    @Resource
    AntigenService antigenService;
    @Value("${User.antigen}")
    String basePath;
    @PostMapping("/selectAntigen")
    public R selectPage(@RequestBody AntigenSearchDto queryPageBean){
        IPage page = antigenService.getlist(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @DeleteMapping("/deleteAntigen")
    public R deleteResident(@RequestBody AntigenEntity entity) {
        String orignPath= entity.getPicturePath();
        new File(basePath+orignPath).delete();//删除图片
        boolean delete = antigenService.remove(entity);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
    @PostMapping("/insertAntigen")
    public R insertAntigen( @RequestPart String entity, @RequestPart MultipartFile file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AntigenEntity antigenEntity = objectMapper.readValue(entity, AntigenEntity.class);

        //原文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件重复造成文件覆盖
        String filename = UUID.randomUUID().toString()+suffix;

        File file2 = new File(basePath + filename);
        file.transferTo(file2);
        antigenEntity.setPicturePath(filename);

        boolean insert = antigenService.save(antigenEntity);

        return insert?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PostMapping("/getAntigenPicture")
    public void getAcidPicture(@RequestBody AntigenEntity antigen, HttpServletResponse response) throws IOException {
        AntigenEntity entity = antigenService.getOne(new LambdaQueryWrapper<AntigenEntity>()
                .eq(AntigenEntity::getIdNumber, antigen.getIdNumber())
                .eq(AntigenEntity::getCollectTime, antigen.getCollectTime()));
        FileInputStream fileInputStream =new FileInputStream(new File(basePath+entity.getPicturePath()));
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
