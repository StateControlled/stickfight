package com.berthouex.stickfight.screen;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.berthouex.stickfight.Main;
import com.berthouex.stickfight.objects.Fighter;
import com.berthouex.stickfight.resources.Assets;
import com.berthouex.stickfight.resources.GlobalVariables;

public class GameScreen implements Screen, InputProcessor {
    private final Main game;
    private final Viewport viewport;

    // Game
    private Difficulty difficulty = Difficulty.EASY;
    private enum GameState {
        RUNNING,
        PAUSED,
        GAME_OVER
    }
    private GameState gameState;

    private int roundsWon = 0;
    private int roundsLost = 0;
    private static final float MAX_ROUND_TIME = 99.99f;
    private float roundTimer = MAX_ROUND_TIME;

    private enum RoundState {
        STARTING,
        IN_PROGRESS,
        ENDING
    }
    private RoundState roundState;
    private float roundStateTime;
    private static final float START_ROUND_DELAY = 2.0f;
    private static final float END_ROUND_DELAY = 2.0f;
    private int currentRound;
    private static final int MAX_ROUNDS = 3;
    private static final float CRITICAL_ROUND_TIME = 10.0f;
    private static final Color CRITICAL_ROUND_TIME_COLOR = Color.RED;

    // Fonts
    private BitmapFont smallFont;
    private BitmapFont mediumFont;
    private BitmapFont largeFont;
    private static final Color DEFAULT_FONT_COLOR = Color.WHITE;

    // HUD
    private static final Color HEALTH_BAR_COLOR = Color.RED;
    private static final Color HEALTH_BAR_BACKGROUND_COLOR = GlobalVariables.GOLD;

    // Textures
    private Texture backgroundTexture;
    private Texture frontRopesTexture;
    // ring bounds
    private static final float RING_MIN_X = 7.0f;
    private static final float RING_MAX_X = 60.0f;
    private static final float RING_MIN_Y = 4.0f;
    private static final float RING_MAX_Y = 22.0f;
    private static final float RING_SLOPE = 3.16f;

    // fighters
    private static final float playerStartPositionX = 16.0f;        // values in world units, not pixels
    private static final float opponentStartPositionX = 51.0f;
    private static final float fighterStartPositionY = 15.0f;
    private static final float fighterContactDistanceX = 7.5f;
    private static final float fighterContactDistanceY = 1.5f;

    // buttons
    private Sprite playAgainButtonSprite;
    private Sprite mainMenuButtonSprite;
    private Sprite continueButtonSprite;
    private Sprite pauseButtonSprite;
    private static final float PAUSE_BUTTON_MARGIN = 1.5f;

    public GameScreen(Main game) {
        this.game = game;
        this.viewport = new ExtendViewport(
            GlobalVariables.WORLD_WIDTH,
            GlobalVariables.MIN_WORLD_HEIGHT,
            GlobalVariables.WORLD_WIDTH,
            0
        );

        createGameArea();
        setUpFonts();
        createButtons();
    }

    private void createGameArea() {
        // get textures from asset manager
        backgroundTexture = game.assets.manager.get(Assets.BACKGROUND_TEXTURE);
        frontRopesTexture = game.assets.manager.get(Assets.FRONT_ROPES_TEXTURE);
    }

    private void setUpFonts() {
        smallFont = game.assets.manager.get(Assets.SMALL_FONT);
        smallFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        smallFont.setColor(DEFAULT_FONT_COLOR);
        smallFont.setUseIntegerPositions(false);

        mediumFont = game.assets.manager.get(Assets.MEDIUM_FONT);
        mediumFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        mediumFont.setColor(DEFAULT_FONT_COLOR);
        mediumFont.setUseIntegerPositions(false);

        largeFont = game.assets.manager.get(Assets.LARGE_FONT);
        largeFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        largeFont.setColor(DEFAULT_FONT_COLOR);
        largeFont.setUseIntegerPositions(false);
    }

