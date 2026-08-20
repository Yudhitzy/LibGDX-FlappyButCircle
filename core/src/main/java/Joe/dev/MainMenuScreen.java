package Joe.dev;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    private final main game;
    private Button startBtn;
    private Texture backgroundTexture;
    private ShapeRenderer sh;
    private Batch batch;
    private BitmapFont font;
    private float width;
    private float height;

    public MainMenuScreen(main game) {
        this.game = game;
    }

    @Override
    public void show() {
        System.out.println("MainMenuScreen show");
        sh = new ShapeRenderer();
        batch = new SpriteBatch();

        // Create BitmapFont for text rendering
        font = new BitmapFont();
        font.getData().setScale(2f);

        // NOW get screen dimensions (safe to access here)
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        // Initialize START button at center
        float btnWidth = 200f;
        float btnHeight = 80f;
        float centerX = width / 2f;
        float centerY = height / 2f;

        startBtn = new Button(centerX - btnWidth / 2f, centerY - btnHeight / 2f, btnWidth, btnHeight, "START GAME",
                () -> {
                    System.out.println("START button clicked!");
                    game.setScreen(new GamePlayScreen(game));
                }, font);

        float[] startColor = new float[]{0.1f, 0.8f, 0.3f, 1f};
        float[] startHoverColor = new float[]{0.3f, 1f, 0.5f, 1f};
        startBtn.setColors(startColor, startHoverColor);

        float[] textColor = new float[]{1f, 1f, 1f, 1f};
        startBtn.setTextColor(textColor);

        // Load background texture (optional - check if file exists)
        try {
            backgroundTexture = new Texture("BackGMainMenu.png");
            System.out.println("Background loaded successfully");
        } catch (Exception e) {
            System.out.println("Background texture not found: BackGMainMenu.png");
            backgroundTexture = null;
        }
    }

    @Override
    public void render(float delta) {
        float mouseX = Gdx.input.getX();
        float mouseY = height - Gdx.input.getY();
        boolean mouseClicked = Gdx.input.justTouched();

        // Also support SPACE key for backward compatibility
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println("SPACE pressed - Start game");
            game.setScreen(new GamePlayScreen(game));
        }

        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);

        // Render background if available
        if (backgroundTexture != null) {
            //backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            batch.begin();
            batch.draw(
                backgroundTexture,
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            );
            batch.end();
        }

        // Update and render button
        startBtn.update(mouseX, mouseY, mouseClicked);
        startBtn.render(sh, batch);
    }

    @Override
    public void hide() {
        System.out.println("MainMenuScreen hide");
    }

    @Override
    public void dispose() {
        System.out.println("MainMenuScreen dispose");
        if (sh != null) sh.dispose();
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
}
