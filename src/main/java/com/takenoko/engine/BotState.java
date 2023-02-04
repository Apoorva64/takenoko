package com.takenoko.engine;

import static com.takenoko.engine.BotManager.DEFAULT_AVAILABLE_ACTIONS;

import com.takenoko.actions.Action;
import com.takenoko.actions.ActionResult;
import com.takenoko.actions.NoActionAction;
import com.takenoko.actions.actors.MoveGardenerAction;
import com.takenoko.actions.actors.MovePandaAction;
import com.takenoko.actions.annotations.ActionAnnotation;
import com.takenoko.actions.annotations.ActionType;
import com.takenoko.actions.improvement.ApplyImprovementFromInventoryAction;
import com.takenoko.actions.irrigation.DrawIrrigationAction;
import com.takenoko.actions.objective.DrawObjectiveAction;
import com.takenoko.actions.objective.RedeemObjectiveAction;
import com.takenoko.actions.tile.DrawTileAction;
import com.takenoko.inventory.Inventory;
import com.takenoko.objective.Objective;
import com.takenoko.objective.PandaObjective;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** This class is used to store the state of a bot. */
public class BotState { // DEFAULT VALUES
    private static final int DEFAULT_NUMBER_OF_ACTIONS = 2;
    public static final int MAX_OBJECTIVES = 5;

    private int numberOfActions;
    private final List<Objective> objectives;
    private final List<Objective> achievedObjectives;
    private final List<Objective> redeemedObjectives;
    private final Inventory inventory;
    private List<Class<? extends Action>> availableActions;
    private List<Class<? extends Action>> alreadyDoneActions = new ArrayList<>();

    public BotState(
            int numberOfActions,
            Inventory inventory,
            List<Class<? extends Action>> availableActions) {
        this.numberOfActions = numberOfActions;
        this.objectives = new ArrayList<>();
        this.inventory = inventory;
        this.availableActions = availableActions;
        this.achievedObjectives = new ArrayList<>();
        this.redeemedObjectives = new ArrayList<>();
    }

    public BotState() {
        this(DEFAULT_NUMBER_OF_ACTIONS, new Inventory(), new ArrayList<>());
    }

    public void setNumberOfActions(int numberOfActions) {
        this.numberOfActions = numberOfActions;
    }

    /**
     * Get the current Objectives of the bot
     *
     * @return Objectives
     */
    public List<Objective> getObjectives() {
        return new ArrayList<>(objectives);
    }

    /**
     * Set the current Objective of the bot
     *
     * @param objective the objectives
     */
    public void addObjective(Objective objective) {
        this.objectives.add(objective);
    }

    /**
     * @return number of actions the bot can do in a turn
     */
    protected int getNumberOfActions() {
        return numberOfActions;
    }

    /**
     * @return the number of bamboo eaten by the bot
     */
    public int getEatenBambooCounter() {
        return inventory.getBambooCount();
    }

    /**
     * Return the bot inventory
     *
     * @return the bot inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Return the list of available actions. If actions of FORCED type are available, only these
     * actions are returned else all available actions are returned.
     *
     * @return the list of available actions
     */
    public List<Class<? extends Action>> getAvailableActions() {
        List<Class<? extends Action>> forcedActions =
                availableActions.stream()
                        .filter(
                                action ->
                                        action.getAnnotation(ActionAnnotation.class).value()
                                                == ActionType.FORCED)
                        .toList();

        if (forcedActions.isEmpty()) {
            return availableActions;
        } else {
            return forcedActions;
        }
    }

    /**
     * Set the list of available actions
     *
     * @param availableActions the list of available actions
     */
    public void setAvailableActions(List<Class<? extends Action>> availableActions) {
        this.availableActions = availableActions;
    }

    /**
     * add an action to the list of available actions
     *
     * @param action the action to add
     */
    public void addAvailableAction(Class<? extends Action> action) {
        this.availableActions.add(action);
    }

    /**
     * add a list of actions to the list of available actions
     *
     * @param actions the list of actions to add
     */
    public void addAvailableActions(List<Class<? extends Action>> actions) {
        this.availableActions.addAll(actions);
    }

    /** add an action to the number of actions to plau this turn */
    public void addAction() {
        numberOfActions++;
    }

    /** reset everything to the default values */
    public void reset() {
        objectives.clear();
        achievedObjectives.clear();
        redeemedObjectives.clear();
        this.inventory.clear();
        this.numberOfActions = DEFAULT_NUMBER_OF_ACTIONS;
        this.availableActions = new ArrayList<>();
        this.alreadyDoneActions.clear();
    }

