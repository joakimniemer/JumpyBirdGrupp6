package se.yrgo;

import com.badlogic.gdx.physics.box2d.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

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


    private static List<String> readFromHighscoreFile() throws IOException {
        if (!Files.exists(Path.of("highScore.txt"))) {
            Files.createFile(Path.of("highScore.txt"));
        }
        BufferedReader reader = new BufferedReader(new FileReader("highScore.txt"));
        List<String> highScoreList = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            highScoreList.add(line);
        }
        return highScoreList;
    }


    private static List<Highscore> listOfHighscoreObjects() throws IOException {
        List<Highscore> listOfhighScores = new ArrayList<>();

        for (String highscore : readFromHighscoreFile()) {
            String[] nameAndScore = highscore.split(":");
            String name = nameAndScore[0];
            int score = Integer.parseInt(nameAndScore[1]);
            listOfhighScores.add(new Highscore(name, score));
        }
        return listOfhighScores;
    }

    public static String getStringOfHighscores() throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Highscore hs : listOfHighscoreObjects()) {
            builder.append(hs.getName() + ":" + hs.getScore() + "\n");
        }
        return builder.toString();
    }

    private static int getLowestHighScore() throws IOException {
        if (listOfHighscoreObjects().size() < 10) {
            return 0;
        }
        var lowestHighScore = Collections.min(listOfHighscoreObjects());
        return lowestHighScore.getScore();
    }

    public static boolean isNewHighscore(int score) throws IOException {
        if (score > getLowestHighScore()) {
            return true;
        }
        return false;
    }

    public static void updateHighScore(String name, int score) throws IOException {
        if (isNewHighscore(score)) {
            String finalName = name;
            char[] chars = name.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (!Character.isAlphabetic(chars[i]) || Character.isWhitespace(chars[i])) {
                    finalName = "player";
                }
            }
            Highscore newHighscore = new Highscore(finalName.trim(), score);
            writeToHighScore(newHighscore);
        }
    }


    private static void writeToHighScore(Highscore highscore) throws IOException {
        List<Highscore> highScorelist = updateHighScoreList(highscore);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("highScore.txt"));
            StringBuilder builder = new StringBuilder();
            for (Highscore hs : highScorelist) {
                builder.append(hs.getName() + ":" + hs.getScore() + "\n");
            }
            String newHighScoreString = builder.toString();

            writer.write(newHighScoreString);
            writer.flush();

        } catch (IOException e) {
            System.err.println("Something went wrong with writing to highscore: " + e);
        }
    }

    private static List<Highscore> updateHighScoreList(Highscore highscore) throws IOException {
        List<Highscore> highScorelist = listOfHighscoreObjects();
        highScorelist.add(highscore);
        highScorelist.sort(Comparator.reverseOrder());
        if (highScorelist.size() > 10) {
            highScorelist.remove(highScorelist.size() - 1);
        }
        return highScorelist;
    }
}

