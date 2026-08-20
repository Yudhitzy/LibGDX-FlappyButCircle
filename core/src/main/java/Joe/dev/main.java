package Joe.dev;
import com.badlogic.gdx.Game;

public class main extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }


    public void render() {
            super.render();
        }


    @Override
    public void dispose() {

    }
}
