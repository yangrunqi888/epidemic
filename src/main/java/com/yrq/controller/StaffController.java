package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.StaffSearchDto;
import com.yrq.entity.StaffEntity;
import com.yrq.service.StaffService;
import com.yrq.utils.QueryPageBean;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/staff")
public class StaffController {
    @Resource
    StaffService staffService;
    @Value("${User.portrait-path.staff}")
    String basePath;
    @GetMapping("/selectStaff")
    public R selectStaff(){
        List<StaffEntity> staffs = staffService.list();
        return !staffs.equals(new ArrayList())?R.ok(staffs):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }

    @GetMapping("/selectOneStaff/{id}")
    public R selectOneStaff(@PathVariable String id){
        StaffEntity staff = staffService.getById(id);
        return staff!=null?R.ok(staff):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }
    @PostMapping("/selectPage")
    public R selectPage(@RequestBody StaffSearchDto queryPageBean){
        IPage page = staffService.getPage(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }
    @PostMapping("/insertStaff")
    public R insertStaff(@RequestBody StaffEntity staff){
        boolean save = staffService.save(staff);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
    @PutMapping("/updateStaff")
    public R updateStaff(@RequestBody StaffEntity staff){
        boolean update = staffService.updateById(staff);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }
    @DeleteMapping("/deleteStaff/{id}")
    public R insertStaff(@PathVariable String id){
        boolean delete = staffService.removeById(id);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }

    @PutMapping("/resetStaff/{id}")
    public R resetStaff(@PathVariable String id){
        boolean update = staffService.resetPassword(id);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }
    @GetMapping("/getProhibit/{IdNumber}")
    public void getProhibit(@PathVariable String IdNumber, HttpServletResponse response) throws IOException {
        StaffEntity staff=staffService.getById(IdNumber);
        FileInputStream fileInputStream =new FileInputStream(new File(basePath+staff.getProhibitPath()));
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
    @Transactional
    public R upload(@RequestPart MultipartFile file, @PathVariable String IdNumber) throws IOException {
        String orignPath=staffService.getById(IdNumber).getProhibitPath();
        if(!orignPath.equals("default.jpg")){
            new File(basePath+orignPath).delete();//删除原头像
        }
        //原文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件重复造成文件覆盖
        String filename = UUID.randomUUID().toString()+suffix;

        File file2 = new File(basePath + filename);
        file.transferTo(file2);
        StaffEntity staff=new StaffEntity();
        staff.setEmployeeNumber(IdNumber);
        staff.setProhibitPath(filename);

        boolean update = staffService.updateById(staff);
        return update? R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }
}
