package se.yrgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Iterator;


public class JumpyBirbScreen implements Screen {

    private Texture spaceship;

    private Texture[] backGround;
    private Texture astroid;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;
    private Label scoreText;
    private Skin mySkin;
    private Sound jumpSound;
    private Sound crashSound;
    private Music gameMusic;

    // box2d variabler
    // TODO: Skala ner allt till 6.0f för bättre hopp. Lås så man inte kan resiza med hjälp av viewport?
    private final float SCALE = 2.0f;
    private final float worldGravity = -300f;
    private final int speedObstacle = -125;
    private final float spawnTimer = 1f;
    private long lastObstacleTime;
    private World world;
    private Body player;
    private Array<Body> obstacles;

    //poängs variabler
    private int currentRoundScore;
    private long scoreTimer;

    //animations variabler
    private static final int FRAME_COLS = 2, FRAME_ROWS = 2;
    private Animation<TextureRegion> fireAnimation;
    private float elapsedTime;
    private Texture animationSheet;
    private long jumpStartTime;
    private int animationDuration = 3; //Tiondels-sekunder


    //bakgrundsbild variabler
    private int backGroundOffset;
    private float backGroundMaxSrollingSpeed;
    private final int WORLD_HEIGHT = 800;
    private final int WORLD_WIDTH = 700;
    private Texture backGround1;

    //Svårighetsgrad
    private int difficulty;

    private final ScreenHandler game;

    public JumpyBirbScreen(final ScreenHandler game, int difficulty) {
        this.game = game;

        // camera TODO: SCALE på camera ger ingen funktion??
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700 / SCALE, 800 / SCALE);

        //Create boxed with Box2d
        world = new World(new Vector2(0, worldGravity), false);

        // Box for player
        player = LoadAssets.createBox(world, SCALE, 20, 10, false, 100, 300);

        loadImages();
        flamesAnimation();
        createText();

        backGround1 = new Texture("MenuAssets/backgroundMenu.png");
        backGroundOffset = 0;


        batch = new SpriteBatch();
        lastObstacleTime = TimeUtils.nanoTime();
        obstacles = new Array<Body>();
        currentRoundScore = 0;
        scoreTimer = System.nanoTime();
        this.difficulty = difficulty;