    private void createButtons() {
        TextureAtlas buttonTextureAtlas = game.assets.manager.get(Assets.GAMEPLAY_BUTTONS_ATLAS);
        playAgainButtonSprite = new Sprite(buttonTextureAtlas.findRegion("PlayAgainButton"));
        playAgainButtonSprite.setSize(playAgainButtonSprite.getWidth() * GlobalVariables.WORLD_SCALE, playAgainButtonSprite.getHeight() * GlobalVariables.WORLD_SCALE);

        mainMenuButtonSprite = new Sprite(buttonTextureAtlas.findRegion("MainMenuButton"));
        mainMenuButtonSprite.setSize(mainMenuButtonSprite.getWidth() * GlobalVariables.WORLD_SCALE, mainMenuButtonSprite.getHeight() * GlobalVariables.WORLD_SCALE);

        continueButtonSprite = new Sprite(buttonTextureAtlas.findRegion("ContinueButton"));
        continueButtonSprite.setSize(continueButtonSprite.getWidth() * GlobalVariables.WORLD_SCALE, continueButtonSprite.getHeight() * GlobalVariables.WORLD_SCALE);

        pauseButtonSprite = new Sprite(buttonTextureAtlas.findRegion("PauseButton"));
        pauseButtonSprite.setSize(pauseButtonSprite.getWidth() * GlobalVariables.WORLD_SCALE, pauseButtonSprite.getHeight() * GlobalVariables.WORLD_SCALE);
    }

    @Override
    public void show() {
        // process user input
        Gdx.input.setInputProcessor(this);

        // start the game
        startGame();
    }

    private void startGame() {
        gameState = GameState.RUNNING;
        roundsWon = 0;
        roundsLost = 0;

        // start round 1
        currentRound = 1;
        startRound();
    }

    private void pauseGame() {
        gameState = GameState.PAUSED;
        game.audioManager.pauseGameSounds();
        game.audioManager.pauseMusic();
    }

    private void resumeGame() {
        gameState = GameState.RUNNING;
        game.audioManager.resumeGameSounds();
        game.audioManager.playMusic();
    }

    private void startRound() {
        game.player.getReady(playerStartPositionX, fighterStartPositionY);
        game.opponent.getReady(opponentStartPositionX, fighterStartPositionY);

        roundState = RoundState.STARTING;
        roundStateTime = 0.0f;
        roundTimer = MAX_ROUND_TIME;
    }

    private void endRound() {
        roundState = RoundState.ENDING;
        roundStateTime = 0.0f;
    }

    private void winRound() {
        game.player.win();
        game.opponent.lose();
        roundsWon++;

        game.audioManager.playSound(Assets.CHEER_SOUND);
        endRound();
    }

    private void loseRound() {
        game.player.lose();
        game.opponent.win();
        roundsLost++;

        game.audioManager.playSound(Assets.BOO_SOUND);
        endRound();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        update(gameState == GameState.RUNNING ? delta : 0.0f);

        // set SpriteBatch to use camera
        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        game.batch.begin();
        // draw background
        game.batch.draw(
            backgroundTexture, 0, 0,
            backgroundTexture.getWidth() * GlobalVariables.WORLD_SCALE,
            backgroundTexture.getHeight() * GlobalVariables.WORLD_SCALE
        );

        renderFighters();
        // draw front ropes
        game.batch.draw(
            frontRopesTexture, 0, 0,
            frontRopesTexture.getWidth() * GlobalVariables.WORLD_SCALE,
            frontRopesTexture.getHeight() * GlobalVariables.WORLD_SCALE
        );

        renderHUD();

        renderPauseButton();

        if (gameState == GameState.GAME_OVER) {
            renderGameOverOverlay();
        } else {
            if (roundState == RoundState.STARTING) {
                renderStartRoundText();
            }
            // if the game is paused, draw pause overlay
            if (gameState == GameState.PAUSED) {
                renderPauseOverlay();
            }
        }

        game.batch.end();
    }

    private void renderFighters() {
        if (game.player.getPosition().y > game.opponent.getPosition().y) {
            game.player.render(game.batch);
            game.opponent.render(game.batch);
        } else {
            game.opponent.render(game.batch);
            game.player.render(game.batch);
        }
    }

