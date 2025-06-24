package com.berthouex.stickfight.resources;

import com.badlogic.gdx.graphics.Color;

public class GlobalVariables {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 480;

    // World Variables
    // World units that are used in place of direct reference to pixels.
    public static final float WORLD_WIDTH = 80.0f;
    public static final float WORLD_HEIGHT = 48.0f;
    public static final float MIN_WORLD_HEIGHT = WORLD_HEIGHT * 0.85f;
    public static final float WORLD_SCALE = 0.5f * 0.1f;

    // Colors
    public static final Color GOLD = new Color(0.94f, 0.85f, 0.32f, 1.0f);

    private GlobalVariables() {

    }

}
