package com.takenoko.weather;

import com.takenoko.actions.Action;
import com.takenoko.actions.improvement.DrawImprovementAction;
import com.takenoko.engine.Board;
import com.takenoko.engine.BotManager;
import java.util.List;

/** Weather that makes the player draw an improvement from the deck. */
public class Cloudy extends Weather {
    @Override
    public List<Class<? extends Action>> apply(Board board, BotManager botManager) {
        board.setWeather(this);
        return List.of(DrawImprovementAction.class);
    }

    @Override
    public void revert(Board board, BotManager botManager) {
        board.resetWeather();
    }

    @Override
    public String toString() {
        return "Cloudy";
    }
}