    private void renderHUD() {
        float hudMargin = 1.0f;
        smallFont.draw(game.batch, "WINS: " + roundsWon + " - " + roundsLost, hudMargin, viewport.getWorldHeight() - hudMargin);
        String text = "DIFFICULTY: ";
        switch (difficulty) {
            case EASY -> text += "EASY";
            case MEDIUM -> text += "MEDIUM";
            case HARD -> text += "HARD";
        }

        smallFont.draw(game.batch, text, viewport.getWorldWidth() - hudMargin, viewport.getWorldHeight() - hudMargin, 0, Align.right, false);

        float healthBarPadding = 0.5f;
        float healthBarHeight = smallFont.getCapHeight() + healthBarPadding * 2.0f;
        float healthBarMaxWidth = 32.0f;
        float healthBarBackgroundPadding = 0.2f;
        float healthBarBackgroundHeight = healthBarHeight + healthBarBackgroundPadding * 2.0f;
        float healthBarBackgroundWidth = healthBarMaxWidth + healthBarBackgroundPadding * 2.0f;
        float healthBarBackgroundMarginTop = 0.8f;
        float healthBarBackgroundPositionY = viewport.getWorldHeight() - hudMargin - smallFont.getCapHeight() - healthBarBackgroundMarginTop - healthBarBackgroundHeight;
        float healthBarPositionY = healthBarBackgroundPositionY + healthBarBackgroundPadding;
        float fighterNamePositionY = healthBarPositionY + healthBarHeight - healthBarPadding;

        // draw health bar
        game.batch.end();
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        game.shapeRenderer.setColor(HEALTH_BAR_BACKGROUND_COLOR);
        game.shapeRenderer.rect(hudMargin, healthBarBackgroundPositionY, healthBarBackgroundWidth, healthBarBackgroundHeight);
        game.shapeRenderer.rect(viewport.getWorldWidth() - hudMargin - healthBarBackgroundWidth, healthBarBackgroundPositionY, healthBarBackgroundWidth, healthBarBackgroundHeight);

        game.shapeRenderer.setColor(HEALTH_BAR_COLOR);
        float healthBarWidth = healthBarMaxWidth * game.player.getLife() / Fighter.MAX_LIFE;
        game.shapeRenderer.rect(hudMargin + healthBarBackgroundPadding, healthBarPositionY, healthBarWidth, healthBarHeight);

        healthBarWidth = healthBarMaxWidth * game.opponent.getLife() / Fighter.MAX_LIFE;
        game.shapeRenderer.rect(viewport.getWorldWidth() - hudMargin - healthBarBackgroundPadding - healthBarWidth,
            healthBarPositionY, healthBarWidth, healthBarHeight);

        game.shapeRenderer.end();
        game.batch.begin();

        // draw names
        smallFont.draw(game.batch, game.player.getName(), hudMargin + healthBarBackgroundPadding + healthBarPadding, fighterNamePositionY);
        smallFont.draw(game.batch, game.opponent.getName(), viewport.getWorldWidth() - hudMargin - healthBarBackgroundPadding - healthBarPadding, fighterNamePositionY, 0, Align.right, false);

        // draw round timer
        if (roundTimer < CRITICAL_ROUND_TIME) {
            mediumFont.setColor(CRITICAL_ROUND_TIME_COLOR);
        }
        mediumFont.draw(
            game.batch,
            String.format(Locale.getDefault(), "%02d", (int) roundTimer),
            viewport.getWorldWidth() / 2.0f - mediumFont.getSpaceXadvance() * 2.3f,
            viewport.getWorldHeight() - hudMargin
        );
        mediumFont.setColor(DEFAULT_FONT_COLOR);
    }

    private void renderStartRoundText() {
        String text;
        if (roundStateTime < START_ROUND_DELAY * 0.5f) {
            text = "ROUND " + currentRound;
        } else {
            text = "FIGHT";
        }
        mediumFont.draw(game.batch, text, viewport.getWorldWidth() / 2.0f, viewport.getWorldHeight() / 2.0f, 0, Align.center, false);
    }

    private void renderPauseButton() {
        pauseButtonSprite.setPosition(viewport.getWorldWidth() - PAUSE_BUTTON_MARGIN - pauseButtonSprite.getWidth(), PAUSE_BUTTON_MARGIN);
        pauseButtonSprite.draw(game.batch);
    }

