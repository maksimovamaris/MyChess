package com.maksimovamaris.chess.game.figures;

import com.maksimovamaris.chess.game.action.Cell;


import org.junit.Before;

import static org.mockito.Mockito.when;

public class TestKingBehavour extends TestHelper {
    private King testKing;
    private DiagonalCells diagonalCells;
    private StraightCells straightCells;
    @Before
    public void prepareBoardDirector()
    {
        initialPrepare();
        when(director.getFigure(initialPosition)).thenReturn(testKing);
        when(director.sameColor(new Cell(1,1),initialPosition)).thenReturn(true);
        testKing=new King(Colors.WHITE,initialPosition);
        diagonalCells=new DiagonalCells();
        straightCells=new StraightCells();
        correctPositions.add(new Cell(0,1));
        correctPositions.add(new Cell(0,0));
        correctPositions.add(new Cell(0,2));
        correctPositions.add(new Cell(1,2));
        correctPositions.add(new Cell(2,2));
        correctPositions.add(new Cell(2,1));
        correctPositions.add(new Cell(2,0));
        correctPositions.add(new Cell(1,0));

    }
    @Override
    public void figureCantMove() {
        correctPositions.clear();
        diagonalCells.block(anyFigure,director);
        straightCells.block(anyFigure,director);
        testPosition(testKing,correctPositions);
    }

    @Override
    public void figureCanEat() {
when(director.getFigure((new Cell(0,0)))).thenReturn(anyFigure);
when(director.sameColor(new Cell(0,0),initialPosition)).thenReturn(false);
testPosition(testKing,correctPositions);
    }

    @Override
    public void figureOnEmptyBoard() {
testPosition(testKing,correctPositions);
    }
}
