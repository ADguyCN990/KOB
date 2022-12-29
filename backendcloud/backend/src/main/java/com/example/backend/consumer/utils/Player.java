package com.example.backend.consumer.utils;

import com.baomidou.mybatisplus.generator.config.INameConvert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;
    private Integer botId; //-1表示人工操作
    private String botCode;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;
    private boolean check_tail_increasing(int step) { //检查当前回合蛇的长度是否增加
        if (step <= 10) return true;
        return step % 3 == 1;
    }
    public List<Cell> getCells() { //获取组成蛇的方块坐标
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0; //回合数
        res.add(new Cell(x, y));
        for (int i = 0; i < steps.size(); i++) {
            x = x + dx[steps.get(i)];
            y = y + dy[steps.get(i)];
            res.add(new Cell(x, y));
            if (!check_tail_increasing(++step)) {
                res.remove(0); //如果当前回合蛇的长度不增加，把组成蛇尾的方块删掉
            }
        }
        return res;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int d : steps) {
            res.append(d);
        }
        return res.toString();
    }
}
