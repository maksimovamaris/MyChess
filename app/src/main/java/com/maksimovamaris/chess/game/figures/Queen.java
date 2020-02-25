package com.maksimovamaris.chess.game.figures;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;


import java.util.ArrayList;
import java.util.List;

public class Queen extends ChessFigure {


    public Queen(Colors color, Cell c ) {
        super(color, c);
        name = Figures.QUEEN;
    }

    @Override
    public List<Cell> getPossiblePositions(BoardDirector boardDirector) {
        StraightMovingHelper helperS = new StraightMovingHelper(color, position);
        DiagonalMovingHelper helperD = new DiagonalMovingHelper(color, position);
        List<Cell> correctPos;
        correctPos = helperS.moveStraight(this, boardDirector);
        correctPos.addAll(helperD.moveDiagonal(this, boardDirector));
        return (correctPos);

    }
}
