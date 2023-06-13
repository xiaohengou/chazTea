package com.chaz.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaz.reggie.entity.ShoppingCart;
import com.chaz.reggie.mapper.ShoppingCartMapper;
import com.chaz.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author chaz
 * @time 2023-06-12 17:01:07
 * @description TODO
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService{
}
