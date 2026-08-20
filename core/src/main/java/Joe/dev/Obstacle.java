package Joe.dev;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Obstacle {
    private float x;
    private float y;
    private float width;
    private float gapHeight;
    private float topRectHeight;
    private float bottomRectHeight;
    private float speed;
    private float intervalSpeed;
    private float Last_Score_Update = 0;

    private static final float OBSTACLE_WIDTH = 80f;
    private static final float GAP_HEIGHT = 140f;

    public Obstacle(float x, float screenHeight, float speed) {
        this.x = x;
        this.width = OBSTACLE_WIDTH;
        this.gapHeight = GAP_HEIGHT;
        this.speed = speed;
        this.intervalSpeed = 2f;

        // Generate random gap position
        float minGapY = 50f;
        float maxGapY = screenHeight - gapHeight - 50f;
        this.y = minGapY + (float) Math.random() * (maxGapY - minGapY);

        // Calculate rectangle heights
        this.topRectHeight = y;
        this.bottomRectHeight = screenHeight - (y + gapHeight);
    }

    public void update(float delta) {
        // Move obstacle from right to left
        x -= speed * delta;
    }

    public void render(ShapeRenderer sh) {
        sh.setColor(1, 0.5f, 0.5f, 1);

        // Draw top rectangle
        sh.rect(x, y + gapHeight, width, bottomRectHeight);

        // Draw bottom rectangle
        sh.rect(x, 0, width, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getGapHeight() {
        return gapHeight;
    }

    public float getTopRectHeight() {
        return topRectHeight;
    }

    public float getBottomRectHeight() {
        return bottomRectHeight;
    }

    public boolean isOffScreen(float screenWidth) {
        return x + width < 0;
    }

    public float Difficult_up(float Score) {
        if (Last_Score_Update != Score) {
            speed += 50f;
            width += 10f;
            intervalSpeed -= 0.1f;
            if (intervalSpeed < 0.5f) intervalSpeed = 0.5f;
            Last_Score_Update = Score;
            System.out.println("Speed Now : " + speed + " | IntervalSpeed: " + intervalSpeed);
        }
        return speed;
    }

    public float getIntervalSpeed() {
        return intervalSpeed;
    }
}
