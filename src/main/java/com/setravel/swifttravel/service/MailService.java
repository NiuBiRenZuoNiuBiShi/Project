package com.setravel.swifttravel.service;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.DTO.MailDTO;

public interface MailService {
    Result sendSimpleMailMessage(MailDTO mailDTO);


    Result sendMimeMessage(MailDTO mailDTO);

    Result sendVerificationEmail(MailDTO mailDTO);
}
