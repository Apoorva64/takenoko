package com.takenoko.stats;

import com.takenoko.layers.tile.TileColor;
import com.takenoko.objective.Objective;
import com.takenoko.objective.ObjectiveType;
import java.util.*;
import org.apache.commons.lang3.tuple.MutablePair;

/** This class represents an extended list of statistics for a specific BotManager */
public class SingleBotStatistics {
    public static final String WINS = "\t -Wins : ";
    public static final String LOSSES = "\t -Losses : ";
    public static final String IRRIGATIONS_PLACED = "\t -Irrigations Placed : ";
    public static final String FINAL_SCORE = "\t -Final Score : ";
    private int totalNbOfAction;
    private final Map<String, Integer> numericStats;
    private final Map<ObjectiveType, Integer> objectivesRedeemed;
    private final Map<TileColor, MutablePair<Integer, Integer>>
            bambooCounter; // Left eaten, right grown
    private final Map<TileColor, Integer> tilesPlaced;
    private final Map<String, MutablePair<Integer, Integer>> weathers; // Left applied, right rolled
    private final Map<String, Integer> actions;

    public SingleBotStatistics(
            Map<String, Integer> numericStats,
            Map<ObjectiveType, Integer> objectivesRedeemed,
            Map<TileColor, MutablePair<Integer, Integer>> bambooCounter,
            Map<TileColor, Integer> tilesPlaced,
            Map<String, MutablePair<Integer, Integer>> weathers,
            Map<String, Integer> actions,
            int totalNbOfAction) {
        this.numericStats = numericStats;
        this.objectivesRedeemed = objectivesRedeemed;
        this.bambooCounter = bambooCounter;
        this.tilesPlaced = tilesPlaced;
        this.weathers = weathers;
        this.actions = actions;
        this.totalNbOfAction = totalNbOfAction;
        numericStats.put(WINS, 0);
        numericStats.put(LOSSES, 0);
        numericStats.put(FINAL_SCORE, 0);
        numericStats.put(IRRIGATIONS_PLACED, 0);
    }

    public SingleBotStatistics(SingleBotStatistics singleBotStatistics) {
        this(
                singleBotStatistics.numericStats,
                singleBotStatistics.objectivesRedeemed,
                singleBotStatistics.bambooCounter,
                singleBotStatistics.tilesPlaced,
                singleBotStatistics.weathers,
                singleBotStatistics.actions,
                singleBotStatistics.totalNbOfAction);
    }

    public SingleBotStatistics(int wins, int losses, int finalScore) {
        this();
        numericStats.replace(WINS, wins);
        numericStats.replace(LOSSES, losses);
        numericStats.replace(FINAL_SCORE, finalScore);
    }

    public SingleBotStatistics() {
        this(
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>(),
                0);
    }

    public void incrementWins() {
        numericStats.replace(WINS, numericStats.get(WINS) + 1);
    }

    public void incrementLosses() {
        numericStats.replace(LOSSES, numericStats.get(LOSSES) + 1);
    }

    public void incrementIrrigationsPlaced() {
        numericStats.replace(IRRIGATIONS_PLACED, numericStats.get(IRRIGATIONS_PLACED) + 1);
    }

    public void updateScore(int toAdd) {
        numericStats.replace(FINAL_SCORE, numericStats.get(FINAL_SCORE) + toAdd);
    }

    public void updateObjectivesRedeemed(Objective objective) {
        if (objective == null) {
            throw new IllegalArgumentException();
        }
        ObjectiveType objectiveType = objective.getType();
        if (objectivesRedeemed.containsKey(objectiveType)) {
            objectivesRedeemed.replace(objectiveType, objectivesRedeemed.get(objectiveType) + 1);
        } else {
            objectivesRedeemed.put(objectiveType, 1);
        }
    }

