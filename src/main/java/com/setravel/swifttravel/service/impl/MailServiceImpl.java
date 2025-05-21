package com.setravel.swifttravel.service.impl;

import java.io.IOException;
import java.util.Date;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.dozermapper.core.Mapper;
import com.setravel.swifttravel.config.MailConfig;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.DTO.MailDTO;
import com.setravel.swifttravel.service.MailService;

import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@Service
public class MailServiceImpl implements MailService {

    @Resource
    private MailConfig mailConfig;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private Mapper mapper;

    @Override
    public Result sendSimpleMailMessage(MailDTO mailDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // 设置发件人
            message.setFrom(
                    mailDTO.getFrom() != null ? mailDTO.getFrom() : mailConfig.getMailProperties().getUsername());

            // 设置收件人
            message.setTo(mailDTO.getTo());

            // 设置抄送人(如果有)
            if (mailDTO.getCc() != null && mailDTO.getCc().length > 0) {
                message.setCc(mailDTO.getCc());
            }

            // 设置密送人(如果有)
            if (mailDTO.getBcc() != null && mailDTO.getBcc().length > 0) {
                message.setBcc(mailDTO.getBcc());
            }

            // 设置回复地址(如果有)
            if (mailDTO.getReplyTo() != null) {
                message.setReplyTo(mailDTO.getReplyTo());
            }

            // 设置发送日期
            message.setSentDate(mailDTO.getSentDate() != null ? mailDTO.getSentDate() : new Date());

            // 设置邮件标题
            message.setSubject(mailDTO.getSubject());

            // 设置邮件内容
            message.setText(mailDTO.getText());

            // 发送邮件
            javaMailSender.send(message);
            return Result.success("Mail sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Failed to send mail");
        }
    }

    @Override
    public Result sendMimeMessage(MailDTO mailDTO) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            if (StringUtils.hasText(mailDTO.getFrom())) {
                helper.setFrom(mailDTO.getFrom());
            } else {
                helper.setFrom(mailConfig.getMailProperties().getUsername());
            }

            helper.setTo(mailDTO.getTo());
            helper.setSubject(mailDTO.getSubject());

            message = helper.getMimeMessage();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();

            mimeBodyPart.setContent(mailDTO.getText(), "text/html;charset=UTF-8");

            // 描述数据关系
            MimeMultipart mm = new MimeMultipart();
            mm.setSubType("related");
            mm.addBodyPart(mimeBodyPart);

            // 添加邮件附件
            for (String filename : mailDTO.getFilenames()) {
                MimeBodyPart attachPart = new MimeBodyPart();
                try {
                    attachPart.attachFile(filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mm.addBodyPart(attachPart);
            }
            message.setContent(mm);
            message.saveChanges();

        } catch (Exception e) {
            return Result.error("Failed to send mail" + e.getMessage());
        }

        javaMailSender.send(message);
        return Result.success("Mail sent successfully");
    }


    @Override
    public Result sendVerificationEmail(MailDTO mailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置发件人
        message.setFrom(mailConfig.getMailProperties().getUsername());
        // 设置收件人
        message.setTo(mailDTO.getTo());
        // 设置发送日期
        message.setSentDate(new Date());
        // 设置邮件标题
        message.setSubject(mailDTO.getSubject());
        // 设置邮件内容
        message.setText(mailDTO.getText());

        try {
            // 发送邮件
            javaMailSender.send(message);
            return Result.success("验证码已发送，请查收邮件");
        } catch (Exception e) {
            return Result.error("发送验证码失败，请稍后重试");
        }
    }
}
