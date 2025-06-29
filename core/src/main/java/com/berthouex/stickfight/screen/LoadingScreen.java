package com.berthouex.stickfight.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.berthouex.stickfight.Main;
import com.berthouex.stickfight.resources.GlobalVariables;

public class LoadingScreen implements Screen {
    private final Main game;
    private final Viewport viewport;

    private static final float PROGRESS_BAR_MAX_WIDTH = 58.0f;
    private static final float PROGRESS_BAR_HEIGHT = 5.0f;
    private static final float PROGRESS_BAR_BACKGROUND_WIDTH = PROGRESS_BAR_MAX_WIDTH + 2.0f;
    private static final float PROGRESS_BAR_BACKGROUND_HEIGHT = PROGRESS_BAR_HEIGHT + 2.0f;

    private float delayTimer;
    private boolean delayStarted;
    private static final float DELAY_TIME = 1.0f;

    public LoadingScreen(Main game) {
        this.game = game;
        this.viewport = new ExtendViewport(GlobalVariables.WORLD_WIDTH, GlobalVariables.MIN_WORLD_HEIGHT, GlobalVariables.WORLD_WIDTH, 0);
        delayTimer = DELAY_TIME;
        delayStarted = false;

        game.assets.load();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(GlobalVariables.BLUE_BACKGROUND);

        if (delayStarted) {
            // if the delay has started check if delay timer has finished
            if (delayTimer <= 0.0f) {
                game.assetsLoaded();
            } else {
                delayTimer -= delta;
            }
        } else if (game.assets.manager.update()) {
            // if assets have finished loading, start delay
            delayStarted = true;
        }

        // shape renderer use viewport camera; draw progress bar
        game.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        game.shapeRenderer.setColor(0, 0, 0, 1);
        game.shapeRenderer.rect(
            viewport.getWorldWidth() / 2.0f - PROGRESS_BAR_BACKGROUND_WIDTH / 2.0f,
            viewport.getWorldHeight() / 2.0f - PROGRESS_BAR_BACKGROUND_HEIGHT / 2.0f,
            PROGRESS_BAR_BACKGROUND_WIDTH,
            PROGRESS_BAR_BACKGROUND_HEIGHT
        );

        game.shapeRenderer.setColor(GlobalVariables.GOLD);
        game.shapeRenderer.rect(
            viewport.getWorldWidth() / 2.0f - PROGRESS_BAR_MAX_WIDTH / 2.0f,
            viewport.getWorldHeight() / 2.0f - PROGRESS_BAR_HEIGHT / 2.0f,
            game.assets.manager.getProgress() * PROGRESS_BAR_MAX_WIDTH,
            PROGRESS_BAR_HEIGHT
        );

        game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
