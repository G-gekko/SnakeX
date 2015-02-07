package com.snakex.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *  Created by G.Gekko - 2015.
 *
 *  Класс отвечает за нарезку текстур под клетки поля, дабы не дублироваться
 *  не изменяемые сущьности, сделан как перечислимый статический.
 */
public enum CellType {

    FREE(new TextureRegion(SnakeX.texturePack, 0, 0, 29, 29)),
    WALL(new TextureRegion(SnakeX.texturePack, 29, 0, 29, 29)),
    BODY(new TextureRegion(SnakeX.texturePack, 58, 0, 29, 29)),
    BODY_EATEN(new TextureRegion(SnakeX.texturePack, 87, 0, 29, 29)),
    HEAD(new TextureRegion(SnakeX.texturePack, 116, 0, 29, 29)),
    HEAD_EATEN(new TextureRegion(SnakeX.texturePack, 145, 0, 29, 29)),
    MOUSE(new TextureRegion(SnakeX.texturePack, 174, 0, 29, 29));

    final TextureRegion texture;
    private CellType(TextureRegion texture) {
        this.texture = texture;
    }
}
