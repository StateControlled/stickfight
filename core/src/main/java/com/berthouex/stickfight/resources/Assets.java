package com.berthouex.stickfight.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * Stores references to game assets, loads those assets, and disposes of them.
 */
public class Assets implements Manager {
    public final AssetManager manager = new AssetManager();

    // gameplay assets
    public static final String BACKGROUND_TEXTURE = "textures/Background.png";
    public static final String FRONT_ROPES_TEXTURE = "textures/FrontRopes.png";
    public static final String IDLE_SPRITE_SHEET = "sprites/IdleSpriteSheet.png";
    public static final String WALK_SPRITE_SHEET = "sprites/WalkSpriteSheet.png";
    public static final String PUNCH_SPRITE_SHEET = "sprites/PunchSpriteSheet.png";
    public static final String KICK_SPRITE_SHEET = "sprites/KickSpriteSheet.png";
    public static final String HURT_SPRITE_SHEET = "sprites/HurtSpriteSheet.png";
    public static final String BLOCK_SPRITE_SHEET = "sprites/BlockSpriteSheet.png";
    public static final String WIN_SPRITE_SHEET = "sprites/WinSpriteSheet.png";
    public static final String LOSE_SPRITE_SHEET = "sprites/LoseSpriteSheet.png";
    public static final String GAMEPLAY_BUTTONS_ATLAS = "textures/GameplayButtons.atlas";
    public static final String BLOOD_ATLAS = "textures/Blood.atlas";

    // fonts
    public static final String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
    public static final String SMALL_FONT = "smallFont.ttf";
    public static final String MEDIUM_FONT = "mediumFont.ttf";
    public static final String LARGE_FONT = "largeFont.ttf";

    // audio assets
    public static final String BLOCK_SOUND = "audio/block.mp3";
    public static final String BOO_SOUND = "audio/boo.mp3";
    public static final String CHEER_SOUND = "audio/cheer.mp3";
    public static final String CLICK_SOUND = "audio/click.mp3";
    public static final String HIT_SOUND = "audio/hit.mp3";
    public static final String MUSIC = "audio/music.ogg";

    // menu assets
    public static final String MENU_ITEMS_ATLAS = "textures/MenuItems.atlas";

    /**
     * Loads all assets.
     */
    @Override
    public void load() {
        loadGameplayAssets();
        loadFonts();
        loadAudio();
        loadMenuAssets();
    }

    /**
     * Assets are loaded asynchronously by LibGDX.
     */
    private void loadGameplayAssets() {
        TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;

        manager.load(BACKGROUND_TEXTURE, Texture.class, parameter);
        manager.load(FRONT_ROPES_TEXTURE, Texture.class, parameter);
        manager.load(IDLE_SPRITE_SHEET, Texture.class, parameter);
        manager.load(WALK_SPRITE_SHEET, Texture.class, parameter);
        manager.load(PUNCH_SPRITE_SHEET, Texture.class, parameter);
        manager.load(KICK_SPRITE_SHEET, Texture.class, parameter);
        manager.load(HURT_SPRITE_SHEET, Texture.class, parameter);
        manager.load(BLOCK_SPRITE_SHEET, Texture.class, parameter);
        manager.load(WIN_SPRITE_SHEET, Texture.class, parameter);
        manager.load(LOSE_SPRITE_SHEET, Texture.class, parameter);

        manager.load(GAMEPLAY_BUTTONS_ATLAS, TextureAtlas.class);
        manager.load(BLOOD_ATLAS, TextureAtlas.class);
    }

    private void loadFonts() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter smallFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        smallFont.fontFileName = ROBOTO_REGULAR;
        smallFont.fontParameters.size = 32;
        smallFont.fontParameters.minFilter = Texture.TextureFilter.Linear;
        smallFont.fontParameters.magFilter = Texture.TextureFilter.Linear;
        manager.load(SMALL_FONT, BitmapFont.class, smallFont);

        FreetypeFontLoader.FreeTypeFontLoaderParameter mediumFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mediumFont.fontFileName = ROBOTO_REGULAR;
        mediumFont.fontParameters.size = 106;
        mediumFont.fontParameters.borderWidth = 4;
        mediumFont.fontParameters.minFilter = Texture.TextureFilter.Linear;
        mediumFont.fontParameters.magFilter = Texture.TextureFilter.Linear;
        manager.load(MEDIUM_FONT, BitmapFont.class, mediumFont);

        FreetypeFontLoader.FreeTypeFontLoaderParameter largeFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        largeFont.fontFileName = ROBOTO_REGULAR;
        largeFont.fontParameters.size = 150;
        largeFont.fontParameters.borderWidth = 6;
        largeFont.fontParameters.minFilter = Texture.TextureFilter.Linear;
        largeFont.fontParameters.magFilter = Texture.TextureFilter.Linear;
        manager.load(LARGE_FONT, BitmapFont.class, largeFont);
    }

    private void loadAudio() {
        manager.load(BLOCK_SOUND, Sound.class);
        manager.load(BOO_SOUND, Sound.class);
        manager.load(CLICK_SOUND, Sound.class);
        manager.load(CHEER_SOUND, Sound.class);
        manager.load(HIT_SOUND, Sound.class);
        manager.load(MUSIC, Music.class);
    }

    private void loadMenuAssets() {
        // load menu items texture atlas
        manager.load(MENU_ITEMS_ATLAS, TextureAtlas.class);
    }

    public void dispose() {
        manager.dispose();
    }

    public Assets() {

    }

}
