package com.snakex.game;

/**
 * Created by G.Gekko - 2015.
 *
 *  Класс отвечающий за игровое поле - стены на нём.
 *
 *  TODO генерация уровней сделана на спех, надо переписать!
 *
 */
public class Lvl {

    public final int width = 26;
    public final int height = 18;
    private boolean[][] world = new boolean[width][height];  // false - поле, true - стена

    public Lvl(int key){

        // Генерируем комнату, стены по периметру
        if (key == 1) {
            for (int i = 0; i < width; i++) {
                world[i][0] = true;
                world[i][height - 1] = true;
            }

            for (int i = 0; i < height; i++) {
                world[0][i] = true;
                world[width - 1][i] = true;
            }
        }

        // Генерируем угол, частичные стены
        if (key == 2) {
            for (int i = 0; i < width - 5; i++) {
                world[i][0] = true;
                world[i][height - 1] = true;
            }

            for (int i = 0; i < height - 5; i++) {
                world[0][i] = true;
                world[width - 1][i] = true;
            }
            world[width-1][height - 1] = true;
        }
    }

    public boolean isBorders(int x, int y){
        if (x < 0 || x >= width || y < 0 || y >= height)
            return true;
        return world[x][y];
    }
}
