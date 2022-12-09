package com.takenoko;

import com.takenoko.ui.ConsoleUserInterface;

public abstract class PlayableManager {
    private static final int DEFAULT_NUMBER_OF_ACTIONS = 2;
    private final int numberOfActions;
    private final Objective placeTileObjective;
    private final ConsoleUserInterface consoleUserInterface;

    public PlayableManager() {
        numberOfActions = DEFAULT_NUMBER_OF_ACTIONS;
        placeTileObjective = new PlaceTileObjective(2);
        consoleUserInterface = new ConsoleUserInterface();
    }

    public String getObjectiveDescription() {
        return placeTileObjective.toString();
    }

    protected int getNumberOfActions() {
        return numberOfActions;
    }

    public void displayMessage(String message) {
        consoleUserInterface.displayMessage(message);
    }

    public boolean objectiveIsAchieved() {
        return placeTileObjective.isAchieved();
    }

    public void verifyObjective(Board board) {
        placeTileObjective.verify(board);
    }
}
