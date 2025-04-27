package com.setravel.swifttravel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.TrainNumberDetail;
import com.setravel.swifttravel.entities.Trainnumbers;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrainNumberMapper extends BaseMapper<Trainnumbers> {
}
