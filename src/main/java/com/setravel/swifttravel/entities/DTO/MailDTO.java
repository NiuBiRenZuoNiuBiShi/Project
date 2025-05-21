package com.setravel.swifttravel.entities.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class MailDTO {
    private String from;
    private String replyTo;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private Date sentDate;
    private String subject;
    private String text;
    private String[] filenames;
}
