package com.example.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.example.backend.consumer.utils.Game;
import com.example.backend.consumer.utils.JwtAuthentication;
import com.example.backend.consumer.utils.Player;
import com.example.backend.mapper.RecordMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    public static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    //与线程安全有关的哈希表，将userID映射到相应用户的WebSocketServer
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private static RestTemplate restTemplate;
    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";
    //线程安全的匹配池
    private Game game = null; //随机生成的游戏地图，要返回给前端
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {WebSocketServer.restTemplate = restTemplate;}
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
    }

    @OnClose
    public void onClose() {
        // 关闭链接时自动调用该函数
        System.out.println("close");
    }
    public static void startGame(Integer aId, Integer bId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Game game = new Game(13, 14, 20, a.getId(), b.getId());
        game.createMap();
        users.get(a.getId()).game = game; //维护用户A的websocket
        users.get(b.getId()).game = game; //维护用户B的websocket
        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());

        JSONObject respa = new JSONObject();
        respa.put("event", "start-matching");
        respa.put("opponent_username", b.getUsername());
        respa.put("opponent_photo", b.getPhoto());
        respa.put("game", respGame);
        users.get(a.getId()).sendMessage(respa.toJSONString()); //向前端发送信息

        JSONObject respb = new JSONObject();
        respb.put("event", "start-matching");
        respb.put("opponent_username", a.getUsername());
        respb.put("opponent_photo", a.getPhoto());
        respb.put("game", respGame);
        users.get(b.getId()).sendMessage(respb.toJSONString()); //向前端发送信息
    }
    private void startMatching() {
        System.out.println("startMatching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private void stopMatching() {
        System.out.println("stopMatching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    private void move(int direction) {
        if (user.getId().equals(game.getPlayerA().getId())) {
            game.setNextStepA(direction);
        }
        else if (user.getId().equals(game.getPlayerB().getId())) {
            game.setNextStepB(direction);
        }
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
        else if ("move".equals(event)) {
            move(data.getInteger("direction"));
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

