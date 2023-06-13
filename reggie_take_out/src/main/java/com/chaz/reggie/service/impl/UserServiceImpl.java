package com.chaz.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaz.reggie.entity.User;
import com.chaz.reggie.mapper.UserMapper;
import com.chaz.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author chaz
 * @time 2023-06-09 18:02:59
 * @description TODO
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
