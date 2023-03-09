package se.yrgo;

import com.badlogic.gdx.physics.box2d.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoadAssets {

    public static Body createBox(World world, float SCALE, int width, int heigth, boolean isStatic, int x, int y) {
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
    public static Body createKinimaticBody(World world, float SCALE, int radius, int x, int y) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
        def.position.set(x, y);
        def.fixedRotation = true;
        pBody = world.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / SCALE);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }

    public static int getHighScore() {
        int highScore = 0;

        Path path = Path.of("highScore.txt");

        try {
            String highScoreString = Files.readString(path);
            highScore = Integer.parseInt(String.valueOf(highScoreString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return highScore;
    }
}

