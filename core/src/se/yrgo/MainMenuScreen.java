package se.yrgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.IOException;

public class MainMenuScreen implements Screen {

    private enum Buttons {
        PLAY, SCORE, EXIT;
    }

    private Buttons buttonSelected;
    private boolean highScoreShow;
    private Texture backgroundMenu;
    private Stage stage;
    private Stage stageTwo;
    private String instructionsOne = "Choose difficulty:\n1 for easy\n2 for medium\n3 for hard";
    private String instructionsTwo = "Move in the menu\nwith up/down\nselect with space";
    private String instructionsThree = "(Exit with backspace)";

    final ScreenHandler game;
    OrthographicCamera camera;

    //Menyknappar
    Texture playButton;
    Texture exitButton;
    Texture scoreButton;
    Texture playButtonSelected;
    Texture exitButtonSelected;
    Texture scoreButtonSelected;
    Rectangle playRec;
    Rectangle exitRec;
    Rectangle scoreRec;

    //Svårighetsgrad-knappar
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

    private Music menuMusic;


    public MainMenuScreen(final ScreenHandler game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700, 800);

        //Skapa rectanglar och texturerer för svårighetsgrad
        recEasy = new Rectangle(50, 600, 150, 100);
        recMedium = new Rectangle(40, 518, 150, 100);
        recHard = new Rectangle(40, 450, 150, 100);
        easy = new Texture("MenuAssets/Easy.png");
        easySelected = new Texture("MenuAssets/EasyPressed.png");
        medium = new Texture("MenuAssets/Medium.png");
        mediumSelected = new Texture("MenuAssets/MediumPressed.png");
        hard = new Texture("MenuAssets/Hard.png");
        hardSelected = new Texture("MenuAssets/HardPressed.png");

        //Skapa rectanglar och texturerer för menyknapparna
        playRec = new Rectangle(225, 500, 250, 100);
        scoreRec = new Rectangle(220, 325, 250, 100);
        exitRec = new Rectangle(220, 165, 250, 100);
        playButton = new Texture("MenuAssets/play.png");
        playButtonSelected = new Texture("MenuAssets/playSelected.png");
        scoreButton = new Texture("MenuAssets/HighScore.png");
        scoreButtonSelected = new Texture("MenuAssets/HighscorePressed.png");
        exitButton = new Texture("MenuAssets/Exit.png");
        exitButtonSelected = new Texture("MenuAssets/ExitPressed.png");

        difficulty = 1;

        //Sätter "buttonSelected" till play som start
        buttonSelected = Buttons.PLAY;

        //Initerar highScoreMenu till false för att dölja den tills det väljs
        highScoreShow = false;

        //ladda highscore diff bakgrund
        backgroundMenu = new Texture("MenuAssets/backgroundMenu.png");


