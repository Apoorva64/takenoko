package com.takenoko.actions;

import com.takenoko.actions.annotations.ActionAnnotation;
import com.takenoko.actions.annotations.ActionType;
import com.takenoko.engine.Board;
import com.takenoko.engine.BotManager;

/** Action interface. An action is a command that can be executed by the Bot Manager. */
@ActionAnnotation(ActionType.DEFAULT)
public interface Action {

    /**
     * Executes the action on the board and the bot manager.
     *
     * @param board the board
     * @param botManager the bot manager
     * @return the action result
     */
    ActionResult execute(Board board, BotManager botManager);
}
