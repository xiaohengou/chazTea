package com.chaz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaz.reggie.common.BaseContext;
import com.chaz.reggie.common.R;
import com.chaz.reggie.entity.ShoppingCart;
import com.chaz.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author chaz
 * @time 2023-06-12 17:02:10
 * @description TODO
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * @description 购物车添加功能
     * @author chaz
     * @date 17:25 2023/6/12
     * @param shoppingCart
     * @return com.chaz.reggie.common.R<com.chaz.reggie.entity.ShoppingCart>
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据:{}",shoppingCart);

        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询当前菜品是否已经在购物车中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if (dishId != null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        else{
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne!=null){
            //如果已经存在，就在原来数量基础上+1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            cartServiceOne.setCreateTime(LocalDateTime.now());
            shoppingCartService.updateById(cartServiceOne);
        }
        else {
            //如果不存在，添加菜品到购物车，数量默认为1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    /**
     * @description 对应id的商品数量-1
     * @author chaz
     * @date 18:13 2023/6/12
     * @param shoppingCart 
     * @return com.chaz.reggie.common.R<com.chaz.reggie.entity.ShoppingCart>
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("对应id的商品数量-1，id = {}",shoppingCart.getDishId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        //确定是 菜品 还是套餐
        if (shoppingCart.getDishId()!=null){
            //如果是菜品，根据菜品id 找到对应的数据
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        else {
            //如果是套餐，根据套餐id 找数据
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        //找到数据后，判断数量是否==1
        if (cartServiceOne.getNumber()==1){
            //如果==1，直接删除本条
            shoppingCartService.removeById(cartServiceOne);
        }
        else {
            //否则，在该数据基础上-1 更新即可。
            cartServiceOne.setNumber(cartServiceOne.getNumber()-1);
            shoppingCartService.updateById(cartServiceOne);
        }
        return R.success(cartServiceOne);
    }

    /**
     * @description 查看购物车
     * @author chaz
     * @date 17:37 2023/6/12
     * @return com.chaz.reggie.common.R<java.util.List<com.chaz.reggie.entity.ShoppingCart>>
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * @description 清空购物车
     * @author chaz
     * @date 17:46 2023/6/12
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        log.info("清空购物车");
        //SQL:delete from shopping_cart where user_id = ?
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("已清空购物车");
    }



}
