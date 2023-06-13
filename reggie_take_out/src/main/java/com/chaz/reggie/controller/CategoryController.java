package com.chaz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chaz.reggie.common.R;
import com.chaz.reggie.entity.Category;
import com.chaz.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chaz
 * @time 2023-06-05 15:31:11
 * @description TODO
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @description 新增分类
     * @author chaz
     * @date 16:12 2023/6/5
     * @param category 
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * @description 分页查询
     * @author chaz
     * @date 16:15 2023/6/5
     * @param page
     * @param pageSize 
     * @return com.chaz.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * @description 根据id删除分类
     * @author chaz
     * @date 16:51 2023/6/5
     * @param id
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id为：{}" ,id);

//        categoryService.removeById(id);
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * @description 根据id更新分类信息
     * @author chaz
     * @date 20:02 2023/6/5
     * @param category
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);

        categoryService.updateById(category);

        return R.success("修改分类信息成功");
    }

    /**
     * @description 根据条件查询分类数据
     * @author chaz
     * @date 10:21 2023/6/6 
     * @param category 
     * @return com.chaz.reggie.common.R<java.util.List<com.chaz.reggie.entity.Category>>
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
