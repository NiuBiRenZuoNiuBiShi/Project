package com.setravel.swifttravel.controller;

import org.springframework.web.bind.annotation.RestController;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.request.ContactRequest;
import com.setravel.swifttravel.service.ContactService;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
public class ContactController {

    @Resource
    private ContactService contactService;

    @PostMapping("/api/contact/add")
    public Result addContact(@RequestBody ContactRequest entity) {
        return contactService.addContact(entity);
    }

    @PostMapping("/api/contact/adds")
    public Result addContacts(@RequestBody List<ContactRequest> contactList) {
        return contactService.addContacts(contactList);
    }

    @DeleteMapping("/api/contact/delete")
    public Result deleteContact(@RequestBody ContactRequest entity) {
        return contactService.deleteContact(entity);
    }

    @PutMapping("/api/contact/update")
    public Result updateContact(@RequestBody ContactRequest entity) {
        return contactService.updateContact(entity);
    }

    @GetMapping("/api/contact/get")
    public Result getContacts() {
        return contactService.getContactsByCurrentUser();
    }
}
