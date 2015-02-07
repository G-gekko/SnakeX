package com.snakex.game.model;

import com.snakex.game.Lvl;

/*
    Created by G.Gekko - 2015.
    Класс описывающий кусочек (блок) тела змеи.
*/
public class SnakeSection {
    public int x;
    public int y;
    boolean isDigesting;  // перевариевает ли текущий кусок пищу? Часть игрового момента

    public SnakeSection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SnakeSection(SnakeSection oldSection) {
        x = oldSection.x;
        y = oldSection.y;
    }

    /*
        Метод переносит хвот на место новой головы, делает проверку на столновения, если столкновения есть,
        возвращает старые координаты и убават (меняет флаг жизни) змею.
     */
    public SnakeSection setNewHeadPosition(SnakeSection oldHead, Snake.Direction direction, Lvl lvl, Snake snake) {
        int oldX = x;
        x = oldHead.x + direction.x;
        while (x < 0) x += lvl.width;
        while (x >= lvl.width) x -= lvl.width;

        int oldY = y;
        y = oldHead.y + direction.y;
        while (y < 0) y += lvl.height;
        while (y >= lvl.height) y -= lvl.height;

        if (snake.checkBody(this) || snake.checkBorders(this)){
            snake.isAlive = false;
            x = oldX;
            y = oldY;
        }

        return this;
    }
}











