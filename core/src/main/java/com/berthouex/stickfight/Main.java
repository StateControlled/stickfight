package com.berthouex.stickfight;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.berthouex.stickfight.objects.Fighter;
import com.berthouex.stickfight.objects.FighterChoice;
import com.berthouex.stickfight.objects.Player;
import com.berthouex.stickfight.resources.Assets;
import com.berthouex.stickfight.resources.AudioManager;
import com.berthouex.stickfight.resources.SettingsManager;
import com.berthouex.stickfight.screen.GameScreen;
import com.berthouex.stickfight.screen.LoadingScreen;
import com.berthouex.stickfight.screen.MainMenuScreen;
import com.berthouex.stickfight.screen.SettingsScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SettingsManager settingsManager;
    public ShapeRenderer shapeRenderer;
    public SpriteBatch batch;
    public Assets assets;
    public AudioManager audioManager;

    public Screen gameScreen;
    public Screen mainMenuScreen;
    public Screen settingsScreen;
    public Screen loadingScreen;

    public final List<FighterChoice> fighterChoiceList = new ArrayList<>();
    public Player player;
    public Fighter opponent;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        assets = new Assets();

        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    public void assetsLoaded() {
        settingsManager = new SettingsManager();
        settingsManager.load();
        audioManager = new AudioManager(assets.manager);

        if (settingsManager.isMusicSettingOn()) {
            audioManager.enableMusic();
        } else {
            audioManager.disableMusic();
        }

        if (settingsManager.isSoundSettingOn()) {
            audioManager.enableSounds();
        } else {
            audioManager.disableSound();
        }

        if (settingsManager.isFullScreenSettingOn()) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }

        loadFighterChoiceList();

        player = new Player(this, fighterChoiceList.get(0).getName(), fighterChoiceList.get(0).getColor());
        opponent = new Fighter(this, fighterChoiceList.get(1).getName(), fighterChoiceList.get(1).getColor());

        // screens
        gameScreen = new GameScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        settingsScreen = new SettingsScreen(this);

        setScreen(mainMenuScreen);
    }

    /**
     * Read json file
     */
    private void loadFighterChoiceList() {
        Json json = new Json();
        JsonValue fighterChoices = new JsonReader().parse(Gdx.files.internal("data/fighter_choices.json"));
        for (int i = 0; i < fighterChoices.size; i++) {
            fighterChoiceList.add(json.fromJson(FighterChoice.class, String.valueOf(fighterChoices.get(i))));
        }
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
