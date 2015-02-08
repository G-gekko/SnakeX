package com.snakex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.snakex.game.model.*;

/**
 * Created by G.Gekko - 2015.
 * Класс ответственный за игровую сцену.
 */
public class Room implements Screen {

    /*
        Для поддержания обратной связи с конкретным экземпляром базового класса игры Game
        Мы храним неизменяемую ссылку на него. Т.е. ссылку на объект, что нас породил.
     */
    public final SnakeX GAME;

    private Stage stage;

    public final Lvl lvl;
    public final Snake snake;
    public final Mouse mouse;
    public final DrawWorld drawWorld;

    private int score;

    /*
         Конструктор, аналог метода create в game
         в нём инициализируем все необходимые параметры
     */
    public Room(SnakeX GAME, int key) {
        this.GAME = GAME;

        // инициализируем сцену, устанавливая её размеры в размер экрана.
        Viewport viewport = new ScreenViewport();
        viewport.setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);

        // Добавляем фон для главного меню
        stage.addActor(new Background("bgRoom.jpg"));

        // TODO тут всё плохо - не прозрачен порядок инициализации. Нужно переписать.
        lvl = new Lvl(key);
        drawWorld = new DrawWorld();
        stage.addActor(drawWorld);
        snake = new Snake(this);
        mouse = new Mouse(this);

