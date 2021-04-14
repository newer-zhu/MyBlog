package com.zhuhodor.myblog.AI;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class COS {
    private static COSClient cosClient = null;
    // 1 初始化用户身份信息（secretId, secretKey）。
    static {
        String secretId = "AKIDqrb6Wa90mwqVw9OFerBFE4ez3uZ0LpoW";
        String secretKey = "qT7amjO5ZprcEZfUGqhZkETecJ5c37uw";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-nanjing");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
    }

    public static String uploadAvatar(MultipartFile avatar) throws IOException {
// 指定文件将要存放的存储桶
        String bucketName = "mybase-1-1305081850";
// 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        Date time = Calendar.getInstance().getTime();
        String path = new SimpleDateFormat("yyyy-MM-dd").format(time);
        String key = path+"/"+UUID.randomUUID().toString()+".jpg";
        ObjectMetadata objectMetadata = new ObjectMetadata();
// 设置输入流长度为
        objectMetadata.setContentLength(avatar.getInputStream().available());
        // 设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType("image/jpeg");
        PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, avatar.getInputStream(), objectMetadata);
        return "https://mybase-1-1305081850.cos.ap-nanjing.myqcloud.com/"+key;
    }

    public static String uploadPic(MultipartFile avatar) throws IOException{
        // 指定文件将要存放的存储桶
        String bucketName = "mybase-1-1305081850";
        Date time = Calendar.getInstance().getTime();
        String path = new SimpleDateFormat("yyyy-MM-dd").format(time);
        String key = path+"/"+UUID.randomUUID().toString()+".jpg";
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(avatar.getInputStream().available());
        objectMetadata.setContentType("image/jpeg");
        PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, avatar.getInputStream(), objectMetadata);
        return "https://mybase-1-1305081850.cos.ap-nanjing.myqcloud.com/"+key;
    }
}
