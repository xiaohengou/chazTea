package com.chaz.reggie.controller;

import com.chaz.reggie.common.R;
import com.chaz.reggie.entity.Orders;
import com.chaz.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaz
 * @time 2023-06-12 19:35:44
 * @description TODO
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @description 用户下单
     * @author chaz
     * @date 19:50 2023/6/12
     * @param orders
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

}
