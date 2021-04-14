package com.zhuhodor.myblog.test;

import com.zhuhodor.myblog.AI.COS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
public class MailTest {
    @Autowired
    JavaMailSenderImpl mailSender;

    @Test
    public void send(){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("测试");
            helper.setText("<h1>666</h1>", true);
            helper.setTo("1713622254@qq.com");
            helper.setFrom("1713622254@qq.com");
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ts(){
    }
}
