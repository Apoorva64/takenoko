package com.takenoko.engine;

import com.takenoko.objective.EatBambooObjective;
import com.takenoko.objective.Objective;
import com.takenoko.player.Action;
import com.takenoko.player.Playable;
import com.takenoko.player.TilePlacingBot;
import com.takenoko.ui.ConsoleUserInterface;

public class BotManager {
    // DEFAULT VALUES
    private static final int DEFAULT_NUMBER_OF_ACTIONS = 2;
    private static final Objective DEFAULT_OBJECTIVE = new EatBambooObjective(1);
    private static final ConsoleUserInterface DEFAULT_CONSOLE_USER_INTERFACE =
            new ConsoleUserInterface();
    private static final String DEFAULT_NAME = "Joe";
    private static final Playable DEFAULT_PLAYABLE = new TilePlacingBot();
    // ATTRIBUTES
    private final int numberOfActions;
    private Objective objective;
    private final ConsoleUserInterface consoleUserInterface;
    private int eatenBambooCounter = 0;
    private final String name;
    private final Playable playable;

    protected BotManager(
            int numberOfActions,
            Objective objective,
            ConsoleUserInterface consoleUserInterface,
            String name,
            Playable playable) {
        this.numberOfActions = numberOfActions;
        this.objective = objective;
        this.consoleUserInterface = consoleUserInterface;
        this.name = name;
        this.playable = playable;
    }

    protected BotManager() {
        this(
                DEFAULT_NUMBER_OF_ACTIONS,
                DEFAULT_OBJECTIVE,
                DEFAULT_CONSOLE_USER_INTERFACE,
                DEFAULT_NAME,
                DEFAULT_PLAYABLE);
    }

    public BotManager(Playable playable) {
        this(
                DEFAULT_NUMBER_OF_ACTIONS,
                DEFAULT_OBJECTIVE,
                DEFAULT_CONSOLE_USER_INTERFACE,
                DEFAULT_NAME,
                playable);
    }

    public void playBot(Board board) {
        for (int i = 0; i < this.getNumberOfActions(); i++) {
            Action action = playable.chooseAction(board);
            action.execute(board, this);
            verifyObjective(board);
            if (isObjectiveAchieved()) {
                return;
            }
        }
    }

    public String getObjectiveDescription() {
        if (objective != null) {
            return objective.toString();
        }
        return "No current objective";
    }

    protected int getNumberOfActions() {
        return numberOfActions;
    }

    public void displayMessage(String message) {
        consoleUserInterface.displayMessage(message);
    }

    public boolean isObjectiveAchieved() {
        if (objective != null) {
            return objective.isAchieved();
        }
        return false;
    }

    public void verifyObjective(Board board) {
        if (objective != null) {
            objective.verify(board, this);
        }
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public String getName() {
        return name;
    }

    public int getEatenBambooCounter() {
        return eatenBambooCounter;
    }

    public void incrementBambooCounter() {
        eatenBambooCounter++;
    }
}
