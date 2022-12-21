package com.example.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    //查询所有用户
    @GetMapping("/user/all")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    //根据ID查找用户
    @GetMapping("/user/{userid}")
    public User getUser(@PathVariable int userid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userid);

        // 范围遍历
        // public List<User> getUser(int userId)
        // queryWrapper.ge("id", 2).le("id", 3);
        // return userMapper.selectList(queryWrapper);
        return userMapper.selectOne(queryWrapper);
    }

    //添加用户
    @GetMapping("/user/{userid}/{username}/{password}")
    public String addUser(@PathVariable int userid,
                          @PathVariable String username,
                          @PathVariable String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(userid, username, encodedPassword);
        userMapper.insert(user);
        return "添加成功！";
    }

    //删除用户
    @GetMapping("/user/delete/{userid}")
    public String deleteUser(@PathVariable int userid) {
        userMapper.deleteById(userid);
        return "删除成功！";
    }

}
