package com.example.backend.service.impl.user.account;

import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.utils.UserDetailsImpl;
import com.example.backend.service.user.account.GetphotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetphotoImpl implements GetphotoService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> getPhoto(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        Map<String, String> map = new HashMap<>();
        String url = data.get("url");

        if (url == null || url.length() == 0) {
            map.put("error_message", "url is empty");
            return map;
        }
        System.out.println(url);
        User newUser = new User(user.getId(), user.getUsername(), user.getPassword(),
                url, user.getRating());
        userMapper.updateById(newUser);
        map.put("error_message", "success");
        return map;
    }
}
