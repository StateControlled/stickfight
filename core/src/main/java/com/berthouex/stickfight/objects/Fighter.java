package com.berthouex.stickfight.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.berthouex.stickfight.Main;
import com.berthouex.stickfight.resources.Assets;
import com.berthouex.stickfight.resources.GlobalVariables;

public class Fighter extends GameObject {
    // number of frame rows and columns in sprite sheet
    public static final int FRAME_ROWS = 2;
    public static final int FRAME_COLS = 3;

    public static final float MOVEMENT_SPEED = 10.0f;
    public static final float MAX_LIFE = 10.0f;
    public static final float HIT_STRENGTH = 5.0f;
    public static final float BLOCK_DAMAGE_FACTOR = 0.2f;

    private String name;
    private Color color;

    /**
     * A state that corresponds to the animations and sprite sheets.
     */
    public enum State {
        BLOCK,
        HURT,
        IDLE,
        KICK,
        LOSE,
        PUNCH,
        WALK,
        WIN
    }

    private State state;
    private float stateTime;
    private State renderState;
    private float renderStateTime;
    private final Vector2 position = new Vector2();
    private final Vector2 movementDirection = new Vector2();
    private float life;
    private int facing; // -1 or 1
    private boolean madeContact; // whether the attack has hit

    // animations
    private Animation<TextureRegion> blockAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> kickAnimation;
    private Animation<TextureRegion> loseAnimation;
    private Animation<TextureRegion> punchAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> winAnimation;

    public Fighter(Main game, String name, Color color) {
        this.name = name;
        this.color = color;
        initializeAnimations(game.assets.manager);
    }

    private void initializeAnimations(AssetManager manager) {
        initializeBlockAnimation(manager);
        initializeHurtAnimation(manager);
        initializeIdleAnimation(manager);
        initializeKickAnimation(manager);
        initializeLoseAnimation(manager);
        initializePunchAnimation(manager);
        initializeWalkAnimation(manager);
        initializeWinAnimation(manager);
    }

    public void alterFromFighterChoice(FighterChoice fighterChoice) {
        this.name = fighterChoice.getName();
        this.color = fighterChoice.getColor();
    }

    public void getReady(float positionX, float positionY) {
        state = State.IDLE;
        renderState = State.IDLE;
        stateTime = 0.0f;
        renderStateTime = 0.0f;
        position.set(positionX, positionY);
        movementDirection.set(0, 0);
        life = MAX_LIFE;
        madeContact = false;
    }

    public void render(SpriteBatch batch) {
        // get current animation frame
        TextureRegion currentFrame = switch (renderState) {
            case BLOCK -> blockAnimation.getKeyFrame(renderStateTime, true);
            case HURT -> hurtAnimation.getKeyFrame(renderStateTime, false);
            case IDLE -> idleAnimation.getKeyFrame(renderStateTime, true);
            case KICK -> kickAnimation.getKeyFrame(renderStateTime, false);
            case LOSE -> loseAnimation.getKeyFrame(renderStateTime, false);
            case PUNCH -> punchAnimation.getKeyFrame(renderStateTime, false);
            case WALK -> walkAnimation.getKeyFrame(renderStateTime, true);
            default -> winAnimation.getKeyFrame(renderStateTime, true);
        };

        batch.setColor(color);
        batch.draw(
            currentFrame,   // region
            position.x,
            position.y,
            currentFrame.getRegionWidth() * 0.5f * GlobalVariables.WORLD_SCALE, // origin X
            0,  // origin Y
            currentFrame.getRegionWidth() * GlobalVariables.WORLD_SCALE,    // width
            currentFrame.getRegionHeight() * GlobalVariables.WORLD_SCALE,   // height
            facing, // scale X
            1,      // scale Y
            0       // rotation
        );
        batch.setColor(Color.WHITE);
    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;

        if (deltaTime > 0) {
            renderState = state;
            renderStateTime = stateTime;
        }

        if (state == State.WALK) {
            // if the fighter is walking move in the direction of the movement direction variable
            position.x += (movementDirection.x * MOVEMENT_SPEED * deltaTime);
            position.y += (movementDirection.y * MOVEMENT_SPEED * deltaTime);
        } else if ((state == State.PUNCH && punchAnimation.isAnimationFinished(stateTime)) ||
            (state == State.KICK && kickAnimation.isAnimationFinished(stateTime)) ||
            (state == State.HURT && hurtAnimation.isAnimationFinished(stateTime))) {
                // walk or go idle if animation is finished
            if (movementDirection.x != 0 || movementDirection.y != 0) {
                changeState(State.WALK);
            } else {
                changeState(State.IDLE);
            }
        }
    }

    public void faceLeft() {
        facing = -1;
    }

    public void faceRight() {
        facing = 1;
    }

    private void changeState(State newState) {
        state = newState;
        stateTime = 0.0f;
    }

