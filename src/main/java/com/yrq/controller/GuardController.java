package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.GuardSearchDto;
import com.yrq.entity.GuardEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.service.GuardService;
import com.yrq.service.StaffService;
import com.yrq.utils.QueryPageBean;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/guard")
public class GuardController {
    @Resource
    GuardService guardService;
    @Value("${User.portrait-path.guard}")
    String basePath;
    @GetMapping("/selectGuard")
    public R selectGuard(){
        List<GuardEntity> guards = guardService.list();
        return !guards.equals(new ArrayList())?R.ok(guards):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }

    @GetMapping("/selectOneGuard/{id}")
    public R selectOneGuard(@PathVariable String id){
        GuardEntity guard = guardService.getById(id);
        return guard!=null?R.ok(guard):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }
    @PostMapping("/selectPage")
    public R selectPage(@RequestBody GuardSearchDto queryPageBean){
        IPage page = guardService.getPage(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @PostMapping("/insertGuard")
    public R insertGuard(@RequestBody GuardEntity guard){
        boolean save = guardService.save(guard);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PutMapping("/updateGuard")
    public R updateGuard(@RequestBody GuardEntity guard){
        boolean update = guardService.updateById(guard);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }
    @DeleteMapping("/deleteGuard/{id}")
    public R insertGuard(@PathVariable String id){
        boolean delete = guardService.removeById(id);

        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }

    @PutMapping("/resetGuard/{id}")
    public R resetGuard(@PathVariable String id){
        boolean update = guardService.resetPassword(id);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }
    @GetMapping("/getProhibit/{IdNumber}")
    public void getProhibit(@PathVariable String IdNumber, HttpServletResponse response) throws IOException {
        GuardEntity guard=guardService.getById(IdNumber);
        FileInputStream fileInputStream =new FileInputStream(basePath+new File(guard.getProhibitPath()));
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
    @PostMapping("/upload/{IdNumber}")
    public R upload(@RequestPart MultipartFile file, @PathVariable String IdNumber) throws IOException {
        //原文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件重复造成文件覆盖
        String filename = UUID.randomUUID().toString()+suffix;

        File file2 = new File(basePath + filename);
        file.transferTo(file2);

        GuardEntity guard=new GuardEntity();
        guard.setGuardNumber(IdNumber);
        guard.setProhibitPath(filename);
        String orignPath=guardService.getById(IdNumber).getProhibitPath();
        boolean update = guardService.updateById(guard);
        if(!orignPath.equals("default.jpg")){
            new File(basePath+orignPath).delete();//删除原头像
        }
        return update? R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
}
