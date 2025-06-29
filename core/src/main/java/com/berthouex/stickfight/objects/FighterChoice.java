package com.berthouex.stickfight.objects;

import com.badlogic.gdx.graphics.Color;

public class FighterChoice {
    public String name;
    public float[] colorValues;
    private Color color;

    public FighterChoice() {

    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        if (color == null) {
            color = new Color(colorValues[0], colorValues[1], colorValues[2], 1.0f);
        }
        return color;
    }

}
