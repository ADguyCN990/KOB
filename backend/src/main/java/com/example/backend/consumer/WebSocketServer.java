package com.example.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.example.backend.consumer.utils.Game;
import com.example.backend.consumer.utils.JwtAuthentication;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private Session session = null; //后端向前端发信息,每个链接用session维护

    private User user; //当前链接请求的用户

    //与线程安全有关的哈希表，将userID映射到相应用户的WebSocketServer
    private static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    private static UserMapper userMapper;

    //线程安全的匹配池
    private static final CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();

    private Game game = null; //随机生成的游戏地图，要返回给前端

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接时自动调用该函数
        this.session = session;

        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);
            System.out.println("connected");
        } else {
            this.session.close();
        }
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接时自动调用该函数
        System.out.println("close");
        if (this.user != null) {
            users.remove(this.user.getId());
            matchPool.remove(this.user); //从匹配池中移出
        }
    }

    private void startMatching() {
        System.out.println("startMatching");
        matchPool.add(this.user);
        while (matchPool.size() >= 2) {
            Iterator<User> it = matchPool.iterator();
            //匹配成功a和b
            Game game = new Game(13, 14, 20);
            game.createMap();
            User a = it.next(), b = it.next();
            matchPool.remove(a);
            matchPool.remove(b);

            JSONObject respa = new JSONObject();
            respa.put("event", "start-matching");
            respa.put("opponent_username", b.getUsername());
            respa.put("opponent_photo", b.getPhoto());
            respa.put("gamemap", game.getG());
            users.get(a.getId()).sendMessage(respa.toJSONString()); //向前端发送信息

            JSONObject respb = new JSONObject();
            respb.put("event", "start-matching");
            respb.put("opponent_username", a.getUsername());
            respb.put("opponent_photo", a.getPhoto());
            respb.put("gamemap", game.getG());
            users.get(b.getId()).sendMessage(respb.toJSONString()); //向前端发送信息
        }
    }

    private void stopMatching() {
        System.out.println("stopMatching");
        matchPool.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) { //当做路由
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching();
        }
        else if ("stop-matching".equals(event)) {
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        //给Client发信息
        //异步通信要加上锁
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

