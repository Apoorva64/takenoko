package com.takenoko.weather;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/** Dice with N sides, by default it has 6. */
public class Dice {
    private final Random random;
    private final int sides;
    private int lastRoll;

    /**
     * Specify the number of sides and the Random Generator
     *
     * @param sides number of sides of the dice
     * @param random random number generator
     */
    public Dice(int sides, Random random) {
        this.sides = sides;
        this.random = random;
        this.lastRoll = 0;
    }

    /**
     * Specify the number of sides
     *
     * @param sides number of sides of the dice
     */
    public Dice(int sides) {
        this(sides, new SecureRandom());
    }

    /**
     * Roll the dice
     *
     * @return the result of the roll (between 0 and N-1)
     */
    protected int roll() {
        lastRoll = random.nextInt(sides);
        return lastRoll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dice dice = (Dice) o;
        return sides == dice.sides;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sides);
    }

    protected int peek() {
        return lastRoll;
    }
}
