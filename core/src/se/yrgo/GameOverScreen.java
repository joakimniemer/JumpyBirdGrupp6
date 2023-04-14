package se.yrgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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
    private String text = "You died!\nYou got %d score.\nAll time highscore is: %d.\nPress space to restard,\n'ESC' to get back to main menu.";
    private Stage stage;

    public GameOverScreen(final ScreenHandler game, int score, int difficulty) {
        this.game = game;
        this.currentRoundScore = score;
        this.scan = new Scanner(System.in);
        this.enteringScreenTimer = TimeUtils.nanoTime();
        this.delayTimer = 1000000000;
        this.difficulty = difficulty;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700, 800);
        highScore = LoadAssets.getHighScore();

        createText();
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

    private void createText() {
        Skin mySkin = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        Label labelOne = new Label(String.format(text,currentRoundScore,highScore), mySkin, "over");
        labelOne.setSize(100, 100);
        labelOne.setPosition(30, 700);

        stage = new Stage(new ScreenViewport());
        stage.addActor(labelOne);
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