package com.zhuhodor.myblog.config;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyCacheConfig {
    /**
     * 自定义key生成策略
     * @return
     */
//    @Bean
//    public KeyGenerator cacheKeyGenerator() {
//        return (target, method, params) -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append(target.getClass().getName());
//            sb.append(method.getName());
//            sb.append(":");
//            sb.append(DigestUtil.md5(JSONUtil.toJsonStr(params)));
//            return sb.toString();
//        };
//    }
}
