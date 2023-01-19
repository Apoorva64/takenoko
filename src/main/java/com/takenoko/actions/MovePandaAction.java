package com.takenoko.actions;

import com.takenoko.engine.Board;
import com.takenoko.engine.BotManager;
import com.takenoko.layers.bamboo.BambooStack;
import com.takenoko.vector.PositionVector;

/** This class represents the action of moving the panda. */
public class MovePandaAction implements Action {
    private final PositionVector relativePositionVector;

    /**
     * Constructor for the MovePandaAction class.
     *
     * @param relativePositionVector the position vector to move the panda
     */
    public MovePandaAction(PositionVector relativePositionVector) {
        this.relativePositionVector = relativePositionVector;
    }

    /**
     * Move the panda with a vector on the board and display a message.
     *
     * @param board the board
     * @param botManager the bot manager
     * @return the action result
     */
    @Override
    public ActionResult execute(Board board, BotManager botManager) {
        // move the panda
        BambooStack bambooStack = board.movePanda(relativePositionVector);
        botManager.displayMessage(
                botManager.getName()
                        + " moved the panda with "
                        + relativePositionVector
                        + " to position "
                        + board.getPandaPosition());

        if (!bambooStack.isEmpty()) {
            // eat bamboo
            botManager.getInventory().collectBamboo();
            botManager.displayMessage(botManager.getName() + " collected one bamboo");
        }
        return new ActionResult(1);
    }

    @Override
    public ActionType getType() {
        return ActionType.DEFAULT;
    }
}
