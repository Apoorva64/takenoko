package com.takenoko;

import com.takenoko.vector.Vector;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class Bot implements Playable {
    private final Objective objective;

    /**
     * Default constructor for the Bot class.
     */
    public Bot() {
        objective = new PlaceTileObjective(1);
    }

    /**
     * This method get the objectif of the bot.
     *
     * @return The main objective.
     */
    public Objective getObjective() {
        return objective;
    }

    /**
     * This method return the chosen tile to place on the board.
     *
     * @param availableTiles The list of available tiles to place.
     * @param availableTilePositions The list of available tile positions.
     * @return A pair containing the position and the tile to place.
     */
    @Override
    public Pair<Vector, Tile> chooseTileToPlace(
            List<Tile> availableTiles, List<Vector> availableTilePositions) {
        if (availableTiles.isEmpty()) {
            throw new IllegalArgumentException("No possible tiles");
        }
        return Pair.of(availableTilePositions.get(0), availableTiles.get(0));
    }
}
