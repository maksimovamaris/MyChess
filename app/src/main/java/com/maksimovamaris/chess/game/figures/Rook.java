package com.maksimovamaris.chess.game.figures;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;


import java.util.List;

public class Rook extends ChessFigure {

    public Rook(Colors color, Cell c) {
        super(color, c);
        name = Figures.ROOK;

    }

    @Override
    public List<Cell> getPossiblePositions(BoardDirector boardDirector) {
        StraightMovingHelper helper = new StraightMovingHelper(color, position);
        return (helper.moveStraight(this, boardDirector));
    }
}
