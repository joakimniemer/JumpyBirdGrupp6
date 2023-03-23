package se.yrgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    private static final int buttonWidth = 300;
    private static final int buttonHeight = 150;


    Texture playButtonImg;
    Texture exitButtonImg;
    Texture scoreButtonImg;
    private Body playBox;
    final ScreenHandler game;
    OrthographicCamera camera;
    Rectangle rec;


    public MainMenuScreen(final ScreenHandler game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700, 800);
        playButtonImg = new Texture("play.png");
        //scoreButtonImg = new Texture("score.png");
        exitButtonImg = new Texture("exit.png");

        //SKapa en rectangle
        rec = new Rectangle( 100, 100, 100, 100);



    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 1f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        //positionera bild timmsannas med rectangle
        game.batch.draw(playButtonImg, 100, 100,100,100);

        game.font.draw(game.batch, "Welcome to Jumpy Birb!!! ", 250, 700);
        playButton();
        game.batch.draw(playButtonImg, camera.viewportWidth / 2 - buttonWidth / 2, 300, buttonWidth, buttonHeight);
        exitButton();
        game.batch.end();

    }

    private void playButton() {
        float xAxis = camera.viewportWidth / 2 - buttonWidth / 2;
        if (Gdx.input.getX() < xAxis + buttonWidth && Gdx.input.getX() > xAxis && camera.viewportWidth - Gdx.input.getY() < 500 + buttonHeight && camera.viewportWidth - Gdx.input.getY() > 500) {
            game.batch.draw(playButtonImg, camera.viewportWidth / 2 - buttonWidth / 2, 500, buttonWidth, buttonHeight);
        }
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            dispose();
//            game.setScreen(new JumpyBirbScreen(game));
//        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Buttons.LEFT)) {
            game.setScreen(new JumpyBirbScreen(game));
            dispose();
        }

        Buttons
    }

    private void scoreButton() {
        float xAxis = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;


        if (Gdx.input.getX() < xAxis + buttonWidth && Gdx.input.getX() > xAxis && camera.viewportHeight - Gdx.input.getY() < 300 + buttonHeight && camera.viewportHeight - Gdx.input.getY() > 300) {
            // game.batch.draw(scorebButtonImg,  camera.viewportWidth / 2 - buttonWidth / 2, 300, buttonWidth, buttonHeight);
        }
    }

    private void exitButton() {
        float xAxis = Gdx.graphics.getWidth() / 2 - buttonWidth / 2;

        if (Gdx.input.getX() < xAxis + buttonWidth && Gdx.input.getX() > xAxis && camera.viewportHeight - Gdx.input.getY() < 100 + buttonHeight && camera.viewportHeight - Gdx.input.getY() > 100) {
            game.batch.draw(exitButtonImg, camera.viewportWidth / 2 - buttonWidth / 2, 100, buttonWidth, buttonHeight);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Gdx.app.exit();
        }
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