    public void updateEatenBambooCounter(TileColor tileColor) {
        if (tileColor == null) {
            throw new IllegalArgumentException();
        }
        if (bambooCounter.containsKey(tileColor)) {
            bambooCounter.put(
                    tileColor,
                    MutablePair.of(
                            bambooCounter.get(tileColor).getLeft() + 1,
                            bambooCounter.get(tileColor).getRight()));
        } else {
            bambooCounter.put(tileColor, MutablePair.of(1, 0));
        }
    }

    public void updatePlantedBambooCounter(TileColor tileColor, int count) {
        if (tileColor == null) {
            throw new IllegalArgumentException();
        }
        if (count == 0) {
            count = 1;
        }
        if (bambooCounter.containsKey(tileColor)) {
            bambooCounter.put(
                    tileColor,
                    MutablePair.of(
                            bambooCounter.get(tileColor).getLeft(),
                            bambooCounter.get(tileColor).getRight() + count));
        } else {
            bambooCounter.put(tileColor, MutablePair.of(0, count));
        }
    }

    public void updateTilesPlacedCounter(TileColor tileColor) {
        if (tileColor == null) {
            throw new IllegalArgumentException();
        }
        if (tilesPlaced.containsKey(tileColor)) {
            tilesPlaced.put(tileColor, tilesPlaced.get(tileColor) + 1);
        } else {
            tilesPlaced.put(tileColor, 1);
        }
    }

    public void updateWeathersRolled(String weather) {
        if (weather == null) {
            throw new IllegalArgumentException();
        }
        if (weathers.containsKey(weather)) {
            weathers.replace(
                    weather,
                    MutablePair.of(
                            weathers.get(weather).getLeft(), weathers.get(weather).getRight() + 1));
        } else {
            weathers.put(weather, MutablePair.of(0, 1));
        }
    }

    public void updateWeathersApplied(String weather) {
        if (weather == null) {
            throw new IllegalArgumentException();
        }
        if (weathers.containsKey(weather))
            weathers.replace(
                    weather,
                    MutablePair.of(
                            weathers.get(weather).getLeft() + 1, weathers.get(weather).getRight()));
        else {
            weathers.put(weather, MutablePair.of(1, 0));
        }
    }

    public void updateActions(String action) {
        if (action == null) {
            throw new IllegalArgumentException();
        }
        totalNbOfAction++;
        if (actions.containsKey(action)) {
            actions.replace(action, actions.get(action) + 1);
        } else {
            actions.put(action, 1);
        }
    }

    public SingleBotStatistics copy() {
        return new SingleBotStatistics(this);
    }

    public void reset() {
        this.objectivesRedeemed.clear();
        this.bambooCounter.clear();
    }

