package Joe.dev;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Button {
    private float x, y, width, height;
    private String text;
    private float[] normalColor;
    private float[] hoverColor;
    private float[] textColor;
    private boolean isHovered;
    private Runnable onClickAction;
    private BitmapFont font;
    private GlyphLayout layout;

    public Button(float x, float y, float width, float height, String text, Runnable onClick, BitmapFont font) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onClickAction = onClick;
        this.font = font;
        this.layout = new GlyphLayout();
        this.normalColor = new float[]{0.2f, 0.7f, 1f, 1f};
        this.hoverColor = new float[]{0.4f, 0.9f, 1f, 1f};
        this.textColor = new float[]{1f, 1f, 1f, 1f};
        this.isHovered = false;
    }

    public void setColors(float[] normal, float[] hover) {
        this.normalColor = normal;
        this.hoverColor = hover;
    }

    public void setTextColor(float[] color) {
        this.textColor = color;
    }

    public boolean isMouseOver(float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width &&
               mouseY >= y && mouseY <= y + height;
    }

    public void update(float mouseX, float mouseY, boolean mouseClicked) {
        isHovered = isMouseOver(mouseX, mouseY);
        if (isHovered && mouseClicked) {
            onClickAction.run();
        }
    }

    public void render(ShapeRenderer sh, Batch batch) {
        // Render background rectangle
        sh.begin(ShapeRenderer.ShapeType.Filled);
        if (isHovered) {
            sh.setColor(hoverColor[0], hoverColor[1], hoverColor[2], hoverColor[3]);
        } else {
            sh.setColor(normalColor[0], normalColor[1], normalColor[2], normalColor[3]);
        }
        sh.rect(x, y, width, height);
        sh.end();

        // Render text
        if (font != null && batch != null) {
            batch.begin();
            layout.setText(font, text);
            float textX = x + (width - layout.width) / 2f;
            float textY = y + height - (height - layout.height) / 2f;
            font.setColor(textColor[0], textColor[1], textColor[2], textColor[3]);
            font.draw(batch, text, textX, textY);
            batch.end();
        }
    }

    public String getText() {
        return text;
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

    public float getHeight() {
        return height;
    }
}
