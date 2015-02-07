package com.snakex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by G.Gekko - 2015.
 *
 * Класс - сцена, отвечает выбор уровня игры
 *
 */
public class LevelSelection implements Screen {


    /*
        Для поддержания обратной связи с конкретным экземпляром базового класса игры Game
        Мы храним неизменяемую ссылку на него. Т.е. ссылку на объект, что нас породил.
     */
    public final SnakeX GAME;

    private Stage stage;

    /*
         Конструктор, аналог метода create в game
         в нём инициализируем все необходимые параметры
     */
    public LevelSelection(SnakeX GAME) {
        this.GAME = GAME;

        // инициализируем сцену, устанавливая её размеры в размер экрана.
        Viewport viewport = new ScreenViewport();
        viewport.setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);

        // Добавляем фон для главного меню
        stage.addActor(new Background("bgMainMenu.jpg"));

        // Вывод названия сцены
        Label.LabelStyle style = new Label.LabelStyle(SnakeX.fontBig, Color.BLACK);
        Label name = new Label("Выбор уровня:", style);
        name.setPosition(53, 505);
        stage.addActor(name);

        // Подключаем кнопки и прочие элементы управления
        Controller controller = new Controller();
        controller.setPosition(100, 150);
        stage.addActor(controller);

        Gdx.input.setInputProcessor(stage);  // Устанавливаем нашу сцену основным процессором для ввода
        Gdx.input.setCatchBackKey(true);     // Это нужно для того, чтобы пользователь возвращался назад,
                                             // в случае нажатия на кнопку Назад на своем устройстве
    }

    /**
     * Этот метод вызывается для обновления экрана.
     * Параметр delta - сколько секунд прошло с момента последнего вызова этого метода
     */
    @Override
    public void render(float delta) {
        // закраска экрана и очистка его
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Отрисовка сцены и вывод FPS в консоль
        stage.draw();
        Gdx.app.log("GameScreen FPS", (1/delta) + "");
    }



    /*
        Вызывается, когда мы делаем экран видимым (он становится текущим)
    */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }


    /*
        Вызывается, когда мы изменяем размеры графического окна (например, расширяем\сужаем окно мышкой)
    */
    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    /*
        Этот метод при вызывается сворачивание (сокрытии) экрана
    */
    @Override
    public void hide() {

    }

    /*
        Метод вызывается для удаления экрана и освобождения ресурсов
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     *
     *   Класс объединяющий в себе кнопки экрана (все элементы управления) и
     *   Отвечающий за их отображение и обработку.
     *   Объявлен как внутренний класс (inner classes) для LevelSelection в целях группировки логических сущьностей
     *   Класс является Группой, собранной в конструкторе.
     *   Содержит четыре кнопки:
     *      playField - режим поля
     *      playRoom - режим комнаты
     *      playAngle - режим угловой комнаты
     *      returnButton - вернуться в главнео меню
     *
     */
    class Controller extends Group {

        public Controller() {
        /*
                Прогружаем текстуры кнопок игры
        */
            final ImageTextButton.ImageTextButtonStyle stileButton = new ImageTextButton.ImageTextButtonStyle(
                    new TextureRegionDrawable(new TextureRegion(SnakeX.texturePack, 203, 0, 79, 29)),
                    new TextureRegionDrawable(new TextureRegion(SnakeX.texturePack, 203, 30, 79, 29)),
                    new TextureRegionDrawable(new TextureRegion(SnakeX.texturePack, 203, 0, 79, 29)),
                    SnakeX.fontMainMenu);
            stileButton.fontColor = Color.BLACK;

        /*
                Режим комната (кругом стены)
        */
            ImageTextButton playRoom = new ImageTextButton("Комната", stileButton);
            playRoom.setPosition(0, 280);
            playRoom.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.input.vibrate(20);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    GAME.setScreen(new Room(GAME, 1));
                }
            });
            playRoom.center();
            playRoom.setSize(180, 60);
            this.addActor(playRoom);

            /*
                Режим поле (без стен)
            */
            ImageTextButton playField = new ImageTextButton("Поле", stileButton);
            playField.setPosition(0, 210);
            playField.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.input.vibrate(20);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    GAME.setScreen(new Room(GAME, 0));
                }
            });
            playField.center();
            playField.setSize(180, 60);
            this.addActor(playField);

            /*
                Режим угол(частичные стены)
            */
            ImageTextButton playAngle = new ImageTextButton("Уголок", stileButton);
            playAngle.setPosition(0, 140);
            playAngle.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.input.vibrate(20);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    GAME.setScreen(new Room(GAME, 2));
                }
            });
            playAngle.center();
            playAngle.setSize(180, 60);
            this.addActor(playAngle);

        /*
                Кнопка для возврата назад в главное меню
        */
            ImageTextButton exitButton = new ImageTextButton("Назад", stileButton);
            exitButton.setPosition(0, 0);
            exitButton.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.input.vibrate(20);
                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    GAME.setScreen(GAME.mainMenu);
                }
            });
            exitButton.center();
            exitButton.setSize(180, 60);
            this.addActor(exitButton);
        }
    }

}
