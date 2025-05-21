package com.setravel.swifttravel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Component;

@Component
public class MailConfig {

    private final MailProperties mailProperties;

    @Value("${spring.mail.password}")
    private String AUTHCODE;

    public MailConfig(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    public MailProperties getMailProperties() {
        return mailProperties;
    }

    public String getAUTHCODE() {
        return AUTHCODE;
    }
}
