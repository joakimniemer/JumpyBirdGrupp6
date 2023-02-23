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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class JumpyBirbScreen implements Screen {

    private Texture rocket;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Body player;
    private Box2DDebugRenderer b2dr;

    // Skalar grafiken
    // TODO: Tror detta behövs för att kunna skala allt som Hampus snacka om. Behöver appliceras på all grafik.
    private final float SCALE = 2.0f;
    private final float worldGravity = -250f;
    private final int speedObstacle = -125;
    private long lastObstacleTime;

    private final ScreenHandler game;

    public JumpyBirbScreen(final ScreenHandler game) {
        this.game = game;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getWidth();

        // camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 700 / SCALE, 800 / SCALE);
        //Create boxed with Box2d
        world = new World(new Vector2(0, worldGravity), false);
        b2dr = new Box2DDebugRenderer();
        // Boxes
        player = createBox(32, 16, false, 100, 300);

        //Creating batch
        batch = new SpriteBatch();
        // Load images
        rocket = new Texture("rocket.png");

        lastObstacleTime = TimeUtils.nanoTime();

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        update(Gdx.graphics.getDeltaTime());

        batch.begin();
        batch.draw(rocket, player.getPosition().x - 16, player.getPosition().y - 8, 32, 16);
        batch.end();

        // Behövs bara för debugging.
        b2dr.render(world, camera.combined);

    }

    private void checkForCollison() {
        int numContacts = world.getContactCount();

        if (numContacts > 0){
            gameOverMenu();
        }
    }


    // Uppdatera box2D i render
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);

        jumpWithSpaceAndMouseClick(delta);
        continuouslySpawningObstacles();
        checkForCollison();
        batch.setProjectionMatrix(camera.combined);
    }


    // Skapa en box, static och dynamisk
    private Body createBox(int width, int heigth, boolean isStatic, int x, int y) {
        Body pBody;
        BodyDef def = new BodyDef();
        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set(x, y);
        def.fixedRotation = true;
        pBody = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / SCALE, heigth / SCALE);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }

    // Skapa hinder (kinimatiska boxar)
    private Body createKinimaticBody(int width, int heigth, int x, int y) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
        def.position.set(x, y);
        def.fixedRotation = true;
        pBody = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / SCALE, heigth / SCALE);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }


    //Spawna nya hinder
    private void spawnObstacle() {
        int randomPositionY = MathUtils.random(-150, 50);
        Body lowerObstacle = createKinimaticBody(32, 400, 367, randomPositionY);
        lowerObstacle.setLinearVelocity(speedObstacle, 0);
        Body upperObstacle = createKinimaticBody(32, 400, 367, randomPositionY + 500); //450 +-25??
        upperObstacle.setLinearVelocity(speedObstacle, 0);
        lastObstacleTime = TimeUtils.nanoTime();
    }

    // Räknar tid mellan hindren
    private void continuouslySpawningObstacles() {
        if (TimeUtils.nanoTime() / 1000000000 - lastObstacleTime / 1000000000 > 1) {
            spawnObstacle();
            //TODO: Måste lösa så att hindren tas bort när dom är utanför banan.
        }
    }

    // Hoppa med space
    private void jumpWithSpaceAndMouseClick(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.applyForceToCenter(0, 100000000, false);
        }
        //TODO: Lägga till så man hoppar med musknappen också.
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
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


