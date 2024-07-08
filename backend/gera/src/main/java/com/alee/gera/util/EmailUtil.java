package com.alee.gera.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EmailUtil {
    @Resource
    JavaMailSender javaMailSender;
    public void sendEnrollCode(String vcode,String userEmail){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 发件人
        simpleMailMessage.setFrom("m13487064935@163.com");
        // 收件人
        simpleMailMessage.setTo(userEmail);
        // 邮件主题
        simpleMailMessage.setSubject("来自Gera的验证码");
        // 邮件内容
        simpleMailMessage.setText("您正在注册账号，验证码(五分钟内有效)为:"+vcode+"。\n若不是您本人操作，请忽略此邮件。");
        javaMailSender.send(simpleMailMessage);
    }

    public void sendRetrieveCode(String vcode,String userEmail){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 发件人
        simpleMailMessage.setFrom("m13487064935@163.com");
        // 收件人
        simpleMailMessage.setTo(userEmail);
        // 邮件主题
        simpleMailMessage.setSubject("来自Gera的验证码");
        // 邮件内容
        simpleMailMessage.setText("您正在找回您的账号，验证码(五分钟内有效)为:"+vcode+"。\n若不是您本人操作，注意账号安全。");
        javaMailSender.send(simpleMailMessage);
    }
}
