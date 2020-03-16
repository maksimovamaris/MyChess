package com.maksimovamaris.chess.game.pieces;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;


import java.util.List;

/**
 * Ладья. Ходит по прямым
 * @see StraightMovingHelper
 * @author машуля
 */
public class Rook extends Piece {
private boolean moved;
    public Rook(Colors color, Cell c) {
        super(color, c);
        name = Figures.ROOK;
        moved=false;

    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public List<Cell> getPossiblePositions(BoardDirector boardDirector) {
        StraightMovingHelper helper = new StraightMovingHelper(color, position);
        return (helper.moveStraight(this, boardDirector));
    }
}
