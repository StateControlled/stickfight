package com.berthouex.stickfight.screen;

public enum Difficulty {
    EASY    ("EASY",    0.10f, 0.40f, 0.80f, 0.20f),
    MEDIUM  ("MEDIUM",  0.07f, 0.40f, 0.80f, 0.50f),
    HARD    ("HARD",    0.01f, 0.40f, 0.80f, 0.99f);

    private final String name;
    private final float nonContactDecisionDelay;
    private final float blockChance;
    private final float attackChance;
    private final float pursuePlayerChance;

    Difficulty(String name, float nonContactDecisionDelay, float blockChance, float attackChance, float pursuePlayerChance) {
        this.name = name;
        this.nonContactDecisionDelay = nonContactDecisionDelay;
        this.blockChance = blockChance;
        this.attackChance = attackChance;
        this.pursuePlayerChance = pursuePlayerChance;
    }

    /**
     * @return this Difficulty's name in string form
     */
    public String getName() {
        return name;
    }

    /**
     * @return this Difficulty's contact decision delay
     */
    public float nonContactDecisionDelay() {
        return nonContactDecisionDelay;
    }

    /**
     * @return this Difficulty's block chance
     */
    public float blockChance() {
        return blockChance;
    }

    /**
     * @return this Difficulty's attack chance
     */
    public float attackChance() {
        return attackChance;
    }

    /**
     * @return this Difficulty's player pursuit chance
     */
    public float pursuePlayerChance() {
        return pursuePlayerChance;
    }

    /**
     * Returns the next difficulty in the series in ascending order. After reaching the last in the series,
     * returns the first difficulty option.
     *
     * @return  the next difficulty
     */
    public Difficulty nextDifficulty() {
        return switch(this) {
            case EASY -> MEDIUM;
            case MEDIUM -> HARD;
            case HARD -> EASY;
        };
    }

}
