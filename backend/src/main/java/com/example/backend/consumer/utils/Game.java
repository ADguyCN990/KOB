package com.example.backend.consumer.utils;

import java.util.Random;

public class Game {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;

    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    private int[][] g; //0表示空地1表示墙

    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
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
