package se.yrgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.Scanner;


public class GameOverScreen implements Screen {

    OrthographicCamera camera;
    final ScreenHandler game;
    private int highScore;
    private int currentRoundScore;
    private Scanner scan;
    private long enteringScreenTimer;
    private long currentTime;
    private long delayTimer;
    private int difficulty;

    public GameOverScreen(final ScreenHandler game, int score, int difficulty) {
        this.game = game;
        this.currentRoundScore = score;
        this.scan = new Scanner(System.in);
        this.enteringScreenTimer = TimeUtils.nanoTime();
        this.delayTimer = 2000000000;
        this.difficulty = difficulty;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700, 800);
        highScore = LoadAssets.getHighScore();

    }



    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "You Died!", 175, 400);
        game.font.draw(game.batch, String.format("You got %d score!", currentRoundScore), 175, 350);
        game.font.draw(game.batch, String.format("All time highscore is: %d", highScore), 175, 300);
        game.font.draw(game.batch, "Press space to restart! (2sec freeze delay)", 175, 250);
        game.font.draw(game.batch, "Or press 'ESC' to get back to the main menu", 175, 200);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen((new MainMenuScreen(game)));
        }
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && setDelayTimer() || Gdx.input.isKeyJustPressed(Input.Buttons.LEFT) && setDelayTimer()) {
            game.setScreen(new JumpyBirbScreen(game, difficulty));
            dispose();
        }
    }

    private boolean setDelayTimer() {
        currentTime = TimeUtils.nanoTime();
        return (currentTime > enteringScreenTimer + delayTimer);
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void exitGame() {
        Gdx.app.exit();
    }
}