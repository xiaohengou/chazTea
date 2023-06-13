package com.chaz.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaz.reggie.entity.DishFlavor;
import com.chaz.reggie.mapper.DishFlavorMapper;
import com.chaz.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author chaz
 * @time 2023-06-06 10:11:58
 * @description TODO
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
