package com.zhuhodor.myblog.AI;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.*;
import com.zhuhodor.myblog.vo.EntityVo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;;import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 文本分析类
 */
@Component
public class TextAnalysis {
    private static Credential cred;
    private static HttpProfile httpProfile;
    private static ClientProfile clientProfile;
    private static NlpClient client;
    static {
        cred = new Credential("AKIDqrb6Wa90mwqVw9OFerBFE4ez3uZ0LpoW", "qT7amjO5ZprcEZfUGqhZkETecJ5c37uw");

        httpProfile = new HttpProfile();
        httpProfile.setEndpoint("nlp.tencentcloudapi.com");

        clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        client = new NlpClient(cred, "ap-guangzhou", clientProfile);
    }

    //生成关键词
    public static List<String> generate(String text){
        ArrayList<String> list = new ArrayList<>();
        try{
            KeywordsExtractionRequest req = new KeywordsExtractionRequest();
            req.setText(text);
            req.setNum(5L);

            KeywordsExtractionResponse resp = client.KeywordsExtraction(req);
            Keyword[] keywords = resp.getKeywords();
            for (Keyword k : keywords)
                list.add(k.getWord());
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return list;
    }

    //生成摘要
    public static String autoSummary(String text){
        if (!StringUtils.isEmpty(text)){
            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);

            AutoSummarizationRequest req = new AutoSummarizationRequest();
            req.setText(text);
            AutoSummarizationResponse resp;
            try {
                resp = client.AutoSummarization(req);
                return resp.getSummary();
            } catch (TencentCloudSDKException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    //单词相似度
    public static Float autoClassify(String src, String target){
        WordSimilarityRequest req = new WordSimilarityRequest();
        req.setSrcWord(src);
        req.setTargetWord(target);

        try {
            WordSimilarityResponse resp = client.WordSimilarity(req);
            return resp.getSimilarity();
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return 0f;
    }

    //实体信息查询
    public static void wordRelation(String tag){
        DescribeEntityRequest request = new DescribeEntityRequest();
        request.setEntityName(tag);
        try {
            DescribeEntityResponse response = client.DescribeEntity(request);
            JSONObject jsonObject = JSONUtil.parseObj(response);
            JSONObject json = jsonObject.getJSONObject("Content");
            System.out.println(json);
            System.out.println(DescribeEntityResponse.toJsonString(response));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}
