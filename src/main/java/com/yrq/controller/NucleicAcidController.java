package com.yrq.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrq.dto.BuildingSearchDto;
import com.yrq.dto.NucleicAcidDto;
import com.yrq.entity.NucleicAcidEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.service.NucleicAcidService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author:YangRunqi
 * @create: 2023-02-21 18:31
 * @Description:
 */
@RestController
@RequestMapping("/nucleic")
public class NucleicAcidController {
    @Resource
    NucleicAcidService nucleicAcidService;
    @Value("${User.acid}")
    String basePath;
    @PostMapping("/selectNucleic")
    public R selectPage(@RequestBody NucleicAcidDto queryPageBean){
        IPage page = nucleicAcidService.getlist(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @PostMapping("/insertNucleic")
    public R insertNucleic(@RequestBody NucleicAcidEntity entity){
        boolean save = nucleicAcidService.save(entity);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PutMapping("/uploadNucleic")
    public R uploadNucleic( @RequestPart String entity, @RequestPart MultipartFile file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        NucleicAcidEntity nucleicAcid = objectMapper.readValue(entity, NucleicAcidEntity.class);

        //原文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件重复造成文件覆盖
        String filename = UUID.randomUUID().toString()+suffix;

        File file2 = new File(basePath + filename);
        file.transferTo(file2);
        nucleicAcid.setPicturePath(filename);

        boolean update = nucleicAcidService.update(nucleicAcid);

        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }

    @DeleteMapping("/deleteNucleic")
    public R deleteNucleic(@RequestBody NucleicAcidEntity entity) {
        boolean delete = nucleicAcidService.remove(entity);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
    @PutMapping("/revokeAcid")
    public R revokeAcid(@RequestBody NucleicAcidEntity entity) {
        String orignPath= entity.getPicturePath();
        new File(basePath+orignPath).delete();//删除原截图
        entity.setPicturePath(null);
        entity.setResult("2");
        boolean update = nucleicAcidService.update(entity);
        return update ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
    @PostMapping("/getAcidPicture")
    public void getAcidPicture(@RequestBody NucleicAcidEntity acid,HttpServletResponse response) throws IOException {
        NucleicAcidEntity entity = nucleicAcidService.getOne(new LambdaQueryWrapper<NucleicAcidEntity>()
                .eq(NucleicAcidEntity::getIdNumber, acid.getIdNumber())
                .eq(NucleicAcidEntity::getCollectTime, acid.getCollectTime()));


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
