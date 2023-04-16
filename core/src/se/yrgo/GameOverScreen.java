package se.yrgo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.IOException;
import java.util.Scanner;


public class GameOverScreen implements Screen {

    OrthographicCamera camera;
    final ScreenHandler game;
    private String newHighScoreName = "";
    private int currentRoundScore;
    private Scanner scan;
    private long enteringScreenTimer;
    private long currentTime;
    private long delayTimer;
    private int difficulty;
    private String text = "              You died!\n          You got %d score\n'ESC' to get back to main menu\n       'Space' to play again";
    private Stage stage;
    private Stage newHighscoreStage;
    private TextField inputLine;
    private boolean showNewHighscoreNameInput;

    public GameOverScreen(final ScreenHandler game, int score, int difficulty) {
        this.game = game;
        this.currentRoundScore = score;
        this.scan = new Scanner(System.in);
        this.enteringScreenTimer = TimeUtils.nanoTime();
        this.delayTimer = 1000000000;
        this.difficulty = difficulty;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700, 800);

        try {
            createTextAndHighscoreBox();
            showNewHighscoreNameInput = LoadAssets.isNewHighscore(currentRoundScore);
        } catch (Exception e) {
            System.err.println("Error when calling createTextAndHighscoreBox: " + e);
        }

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen((new MainMenuScreen(game)));
        }
        game.batch.end();
        stage.act(delta);
        stage.draw();
        try {
            if (showNewHighscoreNameInput) {
                askForNameInput();
                if (!inputLine.getText().equalsIgnoreCase("") && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    LoadAssets.updateHighScore(inputLine.getText(), currentRoundScore);
                    showNewHighscoreNameInput = !showNewHighscoreNameInput;
                }
            }
        } catch (Exception e) {
            System.err.println("Error when checking if new highscore: " + e);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && setDelayTimer() || Gdx.input.isKeyJustPressed(Input.Buttons.LEFT) && setDelayTimer()) {
            game.setScreen(new JumpyBirbScreen(game, difficulty));
            dispose();
        }
    }

    private void askForNameInput() {
        newHighscoreStage.draw();
        newHighscoreStage.act();
    }

    private boolean setDelayTimer() {
        currentTime = TimeUtils.nanoTime();
        return (currentTime > enteringScreenTimer + delayTimer);
    }

    private void createTextAndHighscoreBox() throws IOException {
        Skin mySkin = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        Label labelOne = new Label(String.format(text, currentRoundScore), mySkin, "over");
        labelOne.setSize(100, 100);
        labelOne.setPosition(200, 600);

        Label highScoreText = new Label(String.format("Highscore-list:\n\n%s", LoadAssets.getStringOfHighscores()), mySkin, "over");
        highScoreText.setSize(100, 100);
        highScoreText.setPosition(280, 340);

        List highscoreBackground = new List(mySkin);
        TextField toplineHighscoreBackground = new TextField("", mySkin);
        highscoreBackground.setSize(400, 400);
        highscoreBackground.setPosition(150, 150);
        toplineHighscoreBackground.setSize(385, 20);
        toplineHighscoreBackground.setPosition(158, 545);

        stage = new Stage(new ScreenViewport());
        stage.addActor(labelOne);
        stage.addActor(highscoreBackground);
        stage.addActor(highScoreText);
        stage.addActor(toplineHighscoreBackground);

        newHighscoretext(mySkin);
    }

    private void newHighscoretext(Skin skin) {
        newHighscoreStage = new Stage(new ScreenViewport());
        inputLine = new TextField("", skin);
        inputLine.setSize(200, 25);
        inputLine.setPosition(250, 600);
        Label askForNameInput = new Label("New highscore! Enter name below and press enter", skin, "over");
        askForNameInput.setPosition(100, 630);
        List background = new List(skin);
        background.setSize(600, 60);
        background.setPosition(50, 590);

        newHighscoreStage.addActor(background);
        newHighscoreStage.addActor(askForNameInput);
        newHighscoreStage.addActor(inputLine);
        Gdx.input.setInputProcessor(newHighscoreStage);
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