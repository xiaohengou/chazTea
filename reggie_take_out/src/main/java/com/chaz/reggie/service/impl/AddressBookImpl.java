package com.chaz.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaz.reggie.entity.AddressBook;
import com.chaz.reggie.mapper.AddressBookMapper;
import com.chaz.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author chaz
 * @time 2023-06-12 14:35:10
 * @description TODO
 */
@Service
public class AddressBookImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
