package Joe.dev;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;


public class GamePlayScreen implements Screen {
    private ShapeRenderer sh;
    private AWSD_interacte awsd_detect;
    private Batch batch;
    private BitmapFont font;
    private final main game;
    private float[] Pos;
    private float speed;
    private float Heater;
    private float Value;
    private float height;
    private float width;
    private float sliceX;
    private float sliceY;
    private float playerRadius = 15f;
    private TempClass Time;

    // Obstacle system
    private ArrayList<Obstacle> obstacles;
    private float obstacleSpeed = 250f;
    private float obstacleSpawnTimer = 0f;
    private float obstacleSpawnInterval = 2f;
    private int score = 0;
    private boolean gameOver = false;

    // GUI Buttons
    private Button restartBtn;
    private Button backMenuBtn;

    public GamePlayScreen(main game) {
        this.game = game;
        System.out.println("GamePlayScreen constructor");
        sliceX = 0;
        sliceY = 0;
    }

    @Override
    public void show() {
        System.out.println("GamePlayScreen show - START");

        try {
            awsd_detect = new AWSD_interacte(170f,20);
            sh = new ShapeRenderer();
            batch = new SpriteBatch();
            Time = new TempClass();

            // Create BitmapFont for text rendering
            font = new BitmapFont();
            font.getData().setScale(1.5f);

            // Get screen dimensions
            height = Gdx.graphics.getHeight();
            width = Gdx.graphics.getWidth();
            System.out.println("Screen dimensions: " + width + " x " + height);

            Pos = new float[]{100f, height / 2f};
            speed = 200;
            obstacles = new ArrayList<>();
            gameOver = false;
            score = 0;

            // Initialize buttons
            float btnWidth = 150f;
            float btnHeight = 60f;
            float centerX = width / 2f;
            float centerY = height / 2f;

            System.out.println("Creating restart button at: " + (centerX - btnWidth - 20f) + ", " + (centerY - btnHeight / 2f));
            restartBtn = new Button(centerX - btnWidth - 20f, centerY - btnHeight / 2f, btnWidth, btnHeight, "RESTART",
                    () -> {
                        System.out.println("RESTART clicked");
                        game.setScreen(new GamePlayScreen(game));
                    }, font);

            System.out.println("Creating back menu button at: " + (centerX + 20f) + ", " + (centerY - btnHeight / 2f));
            backMenuBtn = new Button(centerX + 20f, centerY - btnHeight / 2f, btnWidth, btnHeight, "BACK MENU",
                    () -> {
                        System.out.println("BACK MENU clicked");
                        game.setScreen(new MainMenuScreen(game));
                    }, font);

            float[] restartColor = new float[]{0.2f, 1f, 0.2f, 1f};
            float[] restartHoverColor = new float[]{0.4f, 1f, 0.4f, 1f};
            restartBtn.setColors(restartColor, restartHoverColor);

            float[] backColor = new float[]{1f, 0.2f, 0.2f, 1f};
            float[] backHoverColor = new float[]{1f, 0.4f, 0.4f, 1f};
            backMenuBtn.setColors(backColor, backHoverColor);

            float[] textColor = new float[]{1f, 1f, 1f, 1f};
            restartBtn.setTextColor(textColor);
            backMenuBtn.setTextColor(textColor);

            System.out.println("GamePlayScreen show - SUCCESS");
        } catch (Exception e) {
            System.err.println("ERROR in GamePlayScreen.show(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    public float cekHeater() {
        float minX = 30f;
        float maxX = Gdx.graphics.getWidth() - 30f;
        Value = (Pos[0] - minX) / (maxX - minX);

        return Value;
    }

    private boolean checkCollision(Obstacle obs) {
        float circleX = Pos[0];
        float circleY = Pos[1];
        float obsX = obs.getX();
        float obsY = obs.getY();
        float obsWidth = obs.getWidth();
        float gapY = obs.getY();
        float gapHeight = obs.getGapHeight();

        // Check if circle is in obstacle's X range
        if (circleX + playerRadius > obsX && circleX - playerRadius < obsX + obsWidth) {
            // Check if circle hits top rectangle
            if (circleY + playerRadius > gapY + gapHeight) {
                return true;
            }

            if (circleY - playerRadius < gapY) {
                return true;
            }
        }
        return false;
    }

    private void updateObstacles(float delta, float Score) {
        // Spawn new obstacles
        obstacleSpawnTimer += delta;
        if (obstacleSpawnTimer >= obstacleSpawnInterval) {
            obstacles.add(new Obstacle(width, height, obstacleSpeed));
            obstacleSpawnTimer = 0;
        }

        // Update existing obstacles
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Obstacle obs = obstacles.get(i);
            obs.update(delta);

            if (obs.isOffScreen(width)) {
                obstacles.remove(i);
                score++;
            } else if (checkCollision(obs)) {
                gameOver = true;
                System.out.println("Game Over! Score: " + score);
            }
        }

        // Check difficulty increase only once per score change
        if (Score > 0 && Score % 6 == 0 && !obstacles.isEmpty()) {
            obstacleSpeed = obstacles.get(0).Difficult_up(score);
            obstacleSpawnInterval = obstacles.get(0).getIntervalSpeed();
        }
    }

    @Override
    public void render(float delta) {
        try {
            // Safety check
            if (sh == null || restartBtn == null || backMenuBtn == null || batch == null) {
                System.err.println("ERROR: Components not initialized!");
                return;
            }


            float mouseX = Gdx.input.getX();
            float mouseY = height - Gdx.input.getY();
            boolean mouseClicked = Gdx.input.justTouched();

            if (gameOver) {

                ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
                sh.begin(ShapeRenderer.ShapeType.Filled);
                sh.setColor(1, 0, 0, 1);
                sh.circle(Pos[0], Pos[1], playerRadius);



                for (Obstacle obs : obstacles) {
                    obs.render(sh);
                }
                sh.end();

                // Update and render buttons
                restartBtn.update(mouseX, mouseY, mouseClicked);
                backMenuBtn.update(mouseX, mouseY, mouseClicked);

                restartBtn.render(sh, batch);
                backMenuBtn.render(sh, batch);

                // Display score
                if (font != null) {
                    batch.begin();
                    font.setColor(1, 1, 1, 1);
                    font.draw(batch, "Score: " + score, width / 2f - 50, height - 100);
                    batch.end();
                }
                return;
            }

            Heater = cekHeater();

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                speed = 340;
            } else {
                speed = 200;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                Pos[0] -= delta * speed;
                sliceX = -80f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                Pos[0] += delta * speed;
                sliceX = 80f;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                Pos[1] += delta * speed;
                sliceY = 80f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                Pos[1] -= delta * speed;
                sliceY = -80f;
            }



            if (sliceX != 0) {
                if (sliceX > 0f) {
                    Pos[0] += sliceX * delta;
                    sliceX -= delta * 30;
                    if (sliceX < 0f) {
                        sliceX = 0f;
                    }
                }
                if (sliceX < 0f) {
                    Pos[0] -= -sliceX * delta;
                    sliceX += delta * 30;
                    if (sliceX > 0f) {
                        sliceX = 0f;
                    }
                }
            }

            if (sliceY != 0) {
                if (sliceY > 0f) {
                    Pos[1] += sliceY * delta;
                    sliceY -= delta * 30;
                    if (sliceY < 0f) {
                        sliceY = 0f;
                    }
                }
                if (sliceY < 0f) {
                    Pos[1] -= -sliceY * delta;
                    sliceY += delta * 30;
                    if (sliceY > 0f) {
                        sliceY = 0f;
                    }
                }
            }

            if (Pos[0] > width - 30f) {
                Pos[0] = width - 30f;
            }

            if (Pos[0] < 0 + 30f) {
                Pos[0] = 0 + 30f;
            }

            if (Pos[1] > height - 30f) {
                Pos[1] = height - 30f;
            }

            if (Pos[1] < 0 + 30f) {
                Pos[1] = 0 + 30f;
            }

            // Update obstacles
            updateObstacles(delta,score);

            ScreenUtils.clear(Heater, 0.2f, 0.3f, 1);
            sh.begin(ShapeRenderer.ShapeType.Filled);

            // Draw player
            sh.setColor(0, 1, 0, 1);
            sh.circle(Pos[0], Pos[1], playerRadius);

            // Draw obstacles
            for (Obstacle obs : obstacles) {
                obs.render(sh);
            }
            sh.end();

            // Draw UI elements on top (after sh.end())


            batch.begin();
            if (Time.CheckTemp() > 0) {
            font.setColor(1f,1f,1f,Time.getTemp(delta));
            if (Time.getTemp(0) < 0) {
                Time.setTemp(0);
            }
            }
            font.draw(batch,"HOLD SHIFT FOR SPEED UP",30f,Gdx.graphics.getHeight() - 50f);
            awsd_detect.draw(batch);
            batch.end();



        } catch (Exception e) {
            System.err.println("ERROR in render: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void hide() {
        System.out.println("GamePlayScreen hide");
    }

    @Override
    public void dispose() {
        System.out.println("GamePlayScreen dispose");
        if (sh != null) sh.dispose();
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (awsd_detect != null) awsd_detect.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

}
