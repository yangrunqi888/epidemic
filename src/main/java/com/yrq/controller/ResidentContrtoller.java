package com.yrq.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrq.dto.ResidentSearchDto;
import com.yrq.entity.ResidentEntity;
import com.yrq.service.ResidentService;
import com.yrq.utils.QueryPageBean;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.crypto.spec.OAEPParameterSpec;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resident")
public class ResidentContrtoller {

    @Resource
    ResidentService residentService;
    @Value("${User.portrait-path.resident}")
    String basePath;

    @GetMapping("/selectResident/{id}")
    public R selectResident(@PathVariable String id){

        List<ResidentEntity> residents = residentService.getlist(id);
        return residents!=null?R.ok(residents):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }

    @GetMapping("/selectOneResident/{id}")
    public R selectOneResident(@PathVariable String id){
        ResidentEntity resident = residentService.getById(id);

        return resident!=null?R.ok(resident):R.error(RHttpStatusEnum.HTTP_NOT_FOUND);
    }

    @PostMapping("/selectPage")
    public R selectPage(@RequestBody ResidentSearchDto queryPageBean){
        IPage page = residentService.getPage(queryPageBean);
        return !page.getRecords().equals(new ArrayList())?R.ok(page):R.error(RHttpStatusEnum.HTTP_NOT_FOUND,page);
    }

    @PostMapping("/insertResident")
    public R insertResident(@RequestBody ResidentEntity resident){
        boolean save = residentService.save(resident);
        return save?R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }

    @PutMapping("/resetResident/{id}")
    public R resetResident(@PathVariable String id){
        boolean update = residentService.resetPassword(id);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }

    @PutMapping("/updateResident")
    public R updateResident(@RequestBody ResidentEntity resident){
        boolean update = residentService.updateById(resident);
        return update?R.ok():R.error(RHttpStatusEnum.UPDATE_FAILED);
    }

    @DeleteMapping("/deleteResident/{id}")
    public R deleteResident(@PathVariable String id) {
        boolean delete = residentService.removeById(id);
        return delete ? R.ok() :R.error(RHttpStatusEnum.DELETE_FAILED);
    }
    @GetMapping("/getProhibit/{IdNumber}")
    public void getProhibut(@PathVariable String IdNumber, HttpServletResponse response) throws IOException {
        ResidentEntity resident=residentService.getById(IdNumber);
        FileInputStream fileInputStream =new FileInputStream(new File(basePath+resident.getProhibitPath()));
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

        ResidentEntity resident=new ResidentEntity();
        resident.setIdNumber(IdNumber);
        resident.setProhibitPath(filename);
        String orignPath=residentService.getById(IdNumber).getProhibitPath();
        boolean update = residentService.updateById(resident);
        if(!orignPath.equals("default.jpg")){
            new File(basePath+orignPath).delete();//删除原头像
        }
        return update? R.ok():R.error(RHttpStatusEnum.INSERT_FAILED);
    }

}
