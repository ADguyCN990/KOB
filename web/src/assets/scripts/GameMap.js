import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall"

export class GameMap extends AcGameObject {
    constructor(ctx, parent) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;

        this.rows = 13;
        this.cols = 13;

        this.inner_walls_count = 10; //随机障碍物数量
        this.walls = [];

    }

    check_connectivity(g, sx, sy, ex, ey) {
        if (sx == ex && sy == ey) return true; //成功
        g[sx][sy] = true;
        let dx = [0, 0, 1, -1];
        let dy = [1, -1, 0, 0];
        for (let i = 0; i < 4; i++) {
            let a = sx + dx[i], b = sy + dy[i];
            if (!g[a][b] && this.check_connectivity(g, a, b, ex, ey)) {
                return true;
            }
        }
        return false;
    }

    create_walls() {
        const g = [];
        for (let i = 0; i < this.rows; i++) {
            g[i] = [];
            for (let j = 0; j < this.cols; j++) {
                g[i][j] = false;
            }
        }

        //四周是墙
        for (let i = 0; i < this.rows; i++) {
            g[i][0] = true;
            g[i][this.cols - 1] = true;
        }
        for (let i = 0; i < this.cols; i++) {
            g[0][i] = true;
            g[this.rows - 1][i] = true;
        }

        //生成对称的随机障碍物
        for (let i = 0; i < this.inner_walls_count; i++) {
            for (let j = 0; j < 100000; j++) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if (g[r][c] || g[c][r]) continue; //已经有墙了
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue; //排除起点
                g[r][c] = true;
                g[c][r] = true;
                break;
            }
        }

        const copy_g = JSON.parse(JSON.stringify(g));
        if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) {
            return false;
        } 

        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                if (g[i][j]) {
                    this.walls.push(new Wall(i, j, this));
                }
            }
        }

        return true;
    }

    start() {
        for (let i = 0; i < 1000; i++) {
            if (this.create_walls()) {
                break;
            }
        }
        
    }

    update_size() {
        // 计算小正方形的边长
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
        this.render();
    }

    render() {
        // 取颜色
        const color_eve = "#AAD751", color_odd = "#A2D149";
        // 染色
        for (let r = 0; r < this.rows; r++)
            for (let c = 0; c < this.cols; c++) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_eve;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                //左上角左边，明确canvas坐标系
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
    }
}

