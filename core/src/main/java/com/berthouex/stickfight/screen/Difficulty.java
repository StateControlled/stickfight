package com.berthouex.stickfight.screen;

public enum Difficulty {
    EASY(0.1f),
    MEDIUM(0.07f),
    HARD(0.01f);

    private final float contactDecisionDelay;

    Difficulty(float contactDecisionDelay) {
        this.contactDecisionDelay = contactDecisionDelay;
    }

    public float contactDecisionDelay() {
        return contactDecisionDelay;
    }

    public Difficulty nextDifficulty() {
        return switch(this) {
            case EASY -> MEDIUM;
            case MEDIUM -> HARD;
            case HARD -> EASY;
        };
    }

}
