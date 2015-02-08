package com.snakex.game.model;

import com.snakex.game.CellType;
import com.snakex.game.Room;

/**
 * Created by G.Gekko - 2015.
 *
 * Клас мыши, описывает цель, поедаемую змеёй.
 */
public class Mouse {

    final Room GAME;

    int x;
    int y;

    public Mouse(Room ROOM) {
        GAME = ROOM;
        this.genNewMouse();
    }

    /*
        Метод создаёт новую мышку (меняет координаты съединой)
        случайным образом, в рамках поля, и проверяет чтобы не на теле змеи.
     */
    public void genNewMouse() {
        genNewMouse(1);
    }

    public void genNewMouse(int genCnt) {

        // если мы 5 и более раз пытались случайным образом создать мыш, но не получилось
        // делаем простой перебор в поисках свободной позиции.
        // если такой нет, то змея умирает (всё поле заполнено)
        
        if (genCnt >= 5) {
            for (int x = 0; x < GAME.lvl.width; x++)
                for (int y = 0; y < GAME.lvl.height; y++) {
                    if (!GAME.snake.body.isContain(x, y)) {
                        this.x = x;
                        this.y = y;
                        GAME.drawWorld.setType(x, y, CellType.MOUSE);
                        return;
                    }
                }
            GAME.snake.isAlive = false;
            return;
        }

        // пытаемся случайным образом создать мышь, если не получается
        // увеличиваем счётчик попыток
        int x = (int) (Math.random() * GAME.lvl.width);
        int y = (int) (Math.random() * GAME.lvl.height);

        if (GAME.snake.body.isContain(x, y)) {
            genNewMouse(genCnt + 1);
        } else {
            this.x = x;
            this.y = y;
            GAME.drawWorld.setType(x, y, CellType.MOUSE);
            return;
        }
    }
}
