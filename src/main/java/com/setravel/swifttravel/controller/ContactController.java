package com.setravel.swifttravel.controller;

import org.springframework.web.bind.annotation.RestController;

import com.setravel.swifttravel.entities.Contacts;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.service.ContactService;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
public class ContactController {

    @Resource
    private ContactService contactService;

    @PostMapping("/api/contact/add")
    public Result postMethodName(@RequestBody Contacts entity) {
        return contactService.addContact(entity);
    }

    @PostMapping("/api/contact/adds")
    public Result getMethodName(@RequestBody List<Contacts> contactList) {
        return contactService.addContacts(contactList);
    }

    @DeleteMapping("/api/contact/delete")
    public Result deleteMethodName(@RequestBody Contacts entity) {
        return contactService.deleteContact(entity);
    }

    @PutMapping("/api/contact/update")
    public Result updateMethodName(@RequestBody Contacts entity) {
        return contactService.updateContact(entity);
    }

    // @PostMapping("/api/contact/get")
    // public Result getMethodName(@RequestBody Contacts entity) {

    // return entity;
    // }
}
