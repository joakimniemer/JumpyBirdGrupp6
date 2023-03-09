package se.yrgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.io.IOException;

public class GameOverScreen implements Screen {

    OrthographicCamera camera;
    final ScreenHandler game;
    private int highScore;
    private int currentRoundScore;

    public GameOverScreen(final ScreenHandler game, int score) {
        this.game = game;
        this.currentRoundScore = score;

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
        game.font.draw(game.batch, "Press space to restart!", 175, 250);
        game.batch.end();


        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Buttons.LEFT)) {
            game.setScreen(new JumpyBirbScreen(game));
            dispose();
        }
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