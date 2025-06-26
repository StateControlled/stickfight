package com.berthouex.stickfight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.berthouex.stickfight.objects.Fighter;
import com.berthouex.stickfight.resources.Assets;
import com.berthouex.stickfight.resources.AudioManager;
import com.berthouex.stickfight.screen.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public ShapeRenderer shapeRenderer;
    public SpriteBatch batch;
    public Assets assets;
    public AudioManager audioManager;

    public Screen gameScreen;

    public Fighter player;
    public Fighter opponent;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        assets = new Assets();
        assets.load();
        assets.manager.finishLoading();
        audioManager = new AudioManager(assets.manager);
        audioManager.playMusic();

        player = new Fighter(this, "Slim Stallone", new Color(1.0f, 0.2f, 0.2f, 1.0f));
        opponent = new Fighter(this, "Thin Diesel", new Color(0.25f, 0.7f, 1.0f, 1.0f));

        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        assets.dispose();
    }

}
