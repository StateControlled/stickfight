package com.berthouex.stickfight.resources;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Handles game audio
 */
public class AudioManager implements Manager {
    // Settings
    private boolean musicEnabled = true;
    private boolean soundsEnabled = true;

    private final Music music;

    private final Sound clickSound;

    private final Sound blockSound;
    private final Sound booSound;
    private final Sound cheerSound;
    private final Sound hitSound;

    private final List<Sound> allGameSounds;

    /**
     * Retrieves assets and loads them into the game.
     *
     * @param manager   an AssetManager
     */
    public AudioManager(AssetManager manager) {
        music = manager.get(Assets.MUSIC);
        clickSound = manager.get(Assets.CLICK_SOUND);
        blockSound = manager.get(Assets.BLOCK_SOUND);
        booSound = manager.get(Assets.BOO_SOUND);
        cheerSound = manager.get(Assets.CHEER_SOUND);
        hitSound = manager.get(Assets.HIT_SOUND);

        allGameSounds = new ArrayList<>(List.of(blockSound, booSound, cheerSound, hitSound));

        music.setLooping(true);
    }

    @Override
    public void load() {

    }

    public void enableMusic() {
        musicEnabled = true;

        if (!music.isPlaying()) {
            music.play();
        }
    }

    public void disableMusic() {
        musicEnabled = false;

        if (music.isPlaying()) {
            music.stop();
        }
    }

    /**
     * Toggles music on or off.
     */
    public void toggleMusic() {
        if (musicEnabled) {
            disableMusic();
        } else {
            enableMusic();
        }
    }

    /**
     * If music is enabled and isn't playing, play music.
     */
    public void playMusic() {
        if (musicEnabled && !music.isPlaying()) {
            music.play();
        }
    }

    /**
     * Pauses music. Music will resume from the pause point when re-started.
     */
    public void pauseMusic() {
        if (musicEnabled && music.isPlaying()) {
            music.pause();
        }
    }

    public void enableSounds() {
        soundsEnabled = true;
    }

    public void disableSound() {
        soundsEnabled = false;
    }

    /**
     * Play the given sound if sounds are enabled.
     *
     * @param soundAsset    a string reference to the sound to be played
     */
    public void playSound(String soundAsset) {
        if (soundsEnabled) {
            switch(soundAsset) {
                case Assets.CLICK_SOUND:
                    clickSound.play();
                    break;
                case Assets.BLOCK_SOUND:
                    blockSound.play();
                    break;
                case Assets.BOO_SOUND:
                    booSound.play();
                    break;
                case Assets.CHEER_SOUND:
                    cheerSound.play();
                    break;
                case Assets.HIT_SOUND:
                    hitSound.play();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Pause any instances of game sounds.
     */
    public void pauseGameSounds() {
        allGameSounds.forEach(Sound::pause);
    }

    /**
     * Resume paused sounds.
     */
    public void resumeGameSounds() {
        allGameSounds.forEach(Sound::resume);
    }

    /**
     * Stop all game sounds.
     */
    public void stopGameSounds() {
        allGameSounds.forEach(Sound::stop);
    }

}