    @Override
    public String toString() {
        StringBuilder singleBotStat = new StringBuilder();
        String lineJump = "\n \t \t \t";
        String indentation = "\t\t* ";
        TreeMap<ObjectiveType, Integer> sortedObjectives = new TreeMap<>(objectivesRedeemed);
        TreeMap<TileColor, MutablePair<Integer, Integer>> sortedBambooCounter =
                new TreeMap<>(bambooCounter);
        TreeMap<TileColor, Integer> sortedTilesPlaced = new TreeMap<>(tilesPlaced);
        TreeMap<String, Integer> sortedActions = new TreeMap<>(actions);
        singleBotStat
                .append(WINS)
                .append(numericStats.get(WINS))
                .append(lineJump)
                .append(LOSSES)
                .append(numericStats.get(LOSSES))
                .append(lineJump)
                .append(IRRIGATIONS_PLACED)
                .append(numericStats.get(IRRIGATIONS_PLACED))
                .append(lineJump)
                .append(FINAL_SCORE)
                .append(numericStats.get(FINAL_SCORE))
                .append(lineJump)
                .append("\t -Redeemed objectives : ");
        for (ObjectiveType objectiveType : sortedObjectives.keySet()) {
            singleBotStat
                    .append(lineJump)
                    .append(indentation)
                    .append(objectiveType)
                    .append(" : ")
                    .append(objectivesRedeemed.get(objectiveType));
        }
        singleBotStat.append(lineJump).append("\t -Bamboos eaten :");
        for (TileColor tileColor : sortedBambooCounter.keySet()) {
            singleBotStat
                    .append(lineJump)
                    .append(indentation)
                    .append(tileColor)
                    .append(" : ")
                    .append(bambooCounter.get(tileColor).getLeft());
        }
        singleBotStat.append(lineJump).append("\t -Bamboos grown :");
        for (TileColor tileColor : sortedBambooCounter.keySet()) {
            singleBotStat
                    .append(lineJump)
                    .append(indentation)
                    .append(tileColor)
                    .append(" : ")
                    .append(bambooCounter.get(tileColor).getRight());
        }
        singleBotStat.append(lineJump).append("\t -Tiles placed :");
        for (TileColor tileColor : sortedTilesPlaced.keySet()) {
            singleBotStat
                    .append(lineJump)
                    .append(indentation)
                    .append(tileColor)
                    .append(" : ")
                    .append(tilesPlaced.get(tileColor));
        }
        singleBotStat.append(lineJump).append("\t -Rolled Weathers :");
        for (Map.Entry<String, MutablePair<Integer, Integer>> entry : weathers.entrySet()) {
            singleBotStat
                    .append(lineJump)
                    .append(indentation)
                    .append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue().getRight());
        }
        singleBotStat.append(lineJump).append("\t -Applied Weathers :");
        for (Map.Entry<String, MutablePair<Integer, Integer>> entry : weathers.entrySet()) {
            singleBotStat
                    .append(lineJump)
                    .append(indentation)
                    .append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue().getLeft());
        }
        singleBotStat.append(lineJump).append("\t -Average choice of action :");
        for (Map.Entry<String, Integer> entry : sortedActions.entrySet()) {
            singleBotStat
                    .append(lineJump)
                    .append(indentation)
                    .append(entry.getKey())
                    .append(" : ")
                    .append((Float.valueOf(entry.getValue()) / totalNbOfAction) * 100)
                    .append("%");
        }
        return singleBotStat.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleBotStatistics that = (SingleBotStatistics) o;
        return totalNbOfAction == that.totalNbOfAction
                && numericStats.equals(that.numericStats)
                && objectivesRedeemed.equals(that.objectivesRedeemed)
                && bambooCounter.equals(that.bambooCounter)
                && tilesPlaced.equals(that.tilesPlaced)
                && weathers.equals(that.weathers)
                && actions.equals(that.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                totalNbOfAction,
                numericStats,
                objectivesRedeemed,
                bambooCounter,
                tilesPlaced,
                weathers,
                actions);
    }

    public Map<String, Integer> getNumericStats() {
        return numericStats;
    }

    public int getTotalNbOfAction() {
        return totalNbOfAction;
    }

    public Map<ObjectiveType, Integer> getObjectivesRedeemed() {
        return objectivesRedeemed;
    }

    public Map<TileColor, MutablePair<Integer, Integer>> getBambooCounter() {
        return bambooCounter;
    }

    public Map<TileColor, Integer> getTilesPlaced() {
        return tilesPlaced;
    }

    public Map<String, MutablePair<Integer, Integer>> getWeathers() {
        return weathers;
    }

    public Map<String, Integer> getActions() {
        return actions;
    }

    public void addStatistics(SingleBotStatistics value) {
        for (Map.Entry<String, Integer> entry : value.getNumericStats().entrySet()) {
            numericStats.put(entry.getKey(), numericStats.get(entry.getKey()) + entry.getValue());
        }
        totalNbOfAction += value.getTotalNbOfAction();
    }

    public int getWins() {
        return numericStats.get(WINS);
    }

    public int getLosses() {
        return numericStats.get(LOSSES);
    }

    public int getFinalScore() {
        return numericStats.get(FINAL_SCORE);
    }
}