    /**
     * make a copy of the current state
     *
     * @return the copy
     */
    public BotState copy() {
        return new BotState(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotState botState = (BotState) o;
        return getNumberOfActions() == botState.getNumberOfActions()
                && getObjectives().equals(botState.getObjectives())
                && getInventory().equals(botState.getInventory())
                && getObjectiveScore() == botState.getObjectiveScore()
                && getAchievedObjectives().equals(botState.getAchievedObjectives())
                && this.availableActions.equals(botState.getAvailableActions())
                && this.redeemedObjectives.equals(botState.getRedeemedObjectives())
                && this.alreadyDoneActions.equals(botState.getAlreadyDoneActions());
    }

    /**
     * Get the list of the redeemed objectives
     *
     * @return the list of the redeemed objectives
     */
    public List<Objective> getRedeemedObjectives() {
        return new ArrayList<>(redeemedObjectives);
    }

    /**
     * Copy constructor
     *
     * @param botState the state to copy
     */
    public BotState(BotState botState) {
        this.numberOfActions = botState.numberOfActions;
        // Objectives
        this.objectives = new ArrayList<>();
        for (Objective objective : botState.objectives) {
            this.objectives.add(objective.copy());
        }
        // Achieved objectives
        this.achievedObjectives = new ArrayList<>();
        for (Objective objective : botState.achievedObjectives) {
            this.achievedObjectives.add(objective.copy());
        }

        // Redeemed objectives
        this.redeemedObjectives = new ArrayList<>();
        for (Objective objective : botState.redeemedObjectives) {
            this.redeemedObjectives.add(objective.copy());
        }

        this.inventory = botState.getInventory().copy();
        this.availableActions = new ArrayList<>(botState.availableActions);
        this.alreadyDoneActions = new ArrayList<>(botState.alreadyDoneActions);
    }

    /**
     * get the score of the achieved objectives
     *
     * @return the score of the achieved objectives
     */
    public int getObjectiveScore() {
        return redeemedObjectives.stream().mapToInt(Objective::getPoints).sum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getNumberOfActions(),
                getObjectives(),
                getInventory(),
                getAchievedObjectives(),
                getObjectiveScore(),
                this.availableActions,
                this.redeemedObjectives,
                this.alreadyDoneActions);
    }

    /** clear the list of available actions of the FORCED type */
    private void clearForcedActions() {
        availableActions.removeIf(
                action ->
                        action.getAnnotation(ActionAnnotation.class).value() == ActionType.FORCED);
    }

    /**
     * update an action in available actions
     *
     * @param action the action to update
     * @param actionResult the result of the action
     */
    public void updateAvailableActions(Action action, ActionResult actionResult) {
        this.availableActions.remove(action.getClass());
        this.alreadyDoneActions.add(action.getClass());
        this.clearForcedActions();
        this.addAvailableActions(actionResult.availableActions());
        this.setNumberOfActions(this.getNumberOfActions() - actionResult.cost());
    }

    /**
     * get the list of achieved objectives
     *
     * @return the list of achieved objectives
     */
    public List<Objective> getAchievedObjectives() {
        return new ArrayList<>(achievedObjectives);
    }

    /**
     * for each objective, check if it is achieved
     *
     * @param board the board
     * @param botManager the bot manager
     */
    public void verifyObjectives(Board board, BotManager botManager) {
        for (Objective objective : objectives) {
            objective.verify(board, botManager);
        }
        for (Objective objective : achievedObjectives) {
            objective.verify(board, botManager);
        }
    }

    /**
     * update the objectives
     *
     * @param board the board
     * @param botManager the bot manager
     */
    public void update(Board board, BotManager botManager) {
        updateObjectives(board, botManager);
        updateDefaultActions(board);
    }

    /**
     * update to the defaults actions
     *
     * @param board the board
     */
    private void updateDefaultActions(Board board) {
        if (!canDrawObjective(board)) {
            availableActions.removeAll(Collections.singleton(DrawObjectiveAction.class));
        } else if (!availableActions.contains(DrawObjectiveAction.class)) {
            availableActions.add(DrawObjectiveAction.class);
        }

        if (!canRedeemObjective()) {
            availableActions.removeAll(Collections.singleton(RedeemObjectiveAction.class));
        } else if (!availableActions.contains(RedeemObjectiveAction.class)) {
            availableActions.add(RedeemObjectiveAction.class);
        }

        if (canDrawTile(board)) {
            availableActions.removeAll(Collections.singleton(DrawTileAction.class));
        } else if (!availableActions.contains(DrawTileAction.class)) {
            availableActions.add(DrawTileAction.class);
        }

        if (canDrawIrrigation(board)) {
            availableActions.removeAll(Collections.singleton(DrawIrrigationAction.class));
        } else if (!availableActions.contains(DrawIrrigationAction.class)) {
            availableActions.add(DrawIrrigationAction.class);
        }

        if (canMoveGardener(board)) {
            availableActions.removeAll(Collections.singleton(MoveGardenerAction.class));
        } else if (!availableActions.contains(MoveGardenerAction.class)) {
            availableActions.add(MoveGardenerAction.class);
        }

        if (canMovePanda(board)) {
            availableActions.removeAll(Collections.singleton(MovePandaAction.class));
        } else if (!availableActions.contains(MovePandaAction.class)) {
            availableActions.add(MovePandaAction.class);
        }

        if (availableActions.isEmpty()) {
            availableActions.add(NoActionAction.class);
        }

        if (!canPlaceImprovement(board)) {
            availableActions.removeAll(
                    Collections.singleton(ApplyImprovementFromInventoryAction.class));
        } else if (!availableActions.contains(ApplyImprovementFromInventoryAction.class)) {
            availableActions.add(ApplyImprovementFromInventoryAction.class);
        }
    }

