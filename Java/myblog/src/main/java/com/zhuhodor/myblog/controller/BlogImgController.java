package com.zhuhodor.myblog.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.config.GiteeImgBedConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/blogimg")
@Slf4j
public class BlogImgController {

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

        String targetDir = GiteeImgBedConstant.IMG_FILE_DEST_PATH + fileName;
        String requestUrl = String.format(GiteeImgBedConstant.CREATE_REPOS_URL, GiteeImgBedConstant.OWNER,
                GiteeImgBedConstant.REPO_NAME, targetDir);

        System.out.println(requestUrl);

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
}
