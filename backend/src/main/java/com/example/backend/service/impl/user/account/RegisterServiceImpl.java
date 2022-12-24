package com.example.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import com.example.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper; //对数据库进行增删改查

    @Autowired
    private PasswordEncoder passwordEncoder; //加密明文

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if (username == null) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if (password == null || confirmedPassword == null) {
            map.put("error_message", "密码不能为空");
            return map;
        }
        //删除首尾的空白字符
        username = username.trim();
        if (username.length() == 0) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "密码不能为空");
            return map;
        }
        //限制在规定长度内
        if (username.length() > 100) {
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }
        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "密码长度不能大于100");
            return map;
        }
        //两次输入密码不一致
        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "两次输入的密码不一致");
            return map;
        }
        //查询用户名是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "用户名已存在");
            return map;
        }


        //用户名密码无问题，则添加一个新用户
        String encodedPassword = passwordEncoder.encode(password);
        List<String> photos = new ArrayList<>();
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202212072159086.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211242052207.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211232308530.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211232308493.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211231920870.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211231918014.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/202211231910960.webp");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/45301314.png");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/typora-img/202209050905112.jpg");
        photos.add("https://adguycn990-typoraimage.oss-cn-hangzhou.aliyuncs.com/typora-img/202207191026188.webp");

        Random random = new Random();
        User user = new User(null, username, encodedPassword, photos.get(random.nextInt(photos.size())));
        userMapper.insert(user);
        map.put("error_message", "success");
        return map;

    }
}
