package com.chaz.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaz.reggie.dto.SetmealDto;
import com.chaz.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * @description 新增套餐。同时需要保存套餐和菜品的关联关系
     * @author chaz
     * @date 20:06 2023/6/8
     * @param setmealDto 
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * @description 删除套餐，同时需要删除套餐和菜品的关联数据
     * @author chaz
     * @date 21:19 2023/6/8
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
