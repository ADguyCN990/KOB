package com.example.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
@Component
public class MatchingPool extends Thread{
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    private static final String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }
    public void addPlayer(Integer userId, Integer rating, Integer bodId) {
        //涉及到操作player，加锁
        lock.lock();
        try {
            players.add(new Player(userId, rating, bodId, 0));
        } finally {
            lock.unlock();
        }
    }
    public void removePlayer(Integer userId) {
        //涉及到操作player，加锁
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }
    private void increaseWaitingTime() { //辅助函数，等待时间+1
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }
    private boolean checkMatched(Player a, Player b) { //辅助函数，判断两名玩家是否匹配
        int ratingDelta = Math.abs(a.getRating() - b.getRating()); //分差
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }
    private void sendResult(Player a, Player b) { //返回匹配结果
        System.out.println("send result:" + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("b_id", b.getUserId().toString());
        data.add("a_bot_id", a.getBotId().toString());
        data.add("b_bot_id", b.getBotId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }
    private void matchPlayers() {
        System.out.println("matchPlayers: " + players.toString());
        //标记是否被匹配
        boolean[] vis = new boolean[players.size()];
        // 先枚举等待最久的玩家，恰好是players前面的玩家等待的的久
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                if (vis[j] || vis[i]) continue;
                Player a = players.get(i), b = players.get(j);
                if (checkMatched(a, b)) {
                    vis[i] = vis[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }
        //筛出来剩下的玩家
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!vis[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
