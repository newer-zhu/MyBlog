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
public class KeywordsExtraction
{
    public static List<String> generate(String text){
        ArrayList<String> list = new ArrayList<>();
        try{
            Credential cred = new Credential("AKIDqrb6Wa90mwqVw9OFerBFE4ez3uZ0LpoW", "qT7amjO5ZprcEZfUGqhZkETecJ5c37uw");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("nlp.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);

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

}