    /**
     * Darken the screen and render game over menu and buttons
     */
    private void renderGameOverOverlay() {
        game.batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        game.shapeRenderer.setColor(0, 0, 0, 0.7f);
        game.shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();

        // calculate layout dimensions
        float textMarginBottom = 2.0f; // spacing between button and button below
        float buttonSpacing = 0.5f;
        float layoutHeight = largeFont.getCapHeight() + textMarginBottom + playAgainButtonSprite.getHeight() + buttonSpacing + mainMenuButtonSprite.getHeight();
        float layoutPositionY = (viewport.getWorldHeight() / 2.0f) - (layoutHeight / 2.0f);

        // draw buttons
        mainMenuButtonSprite.setPosition(
            viewport.getWorldWidth() / 2.0f - mainMenuButtonSprite.getWidth() / 2.0f,
            layoutPositionY
        );
        mainMenuButtonSprite.draw(game.batch);

        playAgainButtonSprite.setPosition(
            viewport.getWorldWidth() / 2.0f - playAgainButtonSprite.getWidth() / 2.0f,
            layoutPositionY + mainMenuButtonSprite.getHeight() + buttonSpacing
        );
        playAgainButtonSprite.draw(game.batch);

        // draw text
        String text = roundsWon > roundsLost ? "YOU WON!" : "YOU LOST!";
        largeFont.draw(
            game.batch,
            text,
            viewport.getWorldWidth() / 2.0f,
            playAgainButtonSprite.getY() + playAgainButtonSprite.getHeight() + textMarginBottom + largeFont.getCapHeight(),
            0,
            Align.center,
            false
        );
    }

    private void renderPauseOverlay() {
        game.batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        game.shapeRenderer.setColor(0, 0, 0, 0.7f);
        game.shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();

        // calculate layout dimensions
        float textMarginBottom = 2.0f; // spacing between button and button below
        float buttonSpacing = 0.5f;
        float layoutHeight = largeFont.getCapHeight() + textMarginBottom + continueButtonSprite.getHeight() + buttonSpacing + mainMenuButtonSprite.getHeight();
        float layoutPositionY = (viewport.getWorldHeight() / 2.0f) - (layoutHeight / 2.0f);

        // draw buttons
        mainMenuButtonSprite.setPosition(
            viewport.getWorldWidth() / 2.0f - mainMenuButtonSprite.getWidth() / 2.0f,
            layoutPositionY
        );
        mainMenuButtonSprite.draw(game.batch);

        continueButtonSprite.setPosition(
            viewport.getWorldWidth() / 2.0f - continueButtonSprite.getWidth() / 2.0f,
            layoutPositionY + mainMenuButtonSprite.getHeight() + buttonSpacing
        );
        continueButtonSprite.draw(game.batch);

        // draw text
        largeFont.draw(
            game.batch,
            "GAME PAUSED",
            viewport.getWorldWidth() / 2.0f,
            continueButtonSprite.getY() + continueButtonSprite.getHeight() + textMarginBottom + largeFont.getCapHeight(),
            0,
            Align.center,
            false
        );
    }

    private void update(float deltaTime) {
        if (roundState == RoundState.STARTING && roundStateTime >= START_ROUND_DELAY) {
            // if the start round delay has been reached, start the round
            roundState = RoundState.IN_PROGRESS;
            roundStateTime = 0.0f;
        } else if (roundState == RoundState.ENDING && roundStateTime >= END_ROUND_DELAY) {
            // if the end round delay has been reached and the player has won or lost more than half the max number of rounds,
            // end the game, else start the next round
            if (roundsWon > MAX_ROUNDS / 2 || roundsLost > MAX_ROUNDS / 2) {
                gameState = GameState.GAME_OVER;
            } else {
                currentRound++;
                startRound();
            }
        } else {
            roundStateTime += deltaTime;
        }

        game.player.update(deltaTime);
        game.opponent.update(deltaTime);

        if (game.player.getPosition().x <= game.opponent.getPosition().x) {
            game.player.faceRight();
            game.opponent.faceLeft();
        } else {
            game.player.faceLeft();
            game.opponent.faceRight();
        }

        keepWithinRingBounds(game.player.getPosition());
        keepWithinRingBounds(game.opponent.getPosition());

        if (roundState == RoundState.IN_PROGRESS) {
            // if the round is in progress, decrease round timer
            roundTimer -= deltaTime;

            if (roundTimer <= 0.0f) {
                // possible end game
                if (game.player.getLife() >= game.opponent.getLife()) {
                    winRound();
                } else {
                    loseRound();
                }
            }

            if (areWithinContactDistance(game.player.getPosition(), game.opponent.getPosition())) {
                if (game.player.isAttackActive()) {
                    // opponent gets hit
                    game.opponent.getHit(Fighter.HIT_STRENGTH);
                    if (game.opponent.isBlocking()) {
                        // play block sound
                        game.audioManager.playSound(Assets.BLOCK_SOUND);
                    } else {
                        game.audioManager.playSound(Assets.HIT_SOUND);
                    }

                    game.player.makeContact();

                    if (game.opponent.hasLost()) {
                        winRound(); // player wins
                    }
                }
            }
        }

    }