    private void setMovement(float x, float y) {
        movementDirection.set(x, y);
        if (state == State.WALK && x == 0 && y ==0) {
            changeState(State.IDLE);
        } else if (state == State.IDLE && (x != 0 || y != 0)) {
            changeState(State.WALK);
        }
    }

    public void moveLeft() {
        setMovement(-1, movementDirection.y);
    }

    public void moveRight() {
        setMovement(1, movementDirection.y);
    }

    public void moveUp() {
        setMovement(movementDirection.x, 1);
    }

    public void moveDown() {
        setMovement(movementDirection.x, -1);
    }

    public void stopMovingLeft() {
        if (movementDirection.x == -1) {
            setMovement(0, movementDirection.y);
        }
    }

    public void stopMovingRight() {
        if (movementDirection.x == 1) {
            setMovement(0, movementDirection.y);
        }
    }

    public void stopMovingUp() {
        if (movementDirection.y == 1) {
            setMovement(movementDirection.x, 0);
        }
    }

    public void stopMovingDown() {
        if (movementDirection.y == -1) {
            setMovement(movementDirection.x, 0);
        }
    }

    public void block() {
        if (state == State.IDLE || state == State.WALK) {
            changeState(State.BLOCK);
        }
    }

    public void stopBlocking() {
        if (state == State.BLOCK) {
            if (movementDirection.x != 0 || movementDirection.y != 0) {
                changeState(State.WALK);
            } else {
                changeState(State.IDLE);
            }
        }
    }

    public boolean isBlocking() {
        return state == State.BLOCK;
    }

    public void punch() {
        if (state == State.IDLE || state == State.WALK) {
            changeState(State.PUNCH);
            madeContact = false;
        }
    }

    public void kick() {
        if (state == State.IDLE || state == State.WALK) {
            changeState(State.KICK);
            madeContact = false;
        }
    }

    public void makeContact() {
        madeContact = true;
    }

    public boolean hasMadeContact() {
        return madeContact;
    }

    public boolean isAttackActive() {
        // attack is only active if fighter has not made contact and animation has not started or is almost finished
        if (hasMadeContact()) {
            return false;
        } else if (state == State.PUNCH) {
            return stateTime > punchAnimation.getAnimationDuration() * 0.33f && stateTime < punchAnimation.getAnimationDuration() * 0.66f;
        } else if (state == State.KICK) {
            return stateTime > kickAnimation.getAnimationDuration() * 0.33f && stateTime < kickAnimation.getAnimationDuration() * 0.66f;
        } else {
            return false;
        }
    }

    public void getHit(float damage) {
        if (state == State.HURT || state == State.WIN || state == State.LOSE) {
            return;
        }

        // reduce fighters life by damage amount
        life -= (state == State.BLOCK) ? damage * BLOCK_DAMAGE_FACTOR : damage;

        if (life <= 0.0f) {
            lose();
        } else if (state != State.BLOCK) {
            changeState(State.HURT);
        }
    }

    public void lose() {
        changeState(State.LOSE);
        life = 0.0f;
    }

    public boolean hasLost() {
        return state == State.LOSE;
    }

    public void win() {
        changeState(State.WIN);
    }

    public boolean isAttacking() {
        return state == State.PUNCH || state == State.KICK;
    }

    private void initializeBlockAnimation(AssetManager manager) {
        Texture spriteSheet = manager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        blockAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeHurtAnimation(AssetManager manager) {
        Texture spriteSheet = manager.get(Assets.HURT_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        hurtAnimation = new Animation<>(0.03f, frames);
    }

    private void initializeIdleAnimation(AssetManager manager) {
        Texture spriteSheet = manager.get(Assets.IDLE_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        idleAnimation = new Animation<>(0.1f, frames);
    }

    private void initializeKickAnimation(AssetManager manager) {
        Texture spriteSheet = manager.get(Assets.KICK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        kickAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeLoseAnimation(AssetManager manager) {
        Texture spriteSheet = manager.get(Assets.LOSE_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        loseAnimation = new Animation<>(0.05f, frames);
    }

    private void initializePunchAnimation(AssetManager manager) {
        Texture spriteSheet = manager.get(Assets.PUNCH_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        punchAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeWalkAnimation(AssetManager manager) {
        Texture spriteSheet = manager.get(Assets.WALK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        walkAnimation = new Animation<>(0.08f, frames);
    }

    private void initializeWinAnimation(AssetManager manager) {
        Texture spriteSheet = manager.get(Assets.WIN_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        winAnimation = new Animation<>(0.05f, frames);
    }

    private TextureRegion[] getAnimationFrames(Texture spriteSheet) {
        TextureRegion[][] temp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] frames = new TextureRegion[FRAME_ROWS * FRAME_COLS];

        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = temp[i][j];
            }
        }

        return frames;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getLife() {
        return life;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
