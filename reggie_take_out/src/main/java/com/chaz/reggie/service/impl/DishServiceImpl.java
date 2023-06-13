package com.chaz.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaz.reggie.dto.DishDto;
import com.chaz.reggie.entity.Dish;
import com.chaz.reggie.entity.DishFlavor;
import com.chaz.reggie.mapper.DishMapper;
import com.chaz.reggie.service.DishFlavorService;
import com.chaz.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chaz
 * @time 2023-06-05 17:08:30
 * @description TODO
 */
@Slf4j
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * @description 新增菜品同时保存对于的口味数据
     * @author chaz
     * @date 10:48 2023/6/6
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId=dishDto.getId();//获取菜品id
        //菜品口味
        List<DishFlavor> flavors=dishDto.getFlavors();
        //将dishid赋值给每一个DsihFlavor  （.stream()是将集合转化为流,.map是将流中的元素计算或者转换(此处用map为流中的dishid赋值),.collect是将流转为集合）
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
//        dishFlavorService.saveBatch(dishDto.getFlavors()); 这样写有问题，因为直接getFlavors()中的数据没有dishid
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * @description 根据id查询菜品信息和对应的口味信息
     * @author chaz
     * @date 10:24 2023/6/8
     * @param id
     * @return com.chaz.reggie.dto.DishDto
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应额度口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * @description 更新菜品信息，同时更新口味信息
     * @author chaz
     * @date 10:41 2023/6/8
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //清理房前菜品对应口味数据--dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据--dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
