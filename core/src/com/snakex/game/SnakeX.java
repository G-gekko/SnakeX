package com.snakex.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 *  Created by G.Gekko - 2015.
 *
 *  Основной класс игры, на его основе ведётся управление запуском игры и
 *  показом текущей сцены (экрана)
 */

public class SnakeX extends Game {

    // Для поддержки руский букв инициализируем шрифт
    public static BitmapFont fontMainMenu;
    public static BitmapFont fontRoom;
    public static BitmapFont fontBig;
    private static final String FONT_CHARACTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.:;^,{}\"´`'<>";

    // дабы не создавать кучу однотипных сцен
    public MainMenu mainMenu;
    public LevelSelection levelSelection;

    // Для текстур
    public static Texture texturePack;

    /*
        В LibGDX метод create для класса Game вызывается самым первым, при запуске игры.
        И используется для инициализации и загрузки необходимых сцен и ресурсов.
     */
    @Override
    public void create() {


        // Подключаем русские шрифты. Шрифт RussoOne с Google Fonts.
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/russoone.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.characters = FONT_CHARACTERS;
        // Создаём шрифт главного меню
        param.size = 16;
        fontMainMenu = generator.generateFont(param);
        fontMainMenu.setColor(Color.BLACK);
        // Создаём шрифт игровой сцены
        param.size = 20;
        fontRoom = generator.generateFont(param);
        fontRoom.setColor(Color.BLACK);
        // Создаём шрифт для вывода надписо о конце игры и названия игры
        param.size = 36;
        fontBig = generator.generateFont(param);
        fontBig.setColor(Color.BLACK);
        generator.dispose(); // Уничтожаем наш генератор за ненадобностью.

        // устанавливаем размер экрана приложения и говорим, что оно окно. а не полный экран.
        Gdx.graphics.setDisplayMode(800, 600, false);

        // Подключаем наши тестуры
        texturePack = new Texture(Gdx.files.internal("texturePack.png"));

        // Инициализируем сцены, что нет смысла создавать каждый раз заново.
        mainMenu = new MainMenu(this);
        levelSelection = new LevelSelection(this);

        // Показываем главное меню
        setScreen(mainMenu);
    }

    /*
        Метод для установки новой сцены с освобождением (удалением) текущей.
        Обычный метот setScreen не освобождает ресурсы, а скрывает используемую сцену.
        Вызывая метод hide()
     */
    public void setScreenAndDisposeOld(Screen newScreen) {
        if (getScreen()!= null)
            getScreen().dispose();
        super.setScreen(newScreen);
    }
    /*
        Метод закрытия приложения. Высвобождает русурсы и закрывается.
     */
    public void exitGame(){
        dispose();
        Gdx.app.exit();
    }
}
