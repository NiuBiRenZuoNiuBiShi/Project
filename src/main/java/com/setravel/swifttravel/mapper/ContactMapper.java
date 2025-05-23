package com.setravel.swifttravel.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.Contacts;

@Mapper
public interface ContactMapper extends BaseMapper<Contacts> {
    // Define any additional methods specific to Contacts if needed
    // For example, you might want to add methods for querying contacts by different criteria
    // or for updating/deleting contacts.
    
}
