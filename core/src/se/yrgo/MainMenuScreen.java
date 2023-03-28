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

    Rectangle recEasy;
    Rectangle recMedium;
    Rectangle recHard;
    Texture easy;
    Texture easySelected;
    Texture medium;
    Texture mediumSelected;
    Texture hard;
    Texture hardSelected;
    int difficulty;


    public MainMenuScreen(final ScreenHandler game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700, 800);
        playButtonImg = new Texture("play.png");
        //scoreButtonImg = new Texture("score.png");
        exitButtonImg = new Texture("exit.png");

        //Skapa rectanglar och texturerer för svårighetsgrad
        recEasy = new Rectangle(75, 600, 100, 100);
        recMedium = new Rectangle(75, 475, 100, 100);
        recHard = new Rectangle(75, 350, 100, 100);
        easy = new Texture("easy.png");
        easySelected = new Texture("easySelected.png");
        medium = new Texture("medium.png");
        mediumSelected = new Texture("mediumSelected.png");
        hard = new Texture("hard.png");
        hardSelected = new Texture("hardSelected.png");

        //Sätter diff:s startvärde till 1 (default blir easy).
        // OBS! Denna måste också skickas med till gameScreenen
        difficulty = 1;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 1f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        //Väljer svårighetsgrad med 1,2,3
        chooseDifficulty();
        //Skriver ut svårighetsgrader och highlightar vald svårighetsgrad (default easy)
        difficulty();


        game.font.draw(game.batch, "Welcome to Jumpy Birb!", 250, 700);
        game.font.draw(game.batch, "Choose difficulty by pressing 1 for easy (default),", 250, 680);
        game.font.draw(game.batch, "2 for medium and 3 for hard.", 250, 660);
        game.font.draw(game.batch, "Then press space to start!", 250, 640);
        playButton();
        game.batch.draw(playButtonImg, camera.viewportWidth / 2 - buttonWidth / 2, 300, buttonWidth, buttonHeight);
        exitButton();
        game.batch.end();

    }

    private void chooseDifficulty() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            difficulty = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            difficulty = 2;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            difficulty = 3;
        }
    }

    private void difficulty() {
        if (difficulty == 1) {
            game.batch.draw(easySelected, recEasy.x, recEasy.y, 100, 100);
            game.batch.draw(medium, recMedium.x, recMedium.y, 100, 100);
            game.batch.draw(hard, recHard.x, recHard.y, 100, 100);
        }
        if (difficulty == 2) {
            game.batch.draw(easy, recEasy.x, recEasy.y, 100, 100);
            game.batch.draw(mediumSelected, recMedium.x, recMedium.y, 100, 100);
            game.batch.draw(hard, recHard.x, recHard.y, 100, 100);
        }
        if (difficulty == 3) {
            game.batch.draw(easy, recEasy.x, recEasy.y, 100, 100);
            game.batch.draw(medium, recMedium.x, recMedium.y, 100, 100);
            game.batch.draw(hardSelected, recHard.x, recHard.y, 100, 100);
        }
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
            dispose();
            game.setScreen(new JumpyBirbScreen(game, difficulty));
        }

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
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            Gdx.app.exit();
//        }
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
