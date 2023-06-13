package com.chaz.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaz.reggie.entity.Employee;
import com.chaz.reggie.mapper.EmployeeMapper;
import com.chaz.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author chaz
 * @time 2023-05-29 17:28:57
 * @description TODO
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