    /**
     * update the objectives
     *
     * @param board the board
     * @param botManager the bot manager
     */
    private void updateObjectives(Board board, BotManager botManager) {
        verifyObjectives(board, botManager);

        // If objective is achieved, add it to the list of achieved objectives
        List<Objective> toAchieve = new ArrayList<>();
        for (Objective objective : objectives) {
            if (objective.isAchieved()) {
                toAchieve.add(objective);
            }
        }
        for (Objective objective : toAchieve) {
            setObjectiveAchieved(objective);
        }

        // If objective is no more achievable, remove it from the list of objectives
        List<Objective> toRemove = new ArrayList<>();
        for (Objective objective : achievedObjectives) {
            if (!objective.isAchieved()) {
                toRemove.add(objective);
            }
        }
        for (Objective objective : toRemove) {
            setObjectiveNotAchieved(objective);
        }
    }

    /**
     * set an objective as not achieved
     *
     * @param objective the objective
     */
    public void setObjectiveNotAchieved(Objective objective) {
        this.achievedObjectives.remove(objective);
        this.objectives.add(objective);
    }

    /**
     * set an objective as achieved
     *
     * @param objective the objective
     */
    public void setObjectiveAchieved(Objective objective) {
        this.objectives.remove(objective);
        this.achievedObjectives.add(objective);
    }

    /**
     * redeem an objective
     *
     * @param objective the objective
     */
    public void redeemObjective(Objective objective) {
        this.achievedObjectives.remove(objective);
        if (objective instanceof PandaObjective pandaObjective) {
            this.inventory.useBamboo(pandaObjective.getBambooTarget());
        }
        this.redeemedObjectives.add(objective);
    }

    /**
     * can draw an objective
     *
     * @param board the board
     * @return true if the player can draw an objective
     */
    public boolean canDrawObjective(Board board) {
        return (objectives.size() + achievedObjectives.size()) < MAX_OBJECTIVES
                && !board.isObjectiveDeckEmpty()
                && !alreadyDoneActions.contains(DrawObjectiveAction.class);
    }

    /**
     * can draw a tile
     *
     * @param board the board
     * @return true if the player can draw a tile
     */
    private boolean canDrawTile(Board board) {
        return !board.isTileDeckEmpty() && !alreadyDoneActions.contains(DrawTileAction.class);
    }

    /**
     * can place an irrigation
     *
     * @param board the board
     * @return true if the player can place an irrigation
     */
    private boolean canDrawIrrigation(Board board) {
        return board.hasIrrigation() && !alreadyDoneActions.contains(DrawIrrigationAction.class);
    }

    /**
     * can move an actor
     *
     * @param board the board
     * @return true if the player can move an actor
     */
    private boolean canMoveActors(Board board) {
        return !board.getTiles().isEmpty();
    }

    /**
     * can move a gardener
     *
     * @param board the board
     * @return true if the player can move a gardener
     */
    private boolean canMoveGardener(Board board) {
        return canMoveActors(board) && !alreadyDoneActions.contains(MoveGardenerAction.class);
    }

    /**
     * can move a panda
     *
     * @param board the board
     * @return true if the player can move a panda
     */
    private boolean canMovePanda(Board board) {
        return canMoveActors(board) && !alreadyDoneActions.contains(MovePandaAction.class);
    }

    /**
     * can redeem an objective
     *
     * @return true if the player can redeem an objective
     */
    public boolean canRedeemObjective() {
        return !achievedObjectives.isEmpty();
    }

    /**
     * can place an improvement from the inventory
     *
     * @param board the board
     * @return true if the player can place an improvement from the inventory
     */
    private boolean canPlaceImprovement(Board board) {
        return !board.getAvailableImprovementPositions().isEmpty();
    }

    /**
     * Returns the sum of the points of all the panda objectives
     *
     * @return the sum of the points of all the panda objectives
     */
    public int getPandaObjectiveScore() {
        return redeemedObjectives.stream()
                .filter(PandaObjective.class::isInstance)
                .mapToInt(Objective::getPoints)
                .sum();
    }

    /**
     * reset the available actions
     *
     * @param board the board
     */
    public void resetAvailableActions(Board board) {
        this.availableActions = new ArrayList<>(DEFAULT_AVAILABLE_ACTIONS);
        this.alreadyDoneActions = new ArrayList<>();
        updateDefaultActions(board);
    }

    /**
     * get the list of already done actions
     *
     * @return the list of already done actions
     */
    public List<Class<? extends Action>> getAlreadyDoneActions() {
        return new ArrayList<>(alreadyDoneActions);
    }
}
