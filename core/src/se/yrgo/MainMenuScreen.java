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

    private enum Buttons {
        PLAY, SCORE, EXIT;
    }

    private Buttons buttonSelected;
    private boolean highScoreShow;
    private Texture highScoreTabBackGround;


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


    private Body playBox;
    final ScreenHandler game;
    OrthographicCamera camera;

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


    public MainMenuScreen(final ScreenHandler game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700, 800);


        //Skapa rectanglar och texturerer för svårighetsgrad
        recEasy = new Rectangle(50, 600, 150, 75);
        recMedium = new Rectangle(50, 525, 100, 100);
        recHard = new Rectangle(50, 450, 100, 100);
        easy = new Texture("easy.png");
        easySelected = new Texture("easySelected.png");
        medium = new Texture("medium.png");
        mediumSelected = new Texture("mediumSelected.png");
        hard = new Texture("hard.png");
        hardSelected = new Texture("hardSelected.png");

        //Skapa rectanglar och texturerer för menyknapparna
        playRec = new Rectangle(225, 500, 250, 100);
        scoreRec = new Rectangle(225, 325, 250, 100);
        exitRec = new Rectangle(225, 150, 250, 100);
        playButton = new Texture("play.png");
        scoreButton = new Texture("play.png");
        exitButton = new Texture("exit.png");
        playButtonSelected = new Texture("playSelected.png");
        scoreButtonSelected = new Texture("play.png");
        exitButtonSelected = new Texture("play.png");


        //Sätter diff:s startvärde till 1 (default blir easy).
        // OBS! Denna måste också skickas med till gameScreenen
        difficulty = 1;

        //Sätter "buttonSelected" till play som start
        buttonSelected = Buttons.PLAY;

        //Initerar highScoreMenu till false för att dölja den tills det väljs
        highScoreShow = false;

        //ladda highscore tab bakgrund
        highScoreTabBackGround = new Texture("highScoreTab.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        //Väljer svårighetsgrad med 1,2,3
        chooseDifficulty();
        //Skriver ut svårighetsgrader och highlightar vald svårighetsgrad (default easy)
        difficulty();
        //Skriver ut menuknappar och highligthar vald knapp (börjar på play)
        menuButtons();
        //Bläddra i menyn med piltangenterna
        moveInMenu();


        game.font.draw(game.batch, "Welcome to Jumpy Birb!", 250, 750);
        game.font.draw(game.batch, "Choose difficulty by pressing 1 for easy (default),", 250, 730);
        game.font.draw(game.batch, "2 for medium and 3 for hard.", 250, 710);
        game.font.draw(game.batch, "Move in the menu with \"up\" and \"down\".", 250, 690);
        game.font.draw(game.batch, "Select with space", 250, 670);

        //Highscore menu
        highScoreMenu();
        game.batch.end();

    }

    private void highScoreMenu() {
        if (highScoreShow == true) {
            game.batch.draw(highScoreTabBackGround, 100,100,500,600);
            game.font.draw(game.batch, String.format("All time highscore: %d", LoadAssets.getHighScore()), 250, 400);
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
            game.batch.draw(medium, recMedium.x, recMedium.y, 150, 75);
            game.batch.draw(hard, recHard.x, recHard.y, 150, 75);
        }
        if (difficulty == 2) {
            game.batch.draw(easy, recEasy.x, recEasy.y, 150, 75);
            game.batch.draw(mediumSelected, recMedium.x, recMedium.y, 150, 75);
            game.batch.draw(hard, recHard.x, recHard.y, 150, 75);
        }
        if (difficulty == 3) {
            game.batch.draw(easy, recEasy.x, recEasy.y, 150, 75);
            game.batch.draw(medium, recMedium.x, recMedium.y, 150, 75);
            game.batch.draw(hardSelected, recHard.x, recHard.y, 150, 75);
        }
    }

    private void menuButtons() {
        if (buttonSelected == Buttons.PLAY) {
            game.batch.draw(playButtonSelected, playRec.x, playRec.y, 250, 100);
            game.batch.draw(scoreButton, scoreRec.x, scoreRec.y, 250, 100);
            game.batch.draw(playButton, exitRec.x, exitRec.y, 250, 100);
        }
        if (buttonSelected == Buttons.SCORE) {
            game.batch.draw(playButton, playRec.x, playRec.y, 250, 100);
            game.batch.draw(playButtonSelected, scoreRec.x, scoreRec.y, 250, 100);
            game.batch.draw(playButton, exitRec.x, exitRec.y, 250, 100);
        }
        if (buttonSelected == Buttons.EXIT) {
            game.batch.draw(playButton, playRec.x, playRec.y, 250, 100);
            game.batch.draw(scoreButton, scoreRec.x, scoreRec.y, 250, 100);
            game.batch.draw(playButtonSelected, exitRec.x, exitRec.y, 250, 100);
        }

        buttonEvents();
    }

    private void buttonEvents() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && buttonSelected == Buttons.PLAY) {
            dispose();
            game.setScreen(new JumpyBirbScreen(game, difficulty));
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
    }
}