    private void keepWithinRingBounds(Vector2 position) {
        if (position.y < RING_MIN_Y) {
            position.y = RING_MIN_Y;
        } else if (position.y > RING_MAX_Y) {
            position.y = RING_MAX_Y;
        }

        if (position.x < (position.y / RING_SLOPE) + RING_MIN_X) {
            position.x = (position.y / RING_SLOPE) + RING_MIN_X;
        } else if (position.x > (position.y / -RING_SLOPE) + RING_MAX_X) {
            position.x = (position.y / -RING_SLOPE) + RING_MAX_X;
        }
    }

    private boolean areWithinContactDistance(Vector2 position1, Vector2 position2) {
        float xDistance = Math.abs(position1.x - position2.x);
        float yDistance = Math.abs(position1.y - position2.y);
        return xDistance <= fighterContactDistanceX && yDistance <= fighterContactDistanceY;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        if (gameState == GameState.RUNNING) {
            pauseGame();
        }
        game.audioManager.pauseMusic();
    }

    @Override
    public void resume() {
        game.audioManager.playMusic();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    // INPUT PROCESSOR

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            if (gameState == GameState.RUNNING) {
                // if game is running, skip round delays
                if (roundState == RoundState.STARTING) {
                    roundStateTime = START_ROUND_DELAY;
                } else if (roundState == RoundState.ENDING) {
                    roundStateTime = END_ROUND_DELAY;
                }
            } else if (gameState == GameState.GAME_OVER) {
                // if game over, start new game
                startGame();
            } else {
                // Space also resumes game
                resumeGame();
            }
        } else if ((gameState == GameState.RUNNING || gameState == GameState.PAUSED) && keycode == Input.Keys.P) {
            // P toggles pause
            if (gameState == GameState.RUNNING) {
                pauseGame();
            } else {
                resumeGame();
            }
        } else if (keycode == Input.Keys.N) {
            // N toggles music on or off
            game.audioManager.toggleMusic();
        } else {
            if (roundState == RoundState.IN_PROGRESS) {
                // only if round is in progress check if player has pressed a movement key
                if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
                    game.player.moveLeft();
                } else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
                    game.player.moveRight();
                }

                if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    game.player.moveUp();
                } else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    game.player.moveDown();
                }
            }

            // check if player has pressed block or attack key
            if (keycode == Input.Keys.B) {
                game.player.block();
            } else if (keycode == Input.Keys.F) {
                game.player.punch();
            } else if (keycode == Input.Keys.V) {
                game.player.kick();
            }
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // if player has released a movement key, stop moving
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            game.player.stopMovingLeft();
        } else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            game.player.stopMovingRight();
        }

        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            game.player.stopMovingUp();
        } else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            game.player.stopMovingDown();
        }

        if (keycode == Input.Keys.B) {
            game.player.stopBlocking();
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // convert screen coordinates into world coordinates
        Vector3 position = new Vector3(screenX, screenY, 0);
        viewport.getCamera().unproject(position, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        if (gameState == GameState.RUNNING) {
            // pause game
            if (pauseButtonSprite.getBoundingRectangle().contains(position.x, position.y)) {
                pauseGame();
                game.audioManager.playSound(Assets.CLICK_SOUND);
            } else if (roundState == RoundState.STARTING) {
                // skip start round delay
                roundStateTime = START_ROUND_DELAY;
            } else if (roundState == RoundState.ENDING) {
                // skip end round delay
                roundStateTime = END_ROUND_DELAY;
            }
        } else {
            if (gameState == GameState.GAME_OVER && playAgainButtonSprite.getBoundingRectangle().contains(position.x, position.y)) {
                // if game over and play again button touched, start again
                startGame();
                game.audioManager.playSound(Assets.CLICK_SOUND);
            } else if (gameState == GameState.PAUSED && continueButtonSprite.getBoundingRectangle().contains(position.x, position.y)) {
                // resume game by clicking continue button
                resumeGame();
                game.audioManager.playSound(Assets.CLICK_SOUND);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
