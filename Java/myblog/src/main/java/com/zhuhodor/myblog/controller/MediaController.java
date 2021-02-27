package com.zhuhodor.myblog.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.config.GiteeImgBedConstant;
import com.zhuhodor.myblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/media")
@Slf4j
public class MediaController extends BaseController{

    @PostMapping("/saveimg")
    public Result<Map<String, Object>> saveImg(@RequestParam(value = "imgFile", required = true) MultipartFile imgFile) throws Exception {
        log.info("开始上传图片");
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();

        Map<String, Object> resultMap = new HashMap<String, Object>();

        String trueFileName = imgFile.getOriginalFilename();
        assert trueFileName != null;
        String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
        String fileName = System.currentTimeMillis() + "_" + IdUtil.randomUUID() + suffix;
        String paramImgFile = Base64.encode(imgFile.getBytes());

        //转存到gitee
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("access_token", GiteeImgBedConstant.TOKEN);
        paramMap.put("message", GiteeImgBedConstant.CREATE_REPOS_MESSAGE);
        paramMap.put("content", paramImgFile);
        //存放路径
        String targetDir = GiteeImgBedConstant.IMG_FILE_DEST_PATH + fileName;
        //请求路径
        String requestUrl = String.format(GiteeImgBedConstant.CREATE_REPOS_URL, GiteeImgBedConstant.OWNER,
                GiteeImgBedConstant.REPO_NAME, targetDir);
        System.out.println(requestUrl);
        //上传图片
        String resultJson = HttpUtil.post(requestUrl, paramMap);

        JSONObject jsonObject = JSONUtil.parseObj(resultJson);
        if (jsonObject.getObj("commit") != null) {
            String resultImgUrl = GiteeImgBedConstant.GITPAGE_REQUEST_URL + targetDir;
            resultMap.put("resultImgUrl", resultImgUrl);
            System.out.println(resultJson);
            result.setCode(200);
        } else {
            result.setCode(400);
        }
        result.setData(resultMap);

        return result;
    }

    @GetMapping("/delimg/{url}")
    public Result delImag(@PathVariable("url") String url){
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("access_token", GiteeImgBedConstant.TOKEN);
        paramMap.put("message", "delImg");
        paramMap.put("path", url);
        String requestUrl = String.format(GiteeImgBedConstant.CREATE_REPOS_URL, GiteeImgBedConstant.OWNER,
                GiteeImgBedConstant.REPO_NAME);
        return Result.success("delete OK!");
    }

    @PostMapping("/fileupload")
    public Result fileUpload(@RequestParam("file") MultipartFile file){
        String filename = file.getOriginalFilename();//获得文件原名 如：abc.txt
        log.info("开始上传文件====>{}", filename);
        String filePath = "E:/saving/";//存放文件的上一层路径
        File f = new File(filePath);
        //若不存在 E:/saving/ 这个文件夹则创建
        if (!f.exists()){
            f.getParentFile().mkdir();
        }
        //将要被储存的对象 E:/saving/abc.txt
        File target = new File(f.getAbsolutePath() +'/' + filename);
        //目标文件如果已经存在说明文件重名了，需要另起一个唯一的名字
        if (target.exists()){
            int i = filename.lastIndexOf('.');
            String originName = filename.substring(0, i);//文件名字 abc
            String suffix = filename.substring(i, filename.length()); //文件后缀 .txt
            target = new File(f.getAbsolutePath() +'/' + originName + UUID.randomUUID() + suffix);
        }
        //创建目标文件夹
        target.getParentFile().mkdir();
        try {//储存过程
            file.transferTo(target);
            return Result.success(target.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.fail("上传失败");
    }

    @GetMapping("/filedownload/{blogId}")
    public Result fileDownload(@PathVariable String blogId, HttpServletResponse response){
        String filePath = blogService.findBlogById(blogId).getContent();
        response.setHeader("content-type", "image/png");
        response.setContentType("application/octet-stream");
        String fileName = filePath.substring(filePath.lastIndexOf('\\') + 1,
                filePath.lastIndexOf('.')) + filePath.substring(filePath.lastIndexOf('.'));
        log.info("开始下载 {} 文件", fileName);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        File file = new File(filePath);
        if (file.exists()){
            byte[] bytes = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            ServletOutputStream os = null;
            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                os = response.getOutputStream();
                int read = bis.read(bytes);
                while (read !=  -1){
                    os.write(bytes, 0, bytes.length);
                    os.flush();
                    read = bis.read(bytes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (bis != null){
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
