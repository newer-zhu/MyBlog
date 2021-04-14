package com.zhuhodor.myblog.util;

import java.util.Random;

public class SaltUtil {
    public static String getSalt(int n){
        String s = "qwertyuioplkjhgfdsazxcvbnm0123456798";
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < n % s.length(); i++){
            sb.append(s.charAt(new Random().nextInt(s.length())));
        }
        return sb.toString();
    }
}
