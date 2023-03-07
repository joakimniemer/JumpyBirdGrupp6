package se.yrgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameOverScreen implements Screen {

    OrthographicCamera camera;
    final ScreenHandler game;
    private int highScore;

    public GameOverScreen(final ScreenHandler game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700, 800);
        highScore = LoadAssets.getHighScore();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "You Died!", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to restart!", 100, 100);
        game.font.draw(game.batch, String.format("All time highscore is: %d",highScore), 100, 50);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Buttons.LEFT)) {
            game.setScreen(new JumpyBirbScreen(game));
            dispose();
        }
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