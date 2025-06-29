package com.berthouex.stickfight.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject implements Updatable {
    protected float stateTime;
    protected final Vector2 position;

    public GameObject() {
        this.position = new Vector2();
    }

    public abstract void render(Batch batch);

    public Vector2 getPosition() {
        return position;
    }

}
