package com.berthouex.stickfight.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
//        stage.setDebugAll(true);

        Table mainTable = new Table();
        mainTable.setFillParent(true); // fill entire screen
        mainTable.setRound(false);
        stage.addActor(mainTable);

        // banner table, back button
        Table bannerTable = buildBannerTable();
        mainTable.add(bannerTable);
        mainTable.row().padTop(1.0f);

        // audio settings table
        Table audioTable = buildAudioTable();
        mainTable.add(audioTable);
        mainTable.row().padTop(1.0f);

        // difficulty settings table
        Table difficultyTable = buildDifficultyTable();
        mainTable.add(difficultyTable).size(difficultyTable.getWidth(), difficultyTable.getHeight());
        mainTable.row().padTop(1.0f);

        // bottom table
        Table bottomTable = buildBottomTable();
        mainTable.add(bottomTable);
    }

    private Table buildBottomTable() {
        Table bottomTable = new Table();
        bottomTable.setRound(false);

        Table fullScreenTable = buildFullScreenTable();
        bottomTable.add(fullScreenTable).size(fullScreenTable.getWidth(), fullScreenTable.getHeight());

        Table bloodTable = buildBloodTable();
        bottomTable.add(bloodTable).size(bloodTable.getWidth(), bloodTable.getHeight());

        return bottomTable;
    }

    private Table buildFullScreenTable() {
        Table fullScreenTable = new Table();
        fullScreenTable.setRound(false);
        fullScreenTable.setBackground(fullScreenSettingBackgroundImage.getDrawable());
        fullScreenTable.setSize(fullScreenSettingBackgroundImage.getWidth(), fullScreenSettingBackgroundImage.getHeight());

        fullScreenTable.add().width(fullScreenSettingBackgroundImage.getWidth() - fullScreenCheckButton.getWidth() - 2.0f); // empty cell for alignment
        fullScreenTable.add(fullScreenCheckButton).size(fullScreenCheckButton.getWidth(), fullScreenCheckButton.getHeight());

        return fullScreenTable;
    }

    private Table buildBloodTable() {
        Table bloodTable = new Table();
        bloodTable.setRound(false);
        bloodTable.setBackground(bloodSettingBackgroundImage.getDrawable());
        bloodTable.setSize(bloodSettingBackgroundImage.getWidth(), bloodSettingBackgroundImage.getHeight());

        bloodTable.add().width(bloodSettingBackgroundImage.getWidth() - bloodCheckButton.getWidth() - 2.0f); // empty cell for alignment
        bloodTable.add(bloodCheckButton).size(bloodCheckButton.getWidth(), bloodCheckButton.getHeight());

        return bloodTable;
    }

    private Table buildDifficultyTable() {
        Table difficultyTable = new Table();
        difficultyTable.setRound(false);
        difficultyTable.setBackground(difficultySettingBackgroundImage.getDrawable());
        difficultyTable.setSize(difficultySettingBackgroundImage.getWidth(), difficultySettingBackgroundImage.getHeight());

        Table difficultySelectionTable = buildDifficultySelectionTable();
        difficultyTable.add().width(difficultySettingBackgroundImage.getWidth() - difficultySelectionTable.getWidth() - 2.0f); // add empty cell for alignment
        difficultyTable.add(difficultySelectionTable).size(difficultySelectionTable.getWidth(), difficultySelectionTable.getHeight());

        return difficultyTable;
    }

    private Table buildDifficultySelectionTable() {
        Table difficultySelectionTable = new Table();
        difficultySelectionTable.setRound(false);

        Stack difficultyImageStack = new Stack();
        difficultyImageStack.add(easyImage);
        difficultyImageStack.add(mediumImage);
        difficultyImageStack.add(hardImage);

        difficultyImageStack.setSize(easyImage.getWidth(), easyImage.getHeight());

        // add buttons and image stack
        difficultySelectionTable.add(previousDifficultyButton).size(previousDifficultyButton.getWidth(), previousDifficultyButton.getHeight());
        difficultySelectionTable.add(difficultyImageStack).size(difficultyImageStack.getWidth(), difficultyImageStack.getHeight()).padLeft(0.5f).padRight(0.5f);
        difficultySelectionTable.add(nextDifficultyButton).size(nextDifficultyButton.getWidth(), nextDifficultyButton.getHeight());
        difficultySelectionTable.pack();

        return difficultySelectionTable;
    }

    private Table buildBannerTable() {
        Table bannerTable = new Table();
        bannerTable.setRound(false);
        bannerTable.add(backButton).size(backButton.getWidth(), backButton.getHeight());
        bannerTable.add(settingsImage).size(settingsImage.getWidth(), settingsImage.getHeight());
        bannerTable.add().size(backButton.getWidth(), backButton.getHeight()); // empty cell for alignment purposes
        return bannerTable;
    }

    private Table buildAudioTable() {
        Table audioTable = new Table();
        audioTable.setRound(false);

        // music table sub-table
        Table musicTable = buildMusicTable();
        audioTable.add(musicTable).size(musicTable.getWidth(), musicTable.getHeight());

        // sounds table sub-table
        Table soundsTable = buildSoundsTable();
        audioTable.add(soundsTable).size(soundsTable.getWidth(), soundsTable.getHeight());

        return audioTable;
    }

    private Table buildMusicTable() {
        Table musicTable = new Table();
        musicTable.setRound(false);
        musicTable.setBackground(musicSettingBackgroundImage.getDrawable());
        musicTable.setSize(musicSettingBackgroundImage.getWidth(), musicSettingBackgroundImage.getHeight());

        musicTable.add().width(musicSettingBackgroundImage.getWidth() - musicToggleButton.getWidth() - 2.0f); // empty cell for alignment purposes
        musicTable.add(musicToggleButton).size(musicToggleButton.getWidth(), musicToggleButton.getHeight());
        return musicTable;
    }

    private Table buildSoundsTable() {
        Table soundsTable = new Table();
        soundsTable.setRound(false);
        soundsTable.setBackground(soundSettingBackgroundImage.getDrawable());
        soundsTable.setSize(soundSettingBackgroundImage.getWidth(), soundSettingBackgroundImage.getHeight());
        soundsTable.add().width(soundSettingBackgroundImage.getWidth() - soundsToggleButton.getWidth() - 2.0f);
        soundsTable.add(soundsToggleButton).size(soundsToggleButton.getWidth(), soundsToggleButton.getHeight());
        return soundsTable;
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
