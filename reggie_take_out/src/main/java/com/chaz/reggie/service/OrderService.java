package com.chaz.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaz.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * @description 用户下单
     * @author chaz
     * @date 19:52 2023/6/12
     * @param orders 
     */
    public void submit(Orders orders);
}
