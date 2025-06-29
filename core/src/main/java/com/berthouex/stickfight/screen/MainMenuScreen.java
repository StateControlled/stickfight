package com.berthouex.stickfight.screen;

import java.util.Locale;
import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.berthouex.stickfight.Main;
import com.berthouex.stickfight.objects.FighterChoice;
import com.berthouex.stickfight.resources.Assets;
import com.berthouex.stickfight.resources.GlobalVariables;

public class MainMenuScreen implements Screen {
    private final Main game;
    private final Stage stage;
    private final TextureAtlas menuItemsAtlas;

    // image widgets
    private Image logoImage;
    private Image fighterDisplayBackgroundImage;
    private Image fighterDisplayImage;

    // button widgets
    /** Starts the game **/
    private Button playGameButton;
    /** Switches to settings screen **/
    private Button settingsButton;
    /** Exits the game **/
    private Button quitGameButton;
    /** Select previous fighter **/
    private Button previousFighterButton;
    /** Select next fighter **/
    private Button nextFighterButton;

    // label widgets
    private Label fighterDisplayNameLabel;

    // fighter choice
    private int currentFighterChoiceIndex;

    public MainMenuScreen(Main game) {
        this.game = game;
        this.stage = new Stage();
        stage.setViewport(
            new ExtendViewport(
                GlobalVariables.WORLD_WIDTH,
                GlobalVariables.MIN_WORLD_HEIGHT,
                GlobalVariables.WORLD_WIDTH,
                0,
                stage.getCamera()
            ));
        this.menuItemsAtlas = game.assets.manager.get(Assets.MENU_ITEMS_ATLAS);

        // create widgets
        createImages();
        createButtons();
        createLabels();

        // screen setup
        createTables();
    }

    private void createImages() {
        logoImage = new Image(menuItemsAtlas.findRegion("Logo"));
        logoImage.setSize(logoImage.getWidth() * GlobalVariables.WORLD_SCALE, logoImage.getHeight() * GlobalVariables.WORLD_SCALE);

        fighterDisplayBackgroundImage = new Image(menuItemsAtlas.findRegion("FighterDisplayBackground"));
        fighterDisplayBackgroundImage.setSize(
            fighterDisplayBackgroundImage.getWidth() * GlobalVariables.WORLD_SCALE,
            fighterDisplayBackgroundImage.getHeight() * GlobalVariables.WORLD_SCALE
        );

        fighterDisplayImage = new Image(menuItemsAtlas.findRegion("FighterDisplay"));
        fighterDisplayImage.setSize(
            fighterDisplayImage.getWidth() * GlobalVariables.WORLD_SCALE,
            fighterDisplayImage.getHeight() * GlobalVariables.WORLD_SCALE
        );
    }

