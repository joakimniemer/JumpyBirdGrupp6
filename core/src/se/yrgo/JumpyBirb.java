package se.yrgo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class JumpyBirb extends ApplicationAdapter {

    private Texture birdImage;
    private Texture obstacleImages;
    private Sound crashSound;
    private Music backgroundMusic;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Rectangle bird;
    private Array<Rectangle> obstacles;
    private long lastObstacleTime;


    @Override
    public void create() {

        createImages();
        music();

        // Startar bakgrunddmusiken direkt när spelet startas.
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        batch = new SpriteBatch();

        // Skapar fågeln, ger den en storlek och position i rutnätet.
        bird = new Rectangle();
        bird.x = 800 / 2 - 64 / 2;
        bird.y = 480 / 2 - 64 / 2;
        bird.width = 64;
        bird.height = 64;

        obstacles = new Array<Rectangle>();
        spawnObstacle();

    }

    private void music() {
        // Ladda in musik/ljud
        crashSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
    }

    private void createImages() {
        // Create körs en gång varje gång spelet startas.
        // Laddar in bilder mm varje gång spelet startas.
        // Hämtar dessa filer från asset där vi anävnder files.internal
        birdImage = new Texture(Gdx.files.internal("bucket.png"));
        obstacleImages = new Texture(Gdx.files.internal("droplet.png"));

    }

    @Override
    public void render() {
        // Sätter bakgrundfärg.
        ScreenUtils.clear(1, 1, 0, 1);

        // Säger till kameran att säkerställa att den uppdateras. En gång per frame?
        camera.update();

        // camera skapar rutnätet (800x480), och SpriteBatch är det som "målar" ut objekten
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(birdImage, bird.x, bird.y);
        for(Rectangle obstacle: obstacles) {
            batch.draw(obstacleImages, obstacle.x, obstacle.y);
        }
        batch.end();

        birdMovment();
        obstaclePlacement();


    }

    private void obstaclePlacement() {

        // Räknar tid mellan hindren
        if(TimeUtils.nanoTime() - lastObstacleTime > 1000000000) spawnObstacle();


        // Hur funkar denna???
        for (Iterator<Rectangle> iter = obstacles.iterator(); iter.hasNext(); ) {
            Rectangle obstacle = iter.next();
            obstacle.x -= 200 * Gdx.graphics.getDeltaTime();
            if(obstacle.overlaps(bird)) {
                crashSound.play();
                gameEnds();
            }
            if(obstacle.x + 64 < 0) iter.remove();
        }
    }

    private void birdMovment() {
        // Hoppa med space
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) bird.y += 1500 * Gdx.graphics.getDeltaTime();


        //Får bird att falla neråt hela tiden
        bird.y -= 50 * Gdx.graphics.getDeltaTime();

        // Håller bird innanför spelrutan
        // Här bör man dö när man y = 0 eller 480
        if(bird.y < 0) bird.y = 0;
        if(bird.y > 480 - 64) bird.y = 480 - 64;

    }


    private void gameEnds() {
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();
        backgroundMusic.stop();
        gameOverMenu();

    }

    private void gameOverMenu() {
        //Kod för att öppna meny
    }

    public void exitGame(){
        Gdx.app.exit();
    }

    private void spawnObstacle(){
        Rectangle obstacle1 = new Rectangle();
        Rectangle obstacle2 = new Rectangle();
        obstacle1.x = 800;
        obstacle1.y = MathUtils.random(300, 400);
        obstacle1.width = 64;
        obstacle1.height = 64;
        obstacle2.x = 800;
        obstacle2.y = obstacle1.y - 200;
        obstacle2.width = 64;
        obstacle2.height = 64;
        obstacles.add(obstacle1);
        obstacles.add((obstacle2));
        lastObstacleTime = TimeUtils.nanoTime();
    }

//	@Override
//	public void dispose () {
//		batch.dispose();
//		img.dispose();
//	}
}
