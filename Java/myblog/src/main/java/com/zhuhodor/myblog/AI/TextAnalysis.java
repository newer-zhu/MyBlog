package com.zhuhodor.myblog.AI;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.*;
import org.springframework.stereotype.Component;;import java.util.ArrayList;
import java.util.List;

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
}
