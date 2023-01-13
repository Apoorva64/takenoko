package com.takenoko.bot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.takenoko.actions.MoveGardenerAction;
import com.takenoko.engine.Board;
import com.takenoko.engine.BotState;
import com.takenoko.layers.tile.PlaceTileAction;
import com.takenoko.layers.tile.Tile;
import com.takenoko.vector.PositionVector;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TilePlacingAndGardenerMovingBotTest {
    private Board board;
    private TilePlacingAndGardenerMovingBot bot;

    @BeforeEach
    void setUp() {
        board = mock(Board.class);
        bot = new TilePlacingAndGardenerMovingBot();
    }

    @Nested
    @DisplayName("Method chooseAction()")
    class TestChooseAction {
        @Test
        @DisplayName("should return an action")
        void shouldReturnAnAction() {
            when(board.getGardenerPossibleMoves())
                    .thenReturn(List.of(new PositionVector(1, 0, -1)));
            assertThat(bot.chooseAction(board, mock(BotState.class))).isNotNull();
        }

        @Test
        @DisplayName(
                "should return an action of type MoveGardenerAction when the gardener is can move")
        void shouldReturnAnActionOfTypePlaceTileWhenTheGardenerIsCanMove() {
            when(board.getGardenerPossibleMoves())
                    .thenReturn(List.of(new PositionVector(1, 0, -1)));
            assertThat(bot.chooseAction(board, mock(BotState.class)))
                    .isInstanceOfAny(MoveGardenerAction.class);
        }

        @Test
        @DisplayName("should return an action of type PlaceTileAction when the gardener can't move")
        void shouldReturnAnActionOfTypeMoveGardenerWhenTheGardenerCantMove() {
            when(board.getGardenerPossibleMoves()).thenReturn(List.of());
            when(board.getAvailableTiles()).thenReturn(List.of(mock(Tile.class)));
            when(board.getAvailableTilePositions()).thenReturn(List.of(mock(PositionVector.class)));
            assertThat(bot.chooseAction(board, mock(BotState.class)))
                    .isInstanceOfAny(PlaceTileAction.class);
        }
    }
}