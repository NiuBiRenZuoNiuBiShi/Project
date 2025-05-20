package com.setravel.swifttravel.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.setravel.swifttravel.entities.Contacts;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.mapper.ContactMapper;
import com.setravel.swifttravel.service.ContactService;

import jakarta.annotation.Resource;

@Service
public class ContactServiceImpl implements ContactService {

    @Resource
    private ContactMapper contactMapper;

    @Override
    public Result addContact(Contacts contact) {
        try {
            contactMapper.insert(contact);
        } catch (Exception e) {
            return Result.error("Failed to add contact: " + e.getMessage(), e);
        }
        return Result.success("Contact added successfully");
    }

    @Override
    public Result addContacts(List<Contacts> contactList) {
        try {
            contactMapper.insert(contactList);
        } catch (Exception e) {
            return Result.error("Failed to add contacts: " + e.getMessage(), e);
        }
        return Result.success("Contacts added successfully");
    }

    @Override
    public Result deleteContact(Contacts entity) {
        try {
            contactMapper.deleteById(entity.getContactId());
        } catch (Exception e) {
            return Result.error("Failed to delete contact: " + e.getMessage(), e);
        }
        return Result.success("Contact deleted successfully");
    }

    @Override
    public Result updateContact(Contacts entity) {
        LambdaQueryWrapper<Contacts> deleteWrapper = new LambdaQueryWrapper<Contacts>().eq(Contacts::getContactId,
                entity.getContactId());
        try {
            contactMapper.delete(deleteWrapper);
            contactMapper.insert(entity);
        } catch (Exception e) {
            return Result.error("Failed to update contact: " + e.getMessage(), e);
        }
        return Result.success("Contact updated successfully");
    }
}