        try {
            createText();
        } catch (IOException e) {
            System.err.println("Exception thrown when createText was called: " + e);
        }

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/GameMusic.ogg"));
        menuMusic.play();

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundMenu, 0, 0, 700, 800);
        chooseDifficulty();
        menuButtons();
        moveInMenu();
        drawInstructions(delta);
        difficulty();
        game.batch.end();
        highScoreMenu(delta);
    }

    private void createText() throws IOException {
        Skin mySkin = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        Label labelOne = new Label(instructionsOne, mySkin, "over");
        labelOne.setSize(100, 100);
        labelOne.setPosition(30, 700);

        Label labelTwo = new Label(instructionsTwo, mySkin, "over");
        labelTwo.setSize(100, 100);
        labelTwo.setPosition(500, 500);

        Label highScoreText = new Label(String.format("Highscore-list:\n\n%s", LoadAssets.getStringOfHighscores()), mySkin, "over");
        highScoreText.setSize(100, 100);
        highScoreText.setPosition(280, 440);

        List highscoreBackground = new List(mySkin);
        TextField toplineHighscoreBackground = new TextField("", mySkin);
        Label exitHighscoreInstructions = new Label(instructionsThree, mySkin, "over");
        highscoreBackground.setSize(400, 400);
        highscoreBackground.setPosition(150, 250);
        toplineHighscoreBackground.setSize(385, 20);
        toplineHighscoreBackground.setPosition(158, 645);
        exitHighscoreInstructions.setSize(100, 100);
        exitHighscoreInstructions.setPosition(250, 230);

        List diffBackground = new List(mySkin);
        TextField toplineDiffBackground = new TextField("", mySkin);
        TextField diffPlayConnectionLine = new TextField("", mySkin);
        diffBackground.setSize(200, 250);
        diffBackground.setPosition(28, 440);
        toplineDiffBackground.setSize(185, 20);
        toplineDiffBackground.setPosition(35, 685);
        diffPlayConnectionLine.setSize(20, 20);
        diffPlayConnectionLine.setPosition(215, 545);

        stage = new Stage(new ScreenViewport());
        stage.addActor(labelOne);
        stage.addActor(labelTwo);
        stage.addActor(diffBackground);
        stage.addActor(toplineDiffBackground);
        stage.addActor(diffPlayConnectionLine);

        stageTwo = new Stage(new ScreenViewport());
        stageTwo.addActor(highscoreBackground);
        stageTwo.addActor(highScoreText);
        stageTwo.addActor(toplineHighscoreBackground);
        stageTwo.addActor(exitHighscoreInstructions);

    }

    private void drawInstructions(float delta) {
        stage.act(delta);
        stage.draw();
    }

    private void highScoreMenu(float delta) {
        if (highScoreShow == true) {
            stageTwo.act(delta);
            stageTwo.draw();
        }
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
            game.batch.draw(easySelected, recEasy.x, recEasy.y, 150, 75);
            game.batch.draw(medium, recMedium.x, recMedium.y, 170, 95);
            game.batch.draw(hard, recHard.x, recHard.y, 170, 95);
        }
        if (difficulty == 2) {
            game.batch.draw(easy, recEasy.x, recEasy.y, 150, 75);
            game.batch.draw(mediumSelected, recMedium.x, recMedium.y, 170, 95);
            game.batch.draw(hard, recHard.x, recHard.y, 170, 95);
        }
        if (difficulty == 3) {
            game.batch.draw(easy, recEasy.x, recEasy.y, 150, 75);
            game.batch.draw(medium, recMedium.x, recMedium.y, 170, 95);
            game.batch.draw(hardSelected, recHard.x, recHard.y, 170, 95);
        }
    }

    private void menuButtons() {
        if (buttonSelected == Buttons.PLAY) {
            game.batch.draw(playButtonSelected, playRec.x, playRec.y, 250, 100);
            game.batch.draw(scoreButton, scoreRec.x, scoreRec.y, 260, 120);
            game.batch.draw(exitButton, exitRec.x, exitRec.y, 260, 120);
        }
        if (buttonSelected == Buttons.SCORE) {
            game.batch.draw(playButton, playRec.x, playRec.y, 250, 100);
            game.batch.draw(scoreButtonSelected, scoreRec.x, scoreRec.y, 260, 110);
            game.batch.draw(exitButton, exitRec.x, exitRec.y, 260, 120);
        }
        if (buttonSelected == Buttons.EXIT) {
            game.batch.draw(playButton, playRec.x, playRec.y, 250, 100);
            game.batch.draw(scoreButton, scoreRec.x, scoreRec.y, 260, 120);
            game.batch.draw(exitButtonSelected, exitRec.x, exitRec.y, 260, 110);
        }
        buttonEvents();
    }

    private void buttonEvents() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && buttonSelected == Buttons.PLAY) {
            dispose();
            game.setScreen(new JumpyBirbScreen(game, difficulty));
            menuMusic.stop();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && buttonSelected == Buttons.SCORE) {
            highScoreShow = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && buttonSelected == Buttons.EXIT) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && highScoreShow == true) {
            highScoreShow = false;
        }
    }

    private void moveInMenu() {
        //press down
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && buttonSelected == Buttons.PLAY) {
            buttonSelected = Buttons.SCORE;
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && buttonSelected == Buttons.SCORE) {
            buttonSelected = Buttons.EXIT;
            return;
        }
        //press up
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && buttonSelected == Buttons.EXIT) {
            buttonSelected = Buttons.SCORE;
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && buttonSelected == Buttons.SCORE) {
            buttonSelected = Buttons.PLAY;
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
        easy.dispose();
        easySelected.dispose();
        medium.dispose();
        mediumSelected.dispose();
        hard.dispose();
        hardSelected.dispose();
        playButton.dispose();
        playButtonSelected.dispose();
        scoreButton.dispose();
        scoreButtonSelected.dispose();
        exitButton.dispose();
        exitButtonSelected.dispose();
        backgroundMenu.dispose();
        menuMusic.dispose();
    }
}
