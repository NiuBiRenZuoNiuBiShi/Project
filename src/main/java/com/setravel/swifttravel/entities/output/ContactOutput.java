package com.setravel.swifttravel.entities.output;

import java.time.LocalDateTime;
import java.util.Base64;

import com.setravel.swifttravel.entities.Contacts;

import lombok.Data;

@Data
public class ContactOutput {
    private String userId;
    private String contactId;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private LocalDateTime createdAt;

    public static ContactOutput fromEntity(Contacts contact) {
        ContactOutput output = new ContactOutput();
        output.setUserId(Base64.getEncoder().encodeToString(contact.getUserId()));
        output.setContactId(Base64.getEncoder().encodeToString(contact.getContactId()));
        output.setContactName(contact.getContactName());
        output.setContactPhone(contact.getContactPhone());
        output.setContactEmail(contact.getContactEmail());
        output.setCreatedAt(contact.getCreatedAt());
        return output;
    }
}
