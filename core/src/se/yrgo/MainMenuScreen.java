package se.yrgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    Texture playButtonImg;
    private Body playBox;
    final ScreenHandler game;
    OrthographicCamera camera;



    public MainMenuScreen(final ScreenHandler game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700, 800);

        //playBox = createBox(32, 16, false, 100, 300);

        playButtonImg = new Texture("play.png");
    }




    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Jumpy Birb!!! ", 100, 150);
        game.batch.draw(playButtonImg, 100, 100, 300, 150);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen((new JumpyBirbScreen(game)));
            dispose();
        }




    }

    private void PlayButton() {

    }


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
}
