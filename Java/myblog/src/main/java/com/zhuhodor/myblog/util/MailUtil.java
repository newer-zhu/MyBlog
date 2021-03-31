package com.zhuhodor.myblog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailUtil {
    @Autowired
    JavaMailSenderImpl mailSender;

    public void send(String email, String sender){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("新的申请");
            helper.setText("<h1> 用户"+sender+" 对你的项目感兴趣</h1>", true);
            helper.setTo(email);
            helper.setFrom("1713622254@qq.com");
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
