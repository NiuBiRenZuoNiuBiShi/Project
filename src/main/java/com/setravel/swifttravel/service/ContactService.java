package com.setravel.swifttravel.service;

import java.util.List;

import com.setravel.swifttravel.entities.Contacts;
import com.setravel.swifttravel.entities.Result;


public interface ContactService {
    Result addContact(Contacts contact);

    Result addContacts(List<Contacts> contactList);

    Result deleteContact(Contacts entity);

    Result updateContact(Contacts entity);
}
