package com.berthouex.stickfight.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.berthouex.stickfight.Main;
import com.berthouex.stickfight.resources.Assets;
import com.berthouex.stickfight.resources.GlobalVariables;

public class BloodPool implements Updatable {
    private static final float FADE_TIME = 60.0f;
    // state
    private float stateTime;
    private boolean active;
    private float alpha;
    private final Vector2 position = new Vector2();

    // texture
    private TextureRegion texture;
    private static final int TEXTURE_AMOUNT = 3;

    public BloodPool(Main game) {
        stateTime = 0.0f;
        active = false;
        alpha = 1.0f;
        texture = getRandomBloodPoolTexture(game.assets.manager);
    }

    private TextureRegion getRandomBloodPoolTexture(AssetManager manager) {
        TextureAtlas bloodAtlas = manager.get(Assets.BLOOD_ATLAS);
        return bloodAtlas.findRegion("BloodPool" + MathUtils.random(TEXTURE_AMOUNT - 1));
    }

    public void activate(float x, float y) {
        active = true;
        stateTime = 0.0f;
        alpha = 1.0f;
        position.set(x, y);
    }

    public void deactivate() {
        alpha = 0.0f;
        active = false;
    }

    @Override
    public void update(float deltaTime) {
        if (!active) {
            return;
        }

        stateTime += deltaTime;

        // decrement alpha and deactivate blood pool if it reaches zero
        alpha = 1.0f - stateTime / FADE_TIME;
        if (alpha <= 0.0f) {
            deactivate();
        }
    }

    public void render(Batch spriteBatch) {
        if (!active) {
            return;
        }

        spriteBatch.setColor(1, 1, 1, alpha);

        // draw current animation frame
        spriteBatch.draw(
            texture,
            position.x,
            position.y,
            texture.getRegionWidth() * GlobalVariables.WORLD_SCALE,
            texture.getRegionHeight() * GlobalVariables.WORLD_SCALE
        );

        spriteBatch.setColor(1, 1, 1, 1); // reset color to fully opaque
    }
}
