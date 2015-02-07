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
    public LinkedList<SnakeSection> body;   // хвост находится в начале списка, голова в конце.

    public Snake(Room ROOM) {
        GAME = ROOM;

        direction = Direction.UP;
        newDirection = Direction.UP;
        isAlive = true;
        body = new LinkedList<SnakeSection>();
        body.add(new SnakeSection(GAME.lvl.width / 2, GAME.lvl.height / 2));
        GAME.drawWorld.setType(getHead().x, getHead().y, CellType.HEAD);
    }

    /*
        Методы отвечающий за пережвижение змеи по текущему направлению.
     */
    public void move() {
        if (!isAlive) return;

        //    Если кусок змеи сытый, то появится новый кусок.
        if (getTail().isDigesting) {
            getTail().isDigesting = false;
            GAME.addScore(7);
            body.addFirst(new SnakeSection(getTail()));
        }

        // Меняем направление движения, если нужно
        if (direction != newDirection && direction != newDirection.opposite()){
            direction = newDirection;
        }


        // Переносим элемент из хвоста в голову сдвигая позицию и очищая клетку
        // TODO тут я что-то написал какой то сомнительный код =/ весь блок надо переписать!
        GAME.drawWorld.setType(getTail().x, getTail().y, CellType.FREE);
        SnakeSection newHead = getTail().setNewHeadPosition(getHead(), direction, GAME.lvl, this);
        if (!isAlive) {
            // если мы врезались, то хвост неверно делать пустым
            GAME.drawWorld.setType(getTail().x, getTail().y, CellType.BODY);
            return;
        }
        body.addLast(newHead);
        body.removeFirst();


        // Если голова на одной клетке с мышью - едим и создаём новую мышь.
        if (getHead().x == GAME.mouse.x && getHead().y == GAME.mouse.y) {
            getHead().isDigesting = true;
            GAME.addScore(3);
            GAME.mouse.genNewMouse();
        }

        // Меняем состояние клеток мира (дабы перерисавать змею)
        for (SnakeSection currSection : body) {
            if (currSection.isDigesting)
                GAME.drawWorld.setType(currSection.x, currSection.y, CellType.BODY_EATEN);
            else
                GAME.drawWorld.setType(currSection.x, currSection.y, CellType.BODY);
        }
        if (getHead().isDigesting)
            GAME.drawWorld.setType(getHead().x, getHead().y, CellType.HEAD_EATEN);
        else
            GAME.drawWorld.setType(getHead().x, getHead().y, CellType.HEAD);
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
        for (SnakeSection currSection : body) {
            if (currSection != head &&
                    currSection.x == head.x && currSection.y == head.y) {
                return true;
            }
        }
        return false;
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

    /*
         Методы для улучшения читаемости кода, заменяют путанные и непонятные getLast и getFirst
         на методы содержащие смысловую подсказку в имени hetHead и getTail

         TODO Имеет смысл сделать аналогичные методы для set (add) элементов
     */
    public SnakeSection getHead() {
        return body.getLast();
    }

    private SnakeSection getTail() {
        return body.getFirst();
    }
}









