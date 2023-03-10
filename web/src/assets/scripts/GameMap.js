import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall"
import { Snake } from "./Snake"

export class GameMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.store = store;

        this.rows = 13;
        this.cols = 14;

        this.inner_walls_count = 12; //随机障碍物数量
        this.walls = [];

        this.snakes = [
            new Snake({id: 0, color: "#4876ec", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#f94848", r: 1, c: this.cols - 2}, this),
        ]

    }


    create_walls() {
        const g = this.store.state.pk.gamemap;


        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                if (g[i][j]) {
                    this.walls.push(new Wall(i, j, this));
                }
            }
        }

        return true;
    }

    check_ready() { //检测两条蛇是否准备好下一回合的移动
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false;
            if (snake.direction === -1) return false;
        }
        return true;
    }

    add_listening_events() {
        if (this.store.state.record.is_record) {
            let k = 0;
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const loser = this.store.state.record.record_loser;
            const [snake0, snake1] = this.snakes;
            const interval_id = setInterval(() => {
                // 将除死亡的操作每300ms泫渲染出来
                if (k >= a_steps.length - 1) {
                    if (loser === "all" || loser === "A") {
                        snake0.status = "die";
                    }
                    if (loser === "all" || loser === "B") {
                        snake1.status = "die";
                    }
                    clearInterval(interval_id);
                } else {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++;
            }, 300);
        } 
        else {
            this.ctx.canvas.focus();
            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1;
                if (e.key === 'w') d = 0;
                else if (e.key === 'd') d = 1;
                else if (e.key === 's') d = 2;
                else if (e.key === 'a') d = 3;
                // else if(e.key === 'ArrowUp') snake1.set_direction(0);
                // else if(e.key === 'ArrowRight') snake1.set_direction(1);
                // else if(e.key === 'ArrowDown') snake1.set_direction(2);
                // else if(e.key === 'ArrowLeft') snake1.set_direction(3);

                if (d >= 0) {
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                    }));
                }
            });
        }
    }


    start() {
        this.add_listening_events();
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

    next_step() {
        for (let snake of this.snakes) {
            snake.next_step();
        }
    }

    check_valid(cell) { //检查下一步是否合法
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c) {
                return false;
            }
        }
        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) { //此时不要判断蛇尾
                k--;
            }
            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) {
                    return false;
                }
            }
        }
        return true;
    }

    update() {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
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

