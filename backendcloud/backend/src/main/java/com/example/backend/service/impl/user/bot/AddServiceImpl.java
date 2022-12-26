package com.example.backend.service.impl.user.bot;

import com.example.backend.mapper.BotMapper;
import com.example.backend.pojo.Bot;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.utils.UserDetailsImpl;
import com.example.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        //一些非法情况
        if (title == null || title.length() == 0) {
            map.put("error_message", "Bot名称不能为空");
            return map;
        }
        if (title.length() > 100) {
            map.put("error_message", "Bot名称不能超过100个字符");
            return map;
        }
        if (description == null || description.length() == 0) {
            description = "这个用户很懒，什么也没有留下~";
        }
        if (description.length() > 300) {
            map.put("error_message", "Bot简介不能超过300个字符");
            return map;
        }
        if (content == null || content.length() == 0) {
            map.put("error_message", "Bot代码不能为空");
            return map;
        }
        if (content.length() > 10000) {
            map.put("error_message", "Bot代码不能超过10k个字符");
            return map;
        }

        //格式合法，该Bot加入到数据库中
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, now, now);
        botMapper.insert(bot);
        map.put("error_message", "success");
        return map;
    }
}
