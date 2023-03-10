package com.takenoko.actions;

import com.takenoko.actions.actors.MoveGardenerAction;
import com.takenoko.actions.actors.MovePandaAction;
import com.takenoko.actions.improvement.ApplyImprovementFromInventoryAction;
import com.takenoko.actions.irrigation.DrawIrrigationAction;
import com.takenoko.actions.irrigation.PlaceIrrigationFromInventoryAction;
import com.takenoko.actions.objective.DrawObjectiveAction;
import com.takenoko.actions.objective.RedeemObjectiveAction;
import com.takenoko.actions.tile.DrawTileAction;
import com.takenoko.engine.Board;
import com.takenoko.engine.BotState;

public interface DefaultAction extends Action {
    static boolean canBePlayed(
            Board board, BotState botState, Class<? extends DefaultAction> actionClass) {
        return switch (actionClass.getSimpleName()) {
            case "MovePandaAction" -> MovePandaAction.canBePlayed(board);
            case "MoveGardenerAction" -> MoveGardenerAction.canBePlayed(board);
            case "DrawIrrigationAction" -> DrawIrrigationAction.canBePlayed(board);
            case "DrawObjectiveAction" -> DrawObjectiveAction.canBePlayed(board, botState);
            case "RedeemObjectiveAction" -> RedeemObjectiveAction.canBePlayed(botState);
            case "DrawTileAction" -> DrawTileAction.canBePlayed(board);
            case "ApplyImprovementFromInventoryAction" -> ApplyImprovementFromInventoryAction
                    .canBePlayed(board, botState);
            case "PlaceIrrigationFromInventoryAction" -> PlaceIrrigationFromInventoryAction
                    .canBePlayed(board, botState);
            default -> throw new IllegalStateException(
                    "Unexpected value: " + actionClass.getSimpleName());
        };
    }
}
