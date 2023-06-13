package com.chaz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chaz.reggie.common.CustomException;
import com.chaz.reggie.common.R;
import com.chaz.reggie.dto.DishDto;
import com.chaz.reggie.entity.Category;
import com.chaz.reggie.entity.Dish;
import com.chaz.reggie.entity.DishFlavor;
import com.chaz.reggie.entity.Employee;
import com.chaz.reggie.service.CategoryService;
import com.chaz.reggie.service.DishFlavorService;
import com.chaz.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chaz
 * @time 2023-06-06 10:12:54
 * @description 菜品管理
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * @description 新增菜品
     * @author chaz
     * @date 10:41 2023/6/6
     * @param dishDto
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * @description 菜品信息分页查询
     * @author chaz
     * @date 11:44 2023/6/6
     * @param page
     * @param pageSize
     * @param name
     * @return com.chaz.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * @description 根据id查询菜品信息和对应的口味信息
     * @author chaz
     * @date 10:23 2023/6/8
     * @param id
     * @return com.chaz.reggie.common.R<com.chaz.reggie.dto.DishDto>
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * @description 修改菜品信息
     * @author chaz
     * @date 10:40 2023/6/8
     * @param dishDto
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * @description 起售、停售以及 批量起售、停售
     * @author chaz
     * @date 17:53 2023/6/8
     * @param status
     * @param id
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam Long[] id){//PathVariable:路径参数
        log.info("当前状态：{}，当前id：{}",status,id);

        for(int i=0;i<id.length;i++)
        {
            Dish dish = dishService.getById(id[i]);
            if (dish!=null){
                dish.setStatus(status);
                dishService.updateById(dish);
            }
        }
        return R.success("商品状态修改成功");
    }

    /**
     * @description （批量）删除菜品
     * @author chaz
     * @date 18:05 2023/6/8
     * @param id
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @DeleteMapping
    public R<String> delete(@RequestParam Long[] id){
        log.info("删除菜品，id为：{}" ,id);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, id);
        List<Dish> dishes = dishService.list(queryWrapper);
        for (Dish dish : dishes) {
            if (dish.getStatus() == 1) {
                throw new CustomException("删除列表中存在启售状态商品，无法删除");
            }
        }
        dishService.remove(queryWrapper);
        return R.success("删除成功");
    }

    /**
     * @description 展示菜品分类数据
     * @author chaz
     * @date 18:18 2023/6/8
     * @param dish 
     * @return com.chaz.reggie.common.R<java.util.List<com.chaz.reggie.entity.Dish>>
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        //构造查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId, dish.getCategoryId());
//        //只查在售的，过滤掉停售的（状态为0的）
//        queryWrapper.eq(Dish::getStatus,1);
//        //添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId, dish.getCategoryId());
        //只查在售的，过滤掉停售的（状态为0的）
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtolist = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtolist);
    }


}
