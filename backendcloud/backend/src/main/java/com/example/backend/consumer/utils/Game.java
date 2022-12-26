package com.example.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.example.backend.config.WebSocketConfig;
import com.example.backend.consumer.WebSocketServer;
import com.example.backend.pojo.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private String status = "playing"; //playing -> finished
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private int[][] g; //0表示空地1表示墙
    private final Player PlayerA, PlayerB;
    private Integer nextStepA = null; //玩家A的下一步操作
    private Integer nextStepB = null; //玩家B的下一步操作
    private String loser = ""; //"all"平局，"A"，"B"
    private ReentrantLock lock = new ReentrantLock();
    public void setNextStepA(Integer nextStepA) { //设置A的下一步操作
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }
    public void setNextStepB(Integer nextStepB) { //设置B的下一步操作
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }
    private boolean nextStep() { //辅助函数, 两名玩家下一步操作是否就绪
        try {
            Thread.sleep(200); //最多200ms一次操作，频率在高的话操作可能会丢失
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 因为会读玩家的nextStep操作，因此加锁
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        PlayerA.getSteps().add(nextStepA);
                        PlayerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB) {
        //辅助函数，判断cellsA的蛇头是否合法
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (g[cell.getX()][cell.getY()] == 1) { //撞墙
            return false;
        }
        for (int i = 0; i < n - 1; i++) {
            if (cellsA.get(i).getX() == cell.getX() &&
                    cellsA.get(i).getY() == cell.getY()) { //和蛇身重合
                return false;
            }
            if (cellsB.get(i).getX() == cell.getY() &&
                    cellsB.get(i).getY() == cell.getY()) { //和蛇身重合
                return false;
            }
        }
        return true;
    }
    private void judge() { //辅助函数，判断两名玩家下步操作是否合法
        List<Cell> cellsA = PlayerA.getCells();
        List<Cell> cellsB = PlayerB.getCells();
        boolean validA = checkValid(cellsA, cellsB);
        boolean validB = checkValid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            }
            else if (!validA) {
                loser = "A";
            }
            else {
                loser = "B";
            }
        }
    }
    private void sendAllMessage(String message) { //辅助函数，广播信息
        WebSocketServer.users.get(PlayerA.getId()).sendMessage(message);
        WebSocketServer.users.get(PlayerB.getId()).sendMessage(message);
    }
    private void sendMove() { //辅助函数, 向两名玩家发送move操作结果
        lock.lock();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event", "move");
            jsonObject.put("a_direction", nextStepA);
            jsonObject.put("b_direction", nextStepB);
            sendAllMessage(jsonObject.toJSONString());
            nextStepA = nextStepB = null; //清空操作，避免影响接下来的结果
        } finally {
            lock.unlock();
        }
    }
    private String getMapString() { //辅助函数，将游戏地图类型转化为string
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }
    private void saveToDatabase() { //向数据库保存对局记录
        Record record = new Record(
                null,
                getPlayerA().getId(),
                getPlayerA().getSx(),
                getPlayerA().getSy(),
                getPlayerB().getId(),
                getPlayerB().getSx(),
                getPlayerB().getSy(),
                getPlayerA().getStepsString(),
                getPlayerB().getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }
    private void sendResult() { //辅助函数,向两名玩家发送游戏结果，并将游戏记录保存到数据库
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", "result");
        jsonObject.put("loser", loser);
        sendAllMessage(jsonObject.toJSONString());
        saveToDatabase(); //把游戏记录保存到数据库
    }
    @Override
    public void run() { //重写Thread
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) {
                //玩家都执行了操作
                judge();
                if (status.equals("playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            }
            else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    }
                    else if (nextStepA == null) {
                        loser = "A";
                    }
                    else if (nextStepB == null) {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;

            }
        }
    }
    public Player getPlayerA() {
        return PlayerA;
    }
    public Player getPlayerB() {
        return PlayerB;
    }

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        PlayerA = new Player(idA, this.rows - 2, 1, new ArrayList<>());
        PlayerB = new Player(idB, 1, this.cols - 2, new ArrayList<>());
    }

    public int[][] getG() { //返回地图
        return g;
    }

    private void initMap() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }
        //四周是墙
        for (int i = 0; i < this.rows; i++) {
            g[i][0] = 1;
            g[i][this.cols - 1] = 1;
        }
        for (int i = 0; i < this.cols; i++) {
            g[0][i] = 1;
            g[this.rows - 1][i] = 1;
        }
    }

    private boolean check_connectivity(int sx, int sy, int ex, int ey) {
        if (sx == ex && sy == ey) return true;
        g[sx][sy] = 1;
        for (int i = 0; i < 4; i++) {
            int a = sx + dx[i], b = sy + dy[i];
            if (a < 0 || a >= this.rows || b < 0 || b >= this.cols) continue;
            if (g[a][b] == 0 && this.check_connectivity(a, b, ex, ey)) {
                g[sx][sy] = 0;
                return true;
            }
        }
        g[sx][sy] = 0;
        return false;
    }
    private boolean draw() { //画地图
        initMap();
        //随机障碍物
        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i++) {
            for (int j = 0; j < 100000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] == 1 || g[this.rows - r - 1][this.cols - c - 1] == 1) continue; //已经有墙了
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue; //排除起点
                g[r][c] = 1;
                g[this.rows - r - 1][this.cols - c - 1] = 1;
                break;
            }
        }
        return this.check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) {
                break;
            }
        }
    }
}
