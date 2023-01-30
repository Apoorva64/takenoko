package com.takenoko.actions;

import com.takenoko.actions.annotations.ActionAnnotation;
import com.takenoko.actions.annotations.ActionType;
import com.takenoko.engine.Board;
import com.takenoko.engine.BotManager;
import java.util.List;

@ActionAnnotation(ActionType.DEFAULT)
public class DrawObjectiveAction implements Action {

    @Override
    public ActionResult execute(Board board, BotManager botManager) {
        board.drawObjective();
        botManager.addObjective(board.peekObjectiveDeck());
        botManager.displayMessage(
                botManager.getName() + " drew objective" + board.peekObjectiveDeck());
        return new ActionResult(List.of(DrawObjectiveAction.class), 0);
    }
}
