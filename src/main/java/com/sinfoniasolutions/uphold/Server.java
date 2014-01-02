package com.sinfoniasolutions.uphold;

import com.almworks.sqlite4java.SQLiteException;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sinfoniasolutions.uphold.model.UpholdModel;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Created by Cameron Seebach on 12/26/13.
 */
public class Server extends Game{

    private UpholdView view;

    public static void main(String args[]){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.useGL20 = true;
        config.vSyncEnabled = true;

        new LwjglApplication(new Server(), config);
    }

    @Override
    public void create() {
        UpholdModel model = null;
        try {
            model = new UpholdModel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        view = new UpholdView(model);
        view.goToOverview();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);
        view.draw();
    }
}
