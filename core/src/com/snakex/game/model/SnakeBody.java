package com.snakex.game.model;

import com.snakex.game.CellType;
import com.snakex.game.Room;

import java.util.LinkedList;

/**
 * Created by G.Gekko - 2015.
 * 
 * Класс определяющий тело змеи, представленное в виде списка из SnakeSection
 *
 */
public class SnakeBody {
    private LinkedList<SnakeSection> body;

    public SnakeBody(){
        body = new LinkedList<SnakeSection>();
    }
    
    public void addHead(SnakeSection section){
        body.add(section);
    }
    
    public void addTail(SnakeSection section){
        body.addFirst(section);
    }
    
    public void removeTail(){
        body.removeFirst();
    }
    
    public SnakeSection getHead() {
        return body.getLast();
    }

    public SnakeSection getTail() {
        return body.getFirst();
    }

    // Проверяем, есть ли фрагмент змеи по заданным координатам
    public boolean isContain(int x, int y){
        return isContain(x, y, null);
    }
    //Проверяем, есть ли кусок змеи по заданным координатам игнорируя определённый фрагмент
    public boolean isContain(int x, int y, SnakeSection ignore){
        for (SnakeSection currSection : body) {
            if (currSection != ignore &&
                    currSection.x == x && currSection.y == y) {
                return true;
            }
        }
        return false;
    }
    
    // Обновляем состояние клеток мира, для отрисовки актуального состояния
    public void updateDrawWorld(Room.DrawWorld drawWorld){
        for (SnakeSection currSection : body) {
            if (currSection.isDigesting)
                drawWorld.setType(currSection.x, currSection.y, CellType.BODY_EATEN);
            else
                drawWorld.setType(currSection.x, currSection.y, CellType.BODY);
        }
        if (getHead().isDigesting)
            drawWorld.setType(getHead().x, getHead().y, CellType.HEAD_EATEN);
        else
            drawWorld.setType(getHead().x, getHead().y, CellType.HEAD);
    }
    
    
}
