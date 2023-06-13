package com.chaz.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaz.reggie.common.CustomException;
import com.chaz.reggie.entity.Category;
import com.chaz.reggie.entity.Dish;
import com.chaz.reggie.entity.Setmeal;
import com.chaz.reggie.mapper.CategoryMapper;
import com.chaz.reggie.service.CategoryService;
import com.chaz.reggie.service.DishService;
import com.chaz.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chaz
 * @time 2023-06-05 15:29:25
 * @description CategoryServiceImpl
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * @description 根据id删除分类，删除前进行判断
     * @author chaz
     * @date 17:17 2023/6/5
     * @param id
     */
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishlambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id查询
        dishlambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count=dishService.count(dishlambdaQueryWrapper);

        //查询当前 分类是否有关联的菜品，如果关联，抛出异常
        if (count>0) {
            //已经关联菜品，需要抛出异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前 分类是否有关联的套餐，如果关联，抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2=setmealService.count(setmealLambdaQueryWrapper);
        if (count2>0){
            //已经关联套餐，需要抛出异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //都没有，正常删除分类
        super.removeById(id);
    }
}
