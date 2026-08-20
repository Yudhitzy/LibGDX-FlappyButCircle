package Joe.dev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class AWSD_interacte {
    private Texture A_button;
    private Texture W_button;
    private Texture S_button;
    private Texture D_button;
    private Texture A_button_pressed;
    private Texture W_button_pressed;
    private Texture S_button_pressed;
    private Texture D_button_pressed;

    private float posX;
    private float posY;
    private float keySize = 70f;
    private float spacing = 4f;

    // Constructor captures PositionObj (screen position) where the AWSD cluster will be drawn
    public AWSD_interacte(float positionX, float positionY) {
        this.posX = positionX;
        this.posY = positionY;

        A_button = new Texture("Button/A_keys.png");
        W_button = new Texture("Button/W_keys.png");
        S_button = new Texture("Button/S_keys.png");
        D_button = new Texture("Button/D_keys.png");

        // pressed versions (use these when corresponding key is down)
        A_button_pressed = new Texture("Button/A_keys_pressed.png");
        W_button_pressed = new Texture("Button/W_keys_pressed.png");
        S_button_pressed = new Texture("Button/S_keys_pressed.png");
        D_button_pressed = new Texture("Button/D_keys_pressed.png");
    }

    // Draw the AWSD cluster. Caller should provide a Batch (e.g., SpriteBatch) already begun.
    public void draw(Batch batch) {
        float w = keySize;
        float h = keySize;
        float centerX = posX;
        float centerY = posY;

        // W on top, A S D on the row below
        Texture wTex = Gdx.input.isKeyPressed(Input.Keys.W) ? W_button_pressed : W_button;
        batch.draw(wTex, centerX - w/2f, centerY + h + spacing, w, h);

        Texture aTex = Gdx.input.isKeyPressed(Input.Keys.A) ? A_button_pressed : A_button;
        batch.draw(aTex, centerX - w/2f - w - spacing, centerY, w, h);

        Texture sTex = Gdx.input.isKeyPressed(Input.Keys.S) ? S_button_pressed : S_button;
        batch.draw(sTex, centerX - w/2f, centerY, w, h);

        Texture dTex = Gdx.input.isKeyPressed(Input.Keys.D) ? D_button_pressed : D_button;
        batch.draw(dTex, centerX + w/2f + spacing, centerY, w, h);
    }

    public void setPosition(float x, float y) {
        this.posX = x;
        this.posY = y;
    }

    public void setKeySize(float size) {
        this.keySize = size;
    }

    public void dispose() {
        A_button.dispose();
        W_button.dispose();
        S_button.dispose();
        D_button.dispose();
        A_button_pressed.dispose();
        W_button_pressed.dispose();
        S_button_pressed.dispose();
        D_button_pressed.dispose();
    }
}
