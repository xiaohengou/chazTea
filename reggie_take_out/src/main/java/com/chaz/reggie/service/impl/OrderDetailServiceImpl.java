package com.chaz.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaz.reggie.entity.OrderDetail;
import com.chaz.reggie.mapper.OrderDetailMapper;
import com.chaz.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author chaz
 * @time 2023-06-12 19:34:49
 * @description TODO
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
