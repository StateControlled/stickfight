package com.berthouex.stickfight.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.berthouex.stickfight.Main;
import com.berthouex.stickfight.resources.Assets;
import com.berthouex.stickfight.resources.GlobalVariables;

public class SettingsScreen implements Screen {
    private final Main game;
    private final Stage stage;
    private final TextureAtlas menuItemsAtlas;

    // image widgets
    private Image settingsImage;
    private Image musicSettingBackgroundImage;
    private Image soundSettingBackgroundImage;
    private Image difficultySettingBackgroundImage;
    private Image fullScreenSettingBackgroundImage;
    private Image bloodSettingBackgroundImage;

    private Image easyImage;
    private Image mediumImage;
    private Image hardImage;

    // button widgets
    private Button backButton;
    private Button musicToggleButton;
    private Button soundsToggleButton;
    private Button previousDifficultyButton;
    private Button nextDifficultyButton;
    private Button fullScreenCheckButton;
    private Button bloodCheckButton;

    public SettingsScreen(Main game) {
        this.game = game;
        stage = new Stage();
        stage.setViewport(
            new ExtendViewport(
                GlobalVariables.WORLD_WIDTH,
                GlobalVariables.MIN_WORLD_HEIGHT,
                GlobalVariables.WORLD_WIDTH,
                0,
                stage.getCamera())
        );
        menuItemsAtlas = game.assets.manager.get(Assets.MENU_ITEMS_ATLAS);

        createImages();
        createButtons();
        createTables();
    }

    private void createImages() {
        settingsImage = new Image(menuItemsAtlas.findRegion("Settings"));
        settingsImage.setSize(settingsImage.getWidth() * GlobalVariables.WORLD_SCALE, settingsImage.getHeight() * GlobalVariables.WORLD_SCALE);

        musicSettingBackgroundImage = new Image(menuItemsAtlas.findRegion("MusicSettingBackground"));
        musicSettingBackgroundImage.setSize(
            musicSettingBackgroundImage.getWidth() * GlobalVariables.WORLD_SCALE,
            musicSettingBackgroundImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        soundSettingBackgroundImage = new Image(menuItemsAtlas.findRegion("SoundsSettingBackground"));
        soundSettingBackgroundImage.setSize(
            soundSettingBackgroundImage.getWidth() * GlobalVariables.WORLD_SCALE,
            soundSettingBackgroundImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        difficultySettingBackgroundImage = new Image(menuItemsAtlas.findRegion("DifficultySettingBackground"));
        difficultySettingBackgroundImage.setSize(
            difficultySettingBackgroundImage.getWidth() * GlobalVariables.WORLD_SCALE,
            difficultySettingBackgroundImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        fullScreenSettingBackgroundImage = new Image(menuItemsAtlas.findRegion("FullScreenSettingBackground"));
        fullScreenSettingBackgroundImage.setSize(
            fullScreenSettingBackgroundImage.getWidth() * GlobalVariables.WORLD_SCALE,
            fullScreenSettingBackgroundImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        bloodSettingBackgroundImage = new Image(menuItemsAtlas.findRegion("BloodSettingBackground"));
        bloodSettingBackgroundImage.setSize(
            bloodSettingBackgroundImage.getWidth() * GlobalVariables.WORLD_SCALE,
            bloodSettingBackgroundImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        easyImage = new Image(menuItemsAtlas.findRegion("Easy"));
        easyImage.setSize(
            easyImage.getWidth() * GlobalVariables.WORLD_SCALE,
            easyImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        mediumImage = new Image(menuItemsAtlas.findRegion("Medium"));
        mediumImage.setSize(
            mediumImage.getWidth() * GlobalVariables.WORLD_SCALE,
            mediumImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        hardImage = new Image(menuItemsAtlas.findRegion("Hard"));
        hardImage.setSize(
            hardImage.getWidth() * GlobalVariables.WORLD_SCALE,
            hardImage.getHeight() * GlobalVariables.WORLD_SCALE
        );
    }

    private void createButtons() {
        Button.ButtonStyle backButtonStyle = new Button.ButtonStyle();
        backButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("BackButton"));
        backButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("BackButtonDown"));
        backButton = new Button(backButtonStyle);
        backButton.setSize(
            backButton.getWidth() * GlobalVariables.WORLD_SCALE,
            backButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // toggle buttons (on and off)
        Button.ButtonStyle toggleButtonStyle = new Button.ButtonStyle(); //
        toggleButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("ToggleButtonOff"));
        toggleButtonStyle.checked = new TextureRegionDrawable(menuItemsAtlas.findRegion("ToggleButtonOn"));

        musicToggleButton = new Button(toggleButtonStyle);
        musicToggleButton.setSize(
            musicToggleButton.getWidth() * GlobalVariables.WORLD_SCALE,
            musicToggleButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        soundsToggleButton = new Button(toggleButtonStyle);
        soundsToggleButton.setSize(
            soundsToggleButton.getWidth() * GlobalVariables.WORLD_SCALE,
            soundsToggleButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // triangle buttons (previous and next)
        Button.ButtonStyle triangleButtonStyle = new Button.ButtonStyle();
        triangleButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("TriangleButton"));
        triangleButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("TriangleButtonDown"));

        previousDifficultyButton = new Button(triangleButtonStyle);
        previousDifficultyButton.setSize(
            previousDifficultyButton.getWidth() * GlobalVariables.WORLD_SCALE,
            previousDifficultyButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        previousDifficultyButton.setTransform(true);    // flip button on y-axis
        previousDifficultyButton.setOrigin(previousDifficultyButton.getWidth() / 2.0f, previousDifficultyButton.getHeight() / 2.0f);
        previousDifficultyButton.setScaleX(-1);         // flip button on y-axis

        nextDifficultyButton = new Button(triangleButtonStyle);
        nextDifficultyButton.setSize(
            nextDifficultyButton.getWidth() * GlobalVariables.WORLD_SCALE,
            nextDifficultyButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // check buttons
        Button.ButtonStyle checkButtonStyle = new Button.ButtonStyle();
        checkButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("CheckButtonOff"));
        checkButtonStyle.checked = new TextureRegionDrawable(menuItemsAtlas.findRegion("CheckButtonOn"));

        fullScreenCheckButton = new Button(checkButtonStyle);
        fullScreenCheckButton.setSize(
            fullScreenCheckButton.getWidth() * GlobalVariables.WORLD_SCALE,
            fullScreenCheckButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        bloodCheckButton = new Button(checkButtonStyle);
        bloodCheckButton.setSize(
            bloodCheckButton.getWidth() * GlobalVariables.WORLD_SCALE,
            bloodCheckButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

    }

    private void createTables() {
        
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(GlobalVariables.BLUE_BACKGROUND);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
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
        stage.dispose();
    }

}
