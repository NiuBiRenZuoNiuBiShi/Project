package com.setravel.swifttravel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.setravel.swifttravel.entities.Contacts;
import com.setravel.swifttravel.mapper.ContactsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ContactServiceImpl {

    @Resource
    ContactsMapper contactsMapper;

    public Contacts getContactsByID(Integer contactID) {
        QueryWrapper<Contacts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contact_id", Base64.getDecoder().decode(String.valueOf(contactID)))
            .eq("del", false);
        return contactsMapper.selectOne(queryWrapper);
    }
}
