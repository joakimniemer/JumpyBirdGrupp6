package se.yrgo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class JumpyBirb extends ApplicationAdapter {

    private Texture birdImage;
    private Texture obstacles;
    private Sound jumpSound;
    private Music backgroundMusic;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Rectangle bird;


    @Override
    public void create() {
        // Create körs en gång varje gång spelet startas.
        // Laddar in bilder mm varje gång spelet startas.
        // Hämtar dessa filer från asset där vi anävnder files.internal
        birdImage = new Texture(Gdx.files.internal("bucket.png"));
        obstacles = new Texture(Gdx.files.internal("droplet.png"));

        // Ladda in musik/ljud
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

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
        batch.end();

		// Styr bird med tangenterna
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) bird.y += 200 * Gdx.graphics.getDeltaTime();

        //Får bird att falla neråt hela tiden
        bird.y -= 50 * Gdx.graphics.getDeltaTime();

        // Håller bird innanför spelrutan
        // Här bör man dö när man y = 0 eller 480
        if(bird.y < 0) bird.y = 0;
        if(bird.y > 480 - 64) bird.y = 480 - 64;

    }

//	@Override
//	public void dispose () {
//		batch.dispose();
//		img.dispose();
//	}
}