    /**
     * Initializes buttons.
     */
    private void createButtons() {
        Button.ButtonStyle playGameButtonStyle = new Button.ButtonStyle();
        playGameButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("PlayGameButton"));
        playGameButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("PlayGameButtonDown"));
        playGameButton = new Button(playGameButtonStyle);
        playGameButton.setSize(
            playGameButton.getWidth() * GlobalVariables.WORLD_SCALE,
            playGameButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        Button.ButtonStyle settingsButtonStyle = new Button.ButtonStyle();
        settingsButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("SettingsButton"));
        settingsButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("SettingsButtonDown"));
        settingsButton = new Button(settingsButtonStyle);
        settingsButton.setSize(
            settingsButton.getWidth() * GlobalVariables.WORLD_SCALE,
            settingsButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        Button.ButtonStyle quitGameButtonStyle = new Button.ButtonStyle();
        quitGameButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("QuitGameButton"));
        quitGameButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("QuitGameButtonDown"));
        quitGameButton = new Button(quitGameButtonStyle);
        quitGameButton.setSize(
            quitGameButton.getWidth() * GlobalVariables.WORLD_SCALE,
            quitGameButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        // triangle buttons (previous and next)
        Button.ButtonStyle triangleButtonStyle = new Button.ButtonStyle();
        triangleButtonStyle.up = new TextureRegionDrawable(menuItemsAtlas.findRegion("TriangleButton"));
        triangleButtonStyle.down = new TextureRegionDrawable(menuItemsAtlas.findRegion("TriangleButtonDown"));

        previousFighterButton = new Button(triangleButtonStyle);
        previousFighterButton.setSize(
            previousFighterButton.getWidth() * GlobalVariables.WORLD_SCALE,
            previousFighterButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        previousFighterButton.setTransform(true);   // flip button on y-axis
        previousFighterButton.setOrigin(previousFighterButton.getWidth() / 2.0f, previousFighterButton.getHeight() / 2.0f);
        previousFighterButton.setScaleX(-1);        // flip button on y-axis

        nextFighterButton = new Button(triangleButtonStyle);
        nextFighterButton.setSize(
            nextFighterButton.getWidth() * GlobalVariables.WORLD_SCALE,
            nextFighterButton.getHeight() * GlobalVariables.WORLD_SCALE
        );

        addButtonListeners();
    }

    private void addButtonListeners() {
        playGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.audioManager.playSound(Assets.CLICK_SOUND);
                chooseRandomOpponent();
                game.setScreen(game.gameScreen);
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.audioManager.playSound(Assets.CLICK_SOUND);
                game.setScreen(game.settingsScreen);
            }
        });

        quitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.audioManager.playSound(Assets.CLICK_SOUND);
                Gdx.app.exit();
            }
        });

        previousFighterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.audioManager.playSound(Assets.CLICK_SOUND);

                if (currentFighterChoiceIndex > 0) {
                    currentFighterChoiceIndex--;
                } else {
                    currentFighterChoiceIndex = game.fighterChoiceList.size() - 1;
                }
                // set name and color to chosen fighter
                FighterChoice fighterChoice = game.fighterChoiceList.get(currentFighterChoiceIndex);
                game.player.alterFromFighterChoice(fighterChoice);
                fighterDisplayImage.setColor(fighterChoice.getColor());
                fighterDisplayNameLabel.setText(fighterChoice.getName().toUpperCase(Locale.getDefault()));
            }
        });

        nextFighterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.audioManager.playSound(Assets.CLICK_SOUND);

                if (currentFighterChoiceIndex < game.fighterChoiceList.size() - 1) {
                    currentFighterChoiceIndex++;
                } else {
                    currentFighterChoiceIndex = 0;
                }
                // set name and color to chosen fighter
                FighterChoice fighterChoice = game.fighterChoiceList.get(currentFighterChoiceIndex);
                game.player.alterFromFighterChoice(fighterChoice);

                fighterDisplayImage.setColor(fighterChoice.getColor());
                fighterDisplayNameLabel.setText(fighterChoice.getName().toUpperCase(Locale.getDefault()));
            }
        });
    }

    private void chooseRandomOpponent() {
        Optional<FighterChoice> fighterChoiceOption =
            game.fighterChoiceList.stream()
                .filter(fighter -> !fighter.getName().equals(game.player.getName()))
                .findFirst();

        if (fighterChoiceOption.isPresent()) {
            FighterChoice fighterChoice = fighterChoiceOption.get();
            game.opponent.alterFromFighterChoice(fighterChoice);
        } else {
            throw new RuntimeException("Unable to select opponent!");
        }
    }

    private void createLabels() {
        BitmapFont smallFont = game.assets.manager.get(Assets.SMALL_FONT);
        smallFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        smallFont.setUseIntegerPositions(false);

        // create label style
        Label.LabelStyle fighterDisplayNameLabelStyle = new Label.LabelStyle();
        fighterDisplayNameLabelStyle.font = smallFont;
        fighterDisplayNameLabelStyle.fontColor = Color.BLACK;

        // name label
        fighterDisplayNameLabel = new Label("DEFAULT", fighterDisplayNameLabelStyle);
    }

    private void createTables() {
//        stage.setDebugAll(true);

        // primary table layout
        Table mainTable = new Table();
        mainTable.setFillParent(true); // fill entire screen
        mainTable.setRound(false);
        stage.addActor(mainTable);

        Table leftSideTable = buildLeftSideTable();
        Table rightSideTable = buildRightSideTable();

        mainTable.add(leftSideTable);
        mainTable.add(rightSideTable).padLeft(2.0f);
    }

    /**
     * left-side table with logo and fighter selection
     * @return  a Table
     */
    private Table buildLeftSideTable() {
        Table leftSideTable = new Table();
        leftSideTable.setRound(false);
        leftSideTable.add(logoImage).size(logoImage.getWidth(), logoImage.getHeight());
        leftSideTable.row().padTop(1.5f); // add new row so next element will be added below

        // fighter selection, encompassing table nested in mainTable. located below logo
        Table fighterDisplayTable = new Table(); // "Choose A Fighter"
        fighterDisplayTable.setRound(false);
        fighterDisplayTable.setBackground(fighterDisplayBackgroundImage.getDrawable());
        fighterDisplayTable.setSize(fighterDisplayBackgroundImage.getWidth(), fighterDisplayBackgroundImage.getHeight()); // set to same size as background image
        // selection buttons and image
        Table fighterDisplayInnerTable = new Table();
        fighterDisplayInnerTable.setRound(false);
        fighterDisplayInnerTable.add(previousFighterButton).size(previousFighterButton.getWidth(), previousFighterButton.getHeight());
        fighterDisplayInnerTable.add(fighterDisplayImage).size(fighterDisplayImage.getWidth(), fighterDisplayImage.getHeight()).padLeft(0.5f).padRight(0.5f);
        fighterDisplayInnerTable.add(nextFighterButton).size(nextFighterButton.getWidth(), nextFighterButton.getHeight());
        fighterDisplayInnerTable.pack(); // makes sure the size is set correctly

        // add an empty row to fighterDisplayTable for alignment purposes
        fighterDisplayTable.add().height(fighterDisplayBackgroundImage.getHeight() / 2.0f - fighterDisplayImage.getHeight() / 2.0f - 0.5f);
        fighterDisplayTable.row();

        // add inner fighter table to fighterDisplayTable
        fighterDisplayTable.add(fighterDisplayInnerTable).size(fighterDisplayInnerTable.getWidth(), fighterDisplayInnerTable.getHeight());

        // add name label to fighterDisplayTable
        fighterDisplayTable.row();
        fighterDisplayTable.add(fighterDisplayNameLabel).height(fighterDisplayBackgroundImage.getHeight() / 2.0f - fighterDisplayImage.getHeight() / 2.0f - 0.5f);

        // add fighter display to leftSideTable
        leftSideTable.add(fighterDisplayTable).size(fighterDisplayTable.getWidth(), fighterDisplayTable.getHeight());
        return leftSideTable;
    }

    private Table buildRightSideTable() {
        Table rightSideTable = new Table();
        rightSideTable.setRound(false);
        rightSideTable.add(playGameButton).size(playGameButton.getWidth(), playGameButton.getHeight());
        rightSideTable.row().padTop(1.0f);
        rightSideTable.add(settingsButton).size(settingsButton.getWidth(), settingsButton.getHeight());
        rightSideTable.row().padTop(1.0f);
        rightSideTable.add(quitGameButton).size(quitGameButton.getWidth(), quitGameButton.getHeight());
        rightSideTable.row().padTop(1.0f);
        return rightSideTable;
    }

    // SCREEN

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // set stage as input processor

        fighterDisplayNameLabel.setText(game.player.getName().toUpperCase(Locale.getDefault()));
        fighterDisplayImage.setColor(game.player.getColor());

        // find index of player selection
        currentFighterChoiceIndex = 0;
        for (int i = 0; i < game.fighterChoiceList.size(); i++) {
            if (game.fighterChoiceList.get(i).getName().equalsIgnoreCase(game.player.getName())) {
                currentFighterChoiceIndex = i;
                break;
            }
        }
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
