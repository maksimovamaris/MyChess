package com.maksimovamaris.chess.game.pieces;

import com.maksimovamaris.chess.game.action.Cell;


import org.junit.Before;

import static org.mockito.Mockito.when;

public class TestPawnBehavour extends TestHelper {
    Pawn testPawn;

    @Before
    public void prepareBoardDirector() {
        initialPrepare();
        initialPosition = new Cell(1, 1);
        testPawn = new Pawn(Colors.WHITE, initialPosition);
        correctPositions.add(new Cell(1, 2));
        correctPositions.add(new Cell(1, 3));
    }

    @Override
    public void figureCantMove() {
        when(director.getFigure(new Cell(1, 2))).thenReturn(anyFigure);
        correctPositions.clear();
        testPosition(testPawn, correctPositions);

    }

    @Override
    public void figureCanEat() {
        correctPositions.add(new Cell(0, 2));
        correctPositions.add(new Cell(2, 2));
        when(director.getFigure(new Cell(0, 2))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(0, 2), initialPosition)).thenReturn(false);
        when(director.getFigure(new Cell(2, 2))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(2, 2), initialPosition)).thenReturn(false);

        testPosition(testPawn, correctPositions);

    }

    @Override
    public void figureOnEmptyBoard() {
        testPosition(testPawn, correctPositions);

        initialPosition = new Cell(1, 2);
        new FigureInfo().setPosition(testPawn,initialPosition);
        when(director.getFigure(new Cell(0, 3))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(0, 3), initialPosition)).thenReturn(true);
        correctPositions.clear();
        correctPositions.add(new Cell(1, 3));

        testPosition(testPawn, correctPositions);

    }
}
