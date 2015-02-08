package com.snakex.game.model;

import com.snakex.game.CellType;
import com.snakex.game.Room;

import java.util.LinkedList;

/**
 * Created by G.Gekko - 2015.
 * <p/>
 * Класс представляет змею, которая характеризуется своим телом и головой.
 * Так же направлением движения.
 */
public class Snake {

    final Room GAME;

    public Direction direction;             // Текущее направление движения змеи.
    public Direction newDirection;          // Новое направление движения (куда указывает пользователь)
    public boolean isAlive;                 // Жива ли наша змея?
    public final SnakeBody body;                 // Тело змеи.

    public Snake(Room ROOM) {
        GAME = ROOM;

        direction = Direction.UP;
        newDirection = Direction.UP;
        isAlive = true;
        body = new SnakeBody();
        body.addHead(new SnakeSection(GAME.lvl.width / 2, GAME.lvl.height / 2));
        GAME.drawWorld.setType(body.getHead().x, body.getHead().y, CellType.HEAD);
    }

    /*
        Методы отвечающий за пережвижение змеи по текущему направлению.
     */
    public void move() {
        if (!isAlive) return;

        //    Если кусок змеи сытый, то появится новый кусок.
        if (body.getTail().isDigesting) {
            body.getTail().isDigesting = false;
            GAME.addScore(7);
            body.addTail(new SnakeSection(body.getTail()));
        }

        // Меняем направление движения, если нужно
        if (direction != newDirection && direction != newDirection.opposite()){
            direction = newDirection;
        }


        // Переносим элемент из хвоста в голову сдвигая позицию и очищая клетку
        // TODO тут я что-то написал какой то сомнительный код =/ весь блок надо переписать!
        GAME.drawWorld.setType(body.getTail().x, body.getTail().y, CellType.FREE);
        SnakeSection newHead = body.getTail().setNewHeadPosition(body.getHead(), direction, GAME.lvl, this);
        if (!isAlive) {
            // если мы врезались, то хвост неверно делать пустым
            GAME.drawWorld.setType(body.getTail().x, body.getTail().y, CellType.BODY);
            return;
        }
        body.addHead(newHead);
        body.removeTail();


        // Если голова на одной клетке с мышью - едим и создаём новую мышь.
        if (body.getHead().x == GAME.mouse.x && body.getHead().y == GAME.mouse.y) {
            body.getHead().isDigesting = true;
            GAME.addScore(3);
            GAME.mouse.genNewMouse();
        }

        // Меняем состояние клеток мира (дабы перерисавать змею)
        body.updateDrawWorld(GAME.drawWorld);
    }

    /*
        Методы проверки столкновений со стеной. Вернёт true если произойдёт столкновение
     */
    public boolean checkBorders(SnakeSection head) {
        return GAME.lvl.isBorders(head.x, head.y);
    }

    /*
        Методы проверки столкновений с телом змеи. Вернёт true если произойдёт столкновение
     */
    public boolean checkBody(SnakeSection head) {
        return body.isContain(head.x, head.y, head);
    }

    /*
        Класс описывающий направления движения змеи.
        Сделан как nested classes , поскольку направления существует
        только у змеи и таким образом идёт логическая групперовка сущьности.
     */
    public enum Direction {
        UP(0, 1) {
            @Override
            public Direction opposite() {
                return DOWN;
            }
        },
        RIGHT(1, 0) {
            @Override
            public Direction opposite() {
                return LEFT;
            }
        },
        DOWN(0, -1) {
            @Override
            public Direction opposite() {
                return UP;
            }
        },
        LEFT(-1, 0) {
            @Override
            public Direction opposite() {
                return RIGHT;
            }
        };

        int x;
        int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Direction opposite() {
            return null;
        }
    }
}









