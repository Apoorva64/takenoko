package com.takenoko.actions.irrigation;

import com.takenoko.actions.Action;
import com.takenoko.actions.ActionResult;
import com.takenoko.actions.annotations.ActionAnnotation;
import com.takenoko.actions.annotations.ActionType;
import com.takenoko.engine.Board;
import com.takenoko.engine.BotManager;

@ActionAnnotation(ActionType.DEFAULT)
public class DrawIrrigationAction implements Action {

    @Override
    public ActionResult execute(Board board, BotManager botManager) {
        if (!board.hasIrrigation()) {
            botManager.displayMessage("No more irrigation in deck");
            return new ActionResult(0);
        }

        board.drawIrrigation();
        botManager.getInventory().collectIrrigationChannel();
        return new ActionResult(1);
    }
}
