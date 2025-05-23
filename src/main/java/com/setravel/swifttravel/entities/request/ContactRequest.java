package com.setravel.swifttravel.entities.request;

import java.time.LocalDateTime;
import java.util.Base64;

import com.setravel.swifttravel.entities.Contacts;

import lombok.Data;

@Data
public class ContactRequest {
    private String userId;
    private String contactId;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private LocalDateTime createdAt;

    public Contacts toContact() {
        Contacts contact = new Contacts();
        contact.setUserId(Base64.getDecoder().decode(userId));
        contact.setContactId(Base64.getDecoder().decode(contactId));
        contact.setContactName(contactName);
        contact.setContactPhone(contactPhone);
        contact.setContactEmail(contactEmail);
        contact.setCreatedAt(createdAt);
        return contact;
    }
}
