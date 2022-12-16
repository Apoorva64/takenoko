package com.takenoko.objective;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.takenoko.engine.Board;
import com.takenoko.layers.LayerManager;
import com.takenoko.layers.tile.TileLayer;
import com.takenoko.tile.Tile;
import com.takenoko.vector.PositionVector;
import java.util.HashMap;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;

class TwoAdjacentTilesObjectiveTest {

    private TwoAdjacentTilesObjective twoAdjacentTilesObjective;
    private Board board;

    @BeforeEach
    void setUp() {
        twoAdjacentTilesObjective = new TwoAdjacentTilesObjective();
        board = new Board();
    }

    @AfterEach
    void tearDown() {
        twoAdjacentTilesObjective = null;
        board = null;
    }

    @Nested
    @DisplayName("Method verify")
    class TestVerify {
        @Test
        @DisplayName("When board has two tiles placed, state is ACHIEVED")
        void verify_WhenBoardHasTwoTilesNextToEachOther_ThenObjectiveStateIsACHIEVED() {
            HashMap<PositionVector, Tile> tiles = new HashMap<>();
            tiles.put(new PositionVector(0, -1, 1), new Tile());
            tiles.put(new PositionVector(1, -1, 0), new Tile());

            TileLayer tileLayer = mock(TileLayer.class);
            when(tileLayer.getTilesWithoutPond()).thenReturn(tiles);
            for (PositionVector position : tiles.keySet()) {
                when(tileLayer.isTile(position)).thenReturn(true);
            }

            LayerManager layerManager = mock(LayerManager.class);
            when(layerManager.getTileLayer()).thenReturn(tileLayer);
            board = mock(Board.class);
            when(board.getLayerManager()).thenReturn(layerManager);
            twoAdjacentTilesObjective.verify(board);
            assertEquals(ObjectiveState.ACHIEVED, twoAdjacentTilesObjective.getState());
        }
    }

    @Nested
    @DisplayName("Method isAchieved")
    class TestIsAchieved {
        @Test
        @DisplayName("When Objective is initialized return false")
        void isAchieved_WhenObjectiveIsInitialized_ThenReturnsFalse() {
            assertFalse(twoAdjacentTilesObjective.isAchieved());
        }

        @Test
        @DisplayName("When Objective is achieved return true")
        void isAchieved_WhenObjectiveIsAchieved_ThenReturnsTrue() {
            // use reflection to set the private field
            ReflectionTestUtils.setField(
                    twoAdjacentTilesObjective, "state", ObjectiveState.ACHIEVED);
            twoAdjacentTilesObjective.verify(board);
            assertThat(twoAdjacentTilesObjective.isAchieved()).isTrue();
        }
    }

    @Nested
    @DisplayName("Method toString")
    class TestToString {
        @Test
        @DisplayName("When Objective is initialized return correct string")
        void toString_WhenObjectiveIsInitialized_ThenReturnsCorrectString() {
            assertEquals(
                    "TwoAdjacentTilesObjective{state=NOT_ACHIEVED}",
                    twoAdjacentTilesObjective.toString());
        }
    }
}
