package com.chaz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chaz.reggie.common.R;
import com.chaz.reggie.entity.Employee;
import com.chaz.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author chaz
 * @time 2023-05-29 17:30:23
 * @description TODO
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * @description 员工登录
     * @author chaz
     * @date 18:05 2023/5/29
     * @param request
     * @param employee
     * @return com.chaz.reggie.common.R<com.chaz.reggie.entity.Employee>
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request , @RequestBody Employee employee){
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6、登录成功，将员工id存入Sess ion并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * @description 员工退出
     * @author chaz
     * @date 20:05 2023/5/29
     * @param request 
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * @description 新增员工
     * @author chaz
     * @date 11:41 2023/5/30
     * @param request
     * @param employee 
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PostMapping//这里后边不用写具体uri是因为前端post请求 就是只到employee，而本类开头已经写了@RequestMapping("/employee")
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        //初始密码123456，需要md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获取当前用户id
//        Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * @description 员工信息分页查询
     * @author chaz
     * @date 17:40 2023/5/30
     * @param page
     * @param pageSize
     * @param name 
     * @return com.chaz.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {}, pageSize = {}, name={}",page,pageSize,name);

        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper); //执行后并不需要返回。因为mp已经把插叙难道的结果封装在page对象中

        return R.success(pageInfo);
    }

    /**
     * @description 启用/禁用员工
     * @author chaz
     * @date 10:22 2023/6/2
     * @param employee
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }


    /**
     * @description 根据id查询员工信息。
     * @author chaz
     * @date 19:47 2023/6/2
     * @param id 
     * @return com.chaz.reggie.common.R<com.chaz.reggie.entity.Employee>
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
