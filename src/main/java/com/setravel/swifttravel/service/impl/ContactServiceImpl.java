package com.setravel.swifttravel.service.impl;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.setravel.swifttravel.entities.Contacts;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.output.ContactOutput;
import com.setravel.swifttravel.entities.request.ContactRequest;
import com.setravel.swifttravel.mapper.ContactMapper;
import com.setravel.swifttravel.mapper.UserMapper;
import com.setravel.swifttravel.security.UserContext;
import com.setravel.swifttravel.service.ContactService;
import com.setravel.swifttravel.utils.UUIDUtil;

import jakarta.annotation.Resource;

@Service
public class ContactServiceImpl implements ContactService {

    @Resource
    private ContactMapper contactMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result addContact(ContactRequest request) {
        Contacts contact = request.toContact();
        byte[] currentUserId = UserContext.getCurrentUserId(userMapper);
        contact.setUserId(currentUserId);
        contact.setContactId(UUIDUtil.generateUUIDBytes());

        try {
            contactMapper.insert(contact);
        } catch (Exception e) {
            return Result.error("Failed to add contact: " + e.getMessage(), e);
        }
        return Result.success("Contact added successfully");
    }

    @Override
    public Result addContacts(List<ContactRequest> requestsList) {
        List<Contacts> contactList = requestsList.stream().map(ContactRequest::toContact).toList();
        byte[] currentUserId = UserContext.getCurrentUserId(userMapper);
        contactList.forEach(contact -> {
            contact.setUserId(currentUserId);
            contact.setContactId(UUIDUtil.generateUUIDBytes());
        });
        try {
            contactMapper.insert(contactList);
        } catch (Exception e) {
            return Result.error("Failed to add contacts: " + e.getMessage(), e);
        }
        return Result.success("Contacts added successfully");
    }

    @Override
    public Result deleteContact(ContactRequest request) {
        Contacts entity = request.toContact();
        try {
            contactMapper.update(new LambdaUpdateWrapper<Contacts>().set(Contacts::getDel, true)
                    .eq(Contacts::getContactId, entity.getContactId()));
        } catch (Exception e) {
            return Result.error("Failed to delete contact: " + e.getMessage(), e);
        }
        return Result.success("Contact deleted successfully");
    }

    @Override
    public Result updateContact(ContactRequest request) {
        Contacts entity = request.toContact();
        try {
            contactMapper.update(new LambdaUpdateWrapper<Contacts>().setEntity(entity)
                    .eq(Contacts::getContactId, entity.getContactId()).eq(Contacts::getDel, false));
        } catch (Exception e) {
            return Result.error("Failed to update contact: " + e.getMessage(), e);
        }
        return Result.success("Contact updated successfully");
    }

    @Override
    public Result getContactsByID(String contactID) {
        QueryWrapper<Contacts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contact_id", Base64.getDecoder().decode(contactID)).eq("del", false);
        Contacts contact = contactMapper.selectOne(queryWrapper);
        ContactOutput contactOutput = ContactOutput.fromEntity(contact);
        return new Result(200, "成功获取到相关数据", contactOutput);
    }

    @Override
    public Result getContactsByCurrentUser() {
        byte[] currentUserId = UserContext.getCurrentUserId(userMapper);

        LambdaQueryWrapper<Contacts> contactQueryWrapper = new LambdaQueryWrapper<Contacts>()
                .eq(Contacts::getUserId, currentUserId).eq(Contacts::getDel, false);
        List<Contacts> contacts;
        try {
            contacts = contactMapper.selectList(contactQueryWrapper);
        } catch (Exception e) {
            return Result.error("Failed to retrieve contacts: " + e.getMessage(), e);
        }
        List<ContactOutput> contactOutputs = contacts.stream().map(ContactOutput::fromEntity).toList();
        return Result.success("成功获取到相关数据", contactOutputs);
    }
}
