package com.maksimovamaris.chess.game.pieces;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;


import java.util.List;

/**
 * Слон. ходит по диагонали
 * @see DiagonalMovingHelper
 * @author машуля
 */
public class Bishop extends Piece {
    private int image;


    public Bishop(Colors color, Cell c) {
        super(color, c);
        name = Figures.BISHOP;
    }

    @Override
    public List<Cell> getPossiblePositions(BoardDirector boardDirector) {
        DiagonalMovingHelper helper = new DiagonalMovingHelper(color, position);
        return (helper.moveDiagonal(this, boardDirector));
    }
}
