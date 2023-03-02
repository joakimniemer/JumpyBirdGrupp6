package se.yrgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class JumpyBirbScreen implements Screen {

    private Texture rocket;
    private Texture space;
    private Texture astroid;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Body player;
    private Array<Body> obstacles;
    private Box2DDebugRenderer boxDebugger;

    // Skalar grafiken
    // TODO: Tror detta behövs för att kunna skala allt som Hampus snacka om. Behöver appliceras på all grafik.
    private final float SCALE = 2.0f;
    private final float worldGravity = -300f;
    private final int speedObstacle = -125;
    private long lastObstacleTime;

    private final ScreenHandler game;

    public JumpyBirbScreen(final ScreenHandler game) {
        this.game = game;

        // camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700 / SCALE, 800 / SCALE);
        //Create boxed with Box2d
        world = new World(new Vector2(0, worldGravity), false);
        boxDebugger = new Box2DDebugRenderer();
        // Box for player
        player = LoadAssets.createBox(world, SCALE,32, 16, false, 100, 300);

       loadImages();

        //Creating SpriteBatch
        batch = new SpriteBatch();

        //Sätter tiden för senaste hinder första gången
        lastObstacleTime = TimeUtils.nanoTime();

        //Skapar Array för obstacles
        obstacles = new Array<Body>();

    }

    private void loadImages() {
        rocket = new Texture("rocket.png");
        space = new Texture("space.png");
        astroid = new Texture("astroid.png");
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        update(Gdx.graphics.getDeltaTime());

        //Batch, ritar ut spelare, hinder och bakgrund
        batch.begin();
        batch.draw(space, 0, 0, 700 / SCALE, 800 / SCALE);
        batch.draw(rocket, player.getPosition().x - 16, player.getPosition().y - 8, 32, 16);
        for (Body obstacle : obstacles) {
            batch.draw(astroid, obstacle.getPosition().x - 20, obstacle.getPosition().y - 20, 40, 42);
        }
        batch.end();

        // Behövs bara för debugging.
        boxDebugger.render(world, camera.combined);
    }


    private void checkForCollison() {
        int numberContacts = world.getContactCount();
        if (numberContacts > 0) {
            gameOverMenu();
        }
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

    //Spawna nya hinder
    private void spawnObstacle() {
        int randomPositionY1 = MathUtils.random(50, 150);
        int randomPositionY2 = MathUtils.random(150, 300);
        int randomPositionY3 = MathUtils.random(300, 450);
        Body lowerObstacle = LoadAssets.createKinimaticBody(world, SCALE, 32, 367, randomPositionY1);
        lowerObstacle.setLinearVelocity(speedObstacle, 0);
        Body middleObstacle = LoadAssets.createKinimaticBody(world, SCALE, 32, 367, randomPositionY2);
        middleObstacle.setLinearVelocity(speedObstacle, 0);
        Body upperObstacle = LoadAssets.createKinimaticBody(world, SCALE,32, 367, randomPositionY3);
        upperObstacle.setLinearVelocity(speedObstacle, 0);
        obstacles.add(lowerObstacle);
        obstacles.add(middleObstacle);
        obstacles.add(upperObstacle);
        lastObstacleTime = TimeUtils.nanoTime();
    }

    // Räknar tid mellan hindren
    private void continuouslySpawningObstacles() {
        if (TimeUtils.nanoTime() / 1000000000 - lastObstacleTime / 1000000000 > 0.2) {
            spawnObstacle();
            //TODO: Måste lösa så att hindren tas bort när dom är utanför banan.
        }
    }

    // Hoppa med space
    private void jumpWithSpaceAndMouseClick(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            player.applyForceToCenter(0, 100000000, false);
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

    @Override
    public void dispose() {
        world.dispose();
        boxDebugger.dispose();
        batch.dispose();
    }

    // Gör ingenting ännu?
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / SCALE, height / SCALE);
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
        game.setScreen(new GameOverScreen(game));
    }

    @Override
    public void show() {

    }

    public void exitGame() {
        Gdx.app.exit();
    }


}


