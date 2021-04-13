package com.zhuhodor.myblog.service;

import cn.hutool.core.lang.Assert;
import com.zhuhodor.myblog.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service("WebService")
@Slf4j
public class WebService {

    //文件保存路径
    private String fileStorePath;
    private Object lock = new Object();

    public Result upload(MultipartFile avatar, HttpServletRequest request){
        String realPath = request.getSession().getServletContext().getRealPath("/avatarImgs");
        String filePath;
        File folder = new File(realPath);
        if (!folder.isDirectory()){
            folder.mkdir();
        }
        synchronized(lock) {
            //图片名称
            String fileName;
            //图片名称赋值
            fileName = UUID.randomUUID().toString();
            try {
                avatar.transferTo(new File(folder,fileName));
                filePath =
                        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/avatarImgs/" + fileName;
                log.info("filePath------------->{}",filePath);
                return Result.success(filePath);
            } catch (IOException e) {
                log.error("error");
                e.printStackTrace();
            }
        }
        return Result.fail("error upload");
    }


}
