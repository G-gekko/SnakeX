package com.snakex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *   Created by G.Gekko - 2015.
 *
 *  Класс для отрисовки фона сцены Добавляется на сцену первым и ресует фоновое изображение на весь экран.
 *  В единственный конструктор передаётся адрес текстуры фона.
 *
 *  TODO не прозрачна инициализация - надо настроить приоритет, дабы не путать порядок вызова.
 */
public class Background extends Actor {
    private TextureRegion backgroundTexture;

    public Background(String passTexture){
        backgroundTexture = new TextureRegion(new Texture(Gdx.files.internal(passTexture)), 0, 0, 800, 600);
        setPosition(0, 0);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(backgroundTexture, 0, 0, 800, 600);
    }
}