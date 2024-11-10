package com.yrq.utils;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.beans.factory.annotation.Value;
import java.lang.annotation.Annotation;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class SystemException{
    @Value("${spring.servlet.multipart.max-file-size}")
    String maxsize;
    @ExceptionHandler(value = FileSizeLimitExceededException.class)
    public R FileSizeLimitExceededExceptionHander(FileSizeLimitExceededException ex){
        String msg = "上传文件过大，总上传文件大小不得超过" + maxsize + "！";
        return R.error(RHttpStatusEnum.INSERT_FAILED,msg);
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R SQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex){
        if(ex.getMessage().contains("Duplicate entry")){

            String[] split = ex.getMessage().split(" ");
            System.out.println(split);
            System.out.println(split[2]);
            String msg="该用户已存在！";
            return R.error(RHttpStatusEnum.INSERT_FAILED,msg);
        }
        else if(ex.getMessage().contains("fk_live_building_1")){
            String msg="该楼栋有居住信息，如需删除请先删除相关居民信息";
            return R.error(RHttpStatusEnum.DELETE_FAILED,msg);
        }
        else if(ex.getMessage().contains("fk_live_grid_1")){
            String msg="该网格有楼栋信息，如需删除请先删除相关楼栋信息";
            return R.error(RHttpStatusEnum.DELETE_FAILED,msg);
        }
        return R.error(RHttpStatusEnum.SERVER_ERROR);
    }
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public R SQLSyntaxErrorExceptionHander(SQLSyntaxErrorException ex){
        return R.error(RHttpStatusEnum.HTTP_NOT_FOUND,new Page());
    }
}
