package com.maksimovamaris.chess.game.pieces;

import com.maksimovamaris.chess.game.action.Cell;



import org.junit.Before;

import static org.mockito.Mockito.when;

public class TestRookBehavour extends TestHelper {

    private Rook testRook;
    private StraightCells straightCells;

    @Before
    public void prepareBoardDirector() {
        initialPrepare();
        testRook = new Rook(Colors.WHITE, initialPosition);
        straightCells = new StraightCells();
        correctPositions = straightCells.getPositions();
    }


    @Override
    public void figureCantMove() {
        correctPositions.clear();
        straightCells.block(anyFigure, director);
        testPosition(testRook, correctPositions);
    }

    @Override
    public void figureCanEat() {
        when(director.getFigure(new Cell(1, 7))).thenReturn(anyFigure);
        when(director.sameColor(initialPosition, new Cell(1, 7))).thenReturn(false);

        when(director.getFigure(new Cell(7, 1))).thenReturn(anyFigure);
        when(director.sameColor(initialPosition, new Cell(7, 1))).thenReturn(false);
        testPosition(testRook, correctPositions);
    }

    @Override
    public void figureOnEmptyBoard() {
        testPosition(testRook, correctPositions);

    }
}
