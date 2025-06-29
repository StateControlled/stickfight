package com.berthouex.stickfight.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.berthouex.stickfight.screen.Difficulty;

public class SettingsManager implements Manager {
    private static final String IS_MUSIC_ON = "isMusicOn";
    private static final String ARE_SOUNDS_ON = "areSoundsOn";
    private static final String DIFFICULTY_SETTING = "difficulty";
    private static final String IS_BLOOD_ON = "isBloodOn";
    private static final String IS_FULL_SCREEN_ON = "isFullScreenOn";

    private boolean musicSettingOn = true;
    private boolean soundSettingOn = true;
    private Difficulty difficultySetting = Difficulty.EASY;
    private boolean bloodSettingOn = true;
    private boolean fullScreenSettingOn = false;

    private final Preferences preferences = Gdx.app.getPreferences("settings.prefs");

    /**
     * Get current settings from preferences file.
     */
    @Override
    public void load() {
        musicSettingOn = preferences.getBoolean(IS_MUSIC_ON, true);
        soundSettingOn = preferences.getBoolean(ARE_SOUNDS_ON, true);
        bloodSettingOn = preferences.getBoolean(IS_BLOOD_ON, true);
        fullScreenSettingOn = preferences.getBoolean(IS_FULL_SCREEN_ON, false);

        difficultySetting = Difficulty.matchOrdinal(preferences.getInteger(DIFFICULTY_SETTING, 0));
    }

    public boolean isMusicSettingOn() {
        return musicSettingOn;
    }

    public void toggleMusicSetting(boolean on) {
        if (this.musicSettingOn != on) {
            this.musicSettingOn = on;
            preferences.putBoolean(IS_MUSIC_ON, on).flush(); // flush() forces save to happen immediately
        }
    }

    public boolean isSoundSettingOn() {
        return soundSettingOn;
    }

    public void toggleSoundSetting(boolean on) {
        if (this.soundSettingOn != on) {
            this.soundSettingOn = on;
            preferences.putBoolean(ARE_SOUNDS_ON, on);
        }
    }

    public Difficulty getDifficultySetting() {
        return difficultySetting;
    }

    public void setDifficultySetting(Difficulty difficulty) {
        if (this.difficultySetting != difficulty) {
            this.difficultySetting = difficulty;
            preferences.putInteger(DIFFICULTY_SETTING, difficulty.ordinal()).flush();
        }
    }

    public boolean isBloodSettingOn() {
        return bloodSettingOn;
    }

    public void toggleBloodSetting(boolean on) {
        if (bloodSettingOn != on) {
            this.bloodSettingOn = on;
            preferences.putBoolean(IS_BLOOD_ON, on).flush();
        }
    }

    public boolean isFullScreenSettingOn() {
        return fullScreenSettingOn;
    }

    public void setFullScreenSettingOn(boolean on) {
        if (fullScreenSettingOn != on) {
            this.fullScreenSettingOn = on;
            preferences.putBoolean(IS_FULL_SCREEN_ON, on).flush();
        }
    }

}
