package com.setravel.swifttravel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.Contacts;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContactsMapper extends BaseMapper<Contacts> {
}
