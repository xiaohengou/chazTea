package com.chaz.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaz.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