        // Отображение очков
        Label.LabelStyle style = new Label.LabelStyle(SnakeX.fontRoom, Color.BLACK);
        Label scoreLabel = new Label("", style) {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                this.setText("Score: " + String.valueOf(score));
                super.draw(batch, parentAlpha);
            }
        };
        scoreLabel.setPosition(22, 567);
        stage.addActor(scoreLabel);
        
        // Таймер игровой паузы
        style = new Label.LabelStyle(SnakeX.fontBig, Color.BLACK);
        gameTimer = new Label(String.valueOf(timer), style) {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (timer > 0)
                    this.setText(String.valueOf(timer));
                super.draw(batch, parentAlpha);
            }
        };
        gameTimer.setAlignment(0);
        gameTimer.setPosition(380, 300);
        stage.addActor(gameTimer);

        // Подключаем управление стрелками для змеи.
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.LEFT:
                        snake.newDirection = Snake.Direction.LEFT;
                        break;
                    case Input.Keys.RIGHT:
                        snake.newDirection = Snake.Direction.RIGHT;
                        break;
                    case Input.Keys.UP:
                        snake.newDirection = Snake.Direction.UP;
                        break;
                    case Input.Keys.DOWN:
                        snake.newDirection = Snake.Direction.DOWN;
                        break;
                }
                return true;
            }
        });

        // Подключаем управление по касанию или клику мыши
        stage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Snake.Direction newDirection = snake.newDirection;

                // Поскольку в метод передаются координаты пикселя экрана, а мы работаем
                // с координатами массива, то расчитываем пиксель относительно координат
                // ячейка массива по фордуле: x*29+20+2+18
                // 29 - параметр клетки,
                // 20 - стартовое смещение игрового поля относительно экрана,
                // 2 - смещение клеток поля внутри группы,
                // 18 - (половина клетки) поскольку берём центральный пиксель клетки)
                if (x < snake.getHead().x * 29 + 20 + 2 + 18)
                    newDirection = Snake.Direction.LEFT;
                if (x > snake.getHead().x * 29 + 20 + 2 + 18)
                    newDirection = Snake.Direction.RIGHT;

                // Если мы сейчас уже двигаемся по X то двигаемся по Y
                if (newDirection == snake.direction || newDirection.opposite() == snake.direction) {
                    if (y > snake.getHead().y * 29 + 20 + 2 + 18)
                        newDirection = Snake.Direction.UP;
                    if (y < snake.getHead().y * 29 + 20 + 2 + 18)
                        newDirection = Snake.Direction.DOWN;
                }
                snake.newDirection = newDirection;
                return true;
            }
        });


        Gdx.input.setInputProcessor(stage);  // Устанавливаем нашу сцену основным процессором для ввода
        Gdx.input.setCatchBackKey(true);     // Это нужно для того, чтобы пользователь возвращался назад,
        // в случае нажатия на кнопку Назад на своем устройстве
    }

    public void addScore(int x) {
        score += x;
    }


    /**
     * Этот метод вызывается для обновления экрана.
     * Параметр delta - сколько секунд прошло с момента последнего вызова этого метода
     * переменная semmDelta суммирует delta, и используется для контроля скорости обработки логики, т.е.
     * регулировки скорости змеи.
     */
    private float summDelta;
    private boolean isGameOver = false;

    // Таймер игровой паузы
    private Label gameTimer;
    private int timer = 3;

    @Override
    public void render(float delta) {
        // закраска экрана и очистка его
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        summDelta += delta;
        
        // Если игра только началась, или вернулась с паузы, то пускаем таймер
        if (timer > 0) {
           if (summDelta >= 1.0){
               timer--;
               summDelta = 0;
           }
        } else {
            gameTimer.setVisible(false);
        }
        
        // пересчёт игровой логики (движение змеи) через определённый временной интервал, если змея жива.
        if (timer <= 0 && snake.isAlive && summDelta >= 0.2) {
            summDelta = 0;
            snake.move();
        }

        // Если змея метра то мы завершаем игру
        if (!snake.isAlive) {
            // Если надпись о конце игры не инициализирована, то делаем это и выводим её.
            // Так же отмечааем заминку в секунду, чтобы пользователь случайно не
            // прокликал конец игры
            if (!isGameOver) {
                Label.LabelStyle style = new Label.LabelStyle(SnakeX.fontBig, Color.BLACK);
                Label gameOver = new Label("Game over\nscore: " + String.valueOf(score), style);
                gameOver.setAlignment(0);
                gameOver.setPosition(300, 300);
                stage.addActor(gameOver);
                isGameOver = true;
                Gdx.input.vibrate(20);
                summDelta = 0;
            } else {
                if (summDelta > 1 && Gdx.input.isTouched()) {
                    GAME.setScreenAndDisposeOld(GAME.mainMenu);
                }
            }
        }

        //Отрисовка сцены и вывод в консоль fps в консоль
        stage.draw();
        Gdx.app.log("GAME FPS: ", (1 / delta) + "");
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
        timer = 3;
        gameTimer.setVisible(true);
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

    /*
    Класс отвечающий за отрисовку игрового поля, собирательный класс для Cell
    Объявлен как внутренний класс (inner classes) для room в целях логического
    объединения сущьности и доступа к переменным текущей комнаты. (объекта Room)
    */
    public class DrawWorld extends Group {
        public final Cell[][] world;

        public DrawWorld() {
            world = new Cell[lvl.width][lvl.height];
            for (int i = 0; i < lvl.width; i++) {
                for (int j = 0; j < lvl.height; j++) {
                    world[i][j] = new Cell(i, j);
                    this.addActor(world[i][j]);

                    // Если тут стена, то меняем тип клетки
                    if (lvl.isBorders(i, j))
                        world[i][j].setTexture(CellType.WALL);
                }
            }
            // Устанавливаем размеры для группы клеток, и смещение для группы относительно сцены
            setPosition(20, 20);
            setSize(lvl.width * 29 + 4, lvl.height * 29 + 4);
        }

        public void setType(int x, int y, CellType type) {
            world[x][y].setTexture(type);
        }

        /*
         Класс отвечающий за ячейку игрового поля.
         Каждая ячейка характеризуется координатами на поле x, y
         так же типом, содержащим текстуру, которую клетка должсна рисовать.
        */
        class Cell extends Actor {
            private CellType type;

            private int x;
            private int y;

            public Cell(int x, int y) {
                this.x = x;
                this.y = y;
                type = CellType.FREE;
            }

            public void setTexture(CellType type) {
                this.type = type;
            }

            @Override
            public void draw(Batch batch, float alpha) {
                batch.draw(type.texture, 2 + x * 29, 2 + y * 29, 29, 29);
            }

        }
    }
}



