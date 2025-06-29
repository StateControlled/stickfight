package com.berthouex.stickfight.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.berthouex.stickfight.Main;
import com.berthouex.stickfight.resources.Assets;
import com.berthouex.stickfight.resources.GlobalVariables;

public class BloodSplatter extends GameObject {
    // state
    private boolean active;

    // animation
    private Animation<TextureRegion> splatterAnimation;

    public BloodSplatter(Main game) {
        super();
        stateTime = 0.0f;
        active = false;
        initializeSplatterAnimation(game.assets.manager);
    }

    /**
     * @param manager   an AssetManager to load the textures from
     */
    private void initializeSplatterAnimation(AssetManager manager) {
        // create the animation
        TextureAtlas bloodAtlas = manager.get(Assets.BLOOD_ATLAS);
        splatterAnimation = new Animation<>(0.03f, bloodAtlas.findRegions("BloodSplatter"));
    }

    public void activate(float x, float y) {
        active = true;
        stateTime = 0.0f;
        position.set(x, y);
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public void update(float deltaTime) {
        if (!active) {
            return;
        }

        stateTime += deltaTime;
        if (splatterAnimation.isAnimationFinished(stateTime)) {
            deactivate();
        }
    }

    @Override
    public void render(Batch batch) {
        if (!active) {
            return;
        }

        // draw current animation frame
        TextureRegion currentFrame = splatterAnimation.getKeyFrame(stateTime);
        batch.draw(
            currentFrame,
            position.x,
            position.y,
            currentFrame.getRegionWidth() * GlobalVariables.WORLD_SCALE,
            currentFrame.getRegionHeight() * GlobalVariables.WORLD_SCALE
        );
    }

}
