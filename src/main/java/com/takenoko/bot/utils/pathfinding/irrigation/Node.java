package com.takenoko.bot.utils.pathfinding.irrigation;

import com.takenoko.engine.Board;
import com.takenoko.layers.irrigation.EdgePosition;

record Node(Board board, int gCost, int hCost, Node parent, EdgePosition edgePosition) {
    int fCost() {
        return gCost + hCost;
    }
}
