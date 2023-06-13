package com.chaz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaz.reggie.common.R;
import com.chaz.reggie.entity.User;
import com.chaz.reggie.service.UserService;
import com.chaz.reggie.utils.SMSUtils;
import com.chaz.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author chaz
 * @time 2023-06-09 18:04:55
 * @description TODO
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @description 发送手机短信验证码
     * @author chaz
     * @date 20:14 2023/6/9
     * @param user
     * @return com.chaz.reggie.common.R<java.lang.String>
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone=user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            //生成随机4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("phone={} , code={}",phone,code);

            //调用阿里云短信服务api完成发送短信
            SMSUtils.sendMessage("工大外卖奶茶", "SMS_461320090",phone,"1314");

            //需要将生成的验证码保存到session
            session.setAttribute(phone,code);

            return R.success("手机短信验证码发送成功");
        }
        return R.error("手机短信验证码发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //获取手机号
        String phone= map.get("phone").toString();

        //获取验证码
        String code=map.get("code").toString();

        //从Session中获取保存的验证码
        Object codeInsession = session.getAttribute(phone);

        //进行验证码比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInsession!=null&&codeInsession.equals(code)){
            //如果比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if (user == null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }

        return R.error("登陆失败");
    }
}