        //Load sounds
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/jumpSound.mp3"));
        crashSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/crashSound.wav"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/GameMusic.ogg"));
        gameMusic.play();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.setContinuousRendering(true);

        elapsedTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = fireAnimation.getKeyFrame(elapsedTime, true);

        update(Gdx.graphics.getDeltaTime());

        batch.begin();
        backGroundOffset ++;
        if (backGroundOffset % WORLD_WIDTH == 0) {
            backGroundOffset = 0;
        }
       /* renderBackground(elapsedTime);*/
        batch.draw(backGround1, -backGroundOffset, 0);
        batch.draw(backGround1,-backGroundOffset+WORLD_WIDTH,0);
        batch.draw(spaceship, player.getPosition().x - 16, player.getPosition().y - 8, 32, 16);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            setJumpTimer();
        }
        if (jumpTrue()) {
            batch.draw(currentFrame, player.getPosition().x - 16, player.getPosition().y - 60, 32, 55);
        }

        for (Body obstacle : obstacles) {
            batch.draw(astroid, obstacle.getPosition().x - 20, obstacle.getPosition().y - 20, 40, 42);
        }
        batch.end();

        scoreCounter();
        updateScoreText(delta);
    }

    private void updateScoreText(float delta) {
        scoreText.setText(String.format("Score: %d", currentRoundScore));
        stage.act(delta);
        stage.draw();
    }

    private boolean jumpTrue() {
        if ((((System.nanoTime()) - jumpStartTime) / 100000000) < animationDuration) {
            return true;
        }
        return false;
    }

    private void setJumpTimer() {
        jumpStartTime = System.nanoTime();
    }

    private void scoreCounter() {
        currentRoundScore = (int) ((System.nanoTime() - scoreTimer) / 1000000000);
    }

    private void checkForCollison() {
        int numberContacts = world.getContactCount();
        if (numberContacts > 0) {
            crashSound.play();
            conflictWithObstacle();
        }
        if (player.getPosition().y < 0 || player.getPosition().y > 400) {
            crashSound.play();
            conflictWithEdge();
        }
    }

    private void conflictWithEdge() {
        LoadAssets.updateHighScore(currentRoundScore);
        gameOverMenu();
    }

    private void conflictWithObstacle() {
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();
        LoadAssets.updateHighScore(currentRoundScore);
        gameOverMenu();
    }


    // Uppdatera box2D i render
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        jumpWithSpaceAndMouseClick(delta);
        continuouslySpawningObstacles();
        checkForCollison();
        disposeObstacles();
        batch.setProjectionMatrix(camera.combined);
    }

    // Animation för elden
    private void flamesAnimation() {
        //Laddar in bilden some en texture
        animationSheet = new Texture("FIRESheet.png");

        //Delar upp bilden i fyra lika stora delar. FRAME_COLS och FRAME_ROWS är 2x2 eftersom det är fyra bilder
        TextureRegion[][] arrayOfSplitPictures = TextureRegion.split(animationSheet,
                animationSheet.getWidth() / FRAME_ROWS,
                animationSheet.getHeight() / FRAME_COLS);

        //Skapa en 1D array av de fyra bilderna och lägger dem i ordning, börjar från top-left.
        TextureRegion[] animationFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                animationFrames[index++] = arrayOfSplitPictures[i][j];
            }
        }

        //Skapar animationen och bestämmer "frame interval" och array av frames att loopa
        fireAnimation = new Animation<TextureRegion>(0.25f, animationFrames);

        //Sätter elapsed animation tid till 0. TODO: Behövs denna verkligen för animation?
        elapsedTime = 0f;
    }


    //Spawna nya hinder
    private void spawnObstacle() {
        int randomPositionY1 = MathUtils.random(21, 119);
        int randomPositionY2 = MathUtils.random(161, 259);
        int randomPositionY3 = MathUtils.random(280, 379);
        int randomPositionX1 = MathUtils.random(367, 460);
        int randomPositionX2 = MathUtils.random(367, 460);
        int randomPositionX3 = MathUtils.random(367, 460);
        Body lowerObstacle = LoadAssets.createKinimaticBody(world, SCALE, 32, randomPositionX1, randomPositionY1);
        lowerObstacle.setLinearVelocity(speedObstacle, 0);
        Body middleObstacle = LoadAssets.createKinimaticBody(world, SCALE, 32, randomPositionX2, randomPositionY2);
        middleObstacle.setLinearVelocity(speedObstacle, 0);
        Body upperObstacle = LoadAssets.createKinimaticBody(world, SCALE, 32, randomPositionX3, randomPositionY3);
        upperObstacle.setLinearVelocity(speedObstacle, 0);
        obstacles.add(lowerObstacle);
        obstacles.add(middleObstacle);
        obstacles.add(upperObstacle);
        lastObstacleTime = TimeUtils.nanoTime();

    }

    // Räknar tid mellan hindren och anropar spawnObstacle();
    private void continuouslySpawningObstacles() {
        if (TimeUtils.nanoTime() / 1000000000 - lastObstacleTime / 1000000000 > spawnTimer) {
            spawnObstacle();
        }
    }

    // Hoppa med space
    private void jumpWithSpaceAndMouseClick(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            player.applyForceToCenter(0, 100000000, false);
            jumpSound.play(0.3f);
        }
    }

    // Tar bort hindren när dom kommer till x = 0;
    private void disposeObstacles() {
        for (Iterator<Body> iter = obstacles.iterator(); iter.hasNext(); ) {
            Body obs = iter.next();
            if (obs.getPosition().x < 0) {
                iter.remove();
            }
        }
    }

    private void createText() {
        mySkin = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        scoreText = new Label(String.format("Score: %d", currentRoundScore), mySkin, "over");
        scoreText.setSize(100, 100);
        scoreText.setPosition(300, 700);
        stage = new Stage(new ScreenViewport());
        stage.addActor(scoreText);

    }

    private void loadImages() {
        spaceship = new Texture("spaceship.png");
        astroid = new Texture("asteroid.png");
    }

    @Override
    public void dispose() {
        animationSheet.dispose();
        jumpSound.dispose();
        gameMusic.dispose();
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 700 / SCALE, 800 / SCALE);
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

    private void gameOverMenu() {
        dispose();
        game.setScreen(new GameOverScreen(game, currentRoundScore, difficulty));
    }

    @Override
    public void show() {
    }
}



