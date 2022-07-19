import { AcGameObjects } from "./AcGameObjects";
import { Cell } from "./Cell";

export class Snake extends AcGameObjects {
  constructor(info, gamemap) {
    super();

    this.id = info.id;
    this.color = info.color;
    this.gamemap = gamemap;

    this.cells = [new Cell(info.r, info.c)];
    this.next_cell = null; // 下一步目标位置

    this.speed = 5;
    this.direction = -1; // -1表示没有指令 0123=>上右下左
    this.status = "idle"; // idlem move die

    this.dr = [-1, 0, 1, 0];
    this.dc = [0, 1, 0, -1];

    this.step = 0;
    this.eps = 1e-2;

    this.eye_direction = 0;
    if (this.id === 1) this.eye_direction = 2;

    this.eye_dx = [
      [-1, 1],
      [1, 1],
      [1, -1],
      [-1, -1],
    ];
    this.eye_dy = [
      [-1, -1],
      [-1, 1],
      [1, 1],
      [1, -1],
    ];
  }

  start() {}

  set_direction(d) {
    this.direction = d;
  }

  check_tail_increasing() {
    if (this.step <= 10) return true;
    if (this.step % 3 === 1) return true;
    return false;
  }

  next_step() {
    const d = this.direction;
    this.next_cell = new Cell(
      this.cells[0].r + this.dr[d],
      this.cells[0].c + this.dc[d]
    );
    this.eye_direction = d;
    this.direction = -1;
    this.status = "move";
    this.step++;

    const k = this.cells.length;
    for (let i = k; i > 0; i--) {
      this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
    }
    if (!this.gamemap.check_valid(this.next_cell)) {
      this.status = "die";
    }
  }

  update_move() {
    const dx = this.next_cell.x - this.cells[0].x;
    const dy = this.next_cell.y - this.cells[0].y;
    const distance = Math.sqrt(dx * dx + dy * dy);

    if (distance < this.eps) {
      this.cells[0] = this.next_cell;
      this.next_cell = null;
      this.status = "idle";

      if (!this.check_tail_increasing()) {
        this.cells.pop();
      }
    } else {
      const move_distance = (this.speed * this.timedelta) / 1000;
      this.cells[0].x += (move_distance * dx) / distance;
      this.cells[0].y += (move_distance * dy) / distance;

      if (!this.check_tail_increasing()) {
        const len = this.cells.length;
        const tail = this.cells[len - 1],
          tail_target = this.cells[len - 2];
        const tail_dx = tail_target.x - tail.x,
          tail_dy = tail_target.y - tail.y;
        tail.x += (move_distance * tail_dx) / distance;
        tail.y += (move_distance * tail_dy) / distance;
      }
    }
  }

  update() {
    // 每一帧执行一次
    if (this.status === "move") this.update_move();
    this.render();
  }

  render() {
    const L = this.gamemap.L;
    const ctx = this.gamemap.ctx;

    ctx.fillStyle = this.color;
    if (this.status === "die") {
      ctx.fillStyle = "white";
    }
    for (const cell of this.cells) {
      ctx.beginPath();
      ctx.arc(cell.x * L, cell.y * L, (L / 2) * 0.8, 0, Math.PI * 2);
      ctx.fill();
    }

    for (let i = 1; i < this.cells.length; i++) {
      const now = this.cells[i],
        pre = this.cells[i - 1];
      if (
        Math.abs(now.x - pre.x) < this.eps &&
        Math.abs(now.y - pre.y) < this.eps
      ) {
        continue;
      }
      if (Math.abs(now.x - pre.x) < this.eps) {
        ctx.fillRect(
          (now.x - 0.4) * L,
          Math.min(now.y, pre.y) * L,
          L * 0.8,
          Math.abs(now.y - pre.y) * L
        );
      }
      if (Math.abs(now.y - pre.y) < this.eps) {
        ctx.fillRect(
          Math.min(now.x, pre.x) * L,
          (now.y - 0.4) * L,
          Math.abs(now.x - pre.x) * L,
          L * 0.8
        );
      }
    }
    ctx.fillStyle = "black";
    for (let i = 0; i < 2; i++) {
      const eye_x =
          (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L,
        eye_y =
          (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
      ctx.beginPath();
      ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
      ctx.fill();
    }
  }
}
