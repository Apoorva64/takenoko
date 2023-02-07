package com.takenoko.bot.unitary;

import com.takenoko.bot.PriorityBot;
import com.takenoko.bot.utils.GardenerPathfinding;
import com.takenoko.engine.Board;
import com.takenoko.engine.BotState;
import com.takenoko.engine.History;

public class SmartGardener extends PriorityBot {

    @Override
    protected void fillAction(Board board, BotState botState, History history) {
        this.addActionWithPriority(
                new GardenerPathfinding(board, botState).getMoveGardenerAction(), DEFAULT_PRIORITY);
    }
}
