package com.example.backend.service.impl.user.bot;

import com.example.backend.mapper.BotMapper;
import com.example.backend.pojo.Bot;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.utils.UserDetailsImpl;
import com.example.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");
        int bot_id = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(bot_id);


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


        if (bot == null) {
            map.put("error_message", "该Bot不存在或已被删除");
            return map;
        }
        if (!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限修改该Bot");
            return map;
        }

        //若合法，则更改bot信息
        Bot newBot = new Bot(bot.getId(), user.getId(), title, description, content,
                bot.getCreatetime(), new Date());
        botMapper.updateById(newBot);
        map.put("error_message", "update success");
        return map;

    }
}
