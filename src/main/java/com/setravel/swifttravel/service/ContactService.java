package com.setravel.swifttravel.service;

import java.util.List;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.request.ContactRequest;


public interface ContactService {
    Result addContact(ContactRequest contact);

    Result addContacts(List<ContactRequest> contactList);

    Result deleteContact(ContactRequest entity);

    Result updateContact(ContactRequest entity);

    Result getContactsByID(String contactId);

    Result getContactsByCurrentUser();
}
