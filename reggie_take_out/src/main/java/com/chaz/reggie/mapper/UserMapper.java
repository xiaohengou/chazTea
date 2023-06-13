package com.chaz.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaz.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chaz
 * @time 2023-06-09 17:58:57
 * @description TODO
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
