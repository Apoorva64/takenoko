package com.takenoko.bot.unitary;

import com.takenoko.actions.objective.DrawObjectiveAction;
import com.takenoko.actions.objective.RedeemObjectiveAction;
import com.takenoko.bot.PriorityBot;
import com.takenoko.bot.utils.HistoryAnalysis;
import com.takenoko.engine.Board;
import com.takenoko.engine.BotState;
import com.takenoko.engine.GameEngine;
import com.takenoko.engine.History;
import com.takenoko.objective.EmperorObjective;
import com.takenoko.objective.Objective;
import com.takenoko.objective.ObjectiveType;
import com.takenoko.objective.PandaObjective;
import java.util.List;

public class SmartObjective extends PriorityBot {
    private static final int ARBITRARY_MARGIN = 0;

    @Override
    protected void fillAction(Board board, BotState botState, History history) {
        this.addActionWithPriority(analyzeObjectivesToRedeem(botState, history), DEFAULT_PRIORITY);

        if (board.hasObjectiveTypeInDeck(ObjectiveType.PANDA)) {
            this.addActionWithPriority(
                    new DrawObjectiveAction(ObjectiveType.PANDA), DEFAULT_PRIORITY);
        }
        if (board.hasObjectiveTypeInDeck(ObjectiveType.GARDENER)) {
            this.addActionWithPriority(
                    new DrawObjectiveAction(ObjectiveType.GARDENER), DEFAULT_PRIORITY);
        }
        if (board.hasObjectiveTypeInDeck(ObjectiveType.SHAPE)) {
            this.addActionWithPriority(
                    new DrawObjectiveAction(ObjectiveType.SHAPE), DEFAULT_PRIORITY);
        }

        botState.getAchievedObjectives().stream()
                .filter(v -> v.getClass() != PandaObjective.class)
                .forEach(
                        v ->
                                this.addActionWithPriority(
                                        new RedeemObjectiveAction(v), DEFAULT_PRIORITY));
    }

    public RedeemObjectiveAction analyzeObjectivesToRedeem(BotState botState, History history) {
        /*
         * If we have only one panda objective, do not redeem it
         * If we have any other objective, redeem any of them
         */
        List<Objective> pandaObjectives =
                botState.getAchievedObjectives().stream()
                        .filter(PandaObjective.class::isInstance)
                        .toList();

        // If we have two PandaObjective we redeem one
        if (pandaObjectives.size() > 1) {
            return new RedeemObjectiveAction(pandaObjectives.get(0));
        }

        // If when we redeem an objective, and it does not make us win we don't redeem
        if (botState.getRedeemedObjectives().size() + 1
                < GameEngine.DEFAULT_NUMBER_OF_OBJECTIVES_TO_WIN.get(history.keySet().size())) {
            return null;
        }

        if (pandaObjectives.size() == 1
                && pandaObjectives.get(0).getPoints()
                                + botState.getObjectiveScore()
                                + EmperorObjective.EMPEROR_BONUS
                        > HistoryAnalysis.getMaxCurrentBotScore(history) + ARBITRARY_MARGIN) {
            return new RedeemObjectiveAction(pandaObjectives.get(0));
        }

        return null;
    }
}
