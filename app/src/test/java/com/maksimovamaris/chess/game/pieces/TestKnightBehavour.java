package com.maksimovamaris.chess.game.pieces;

import com.maksimovamaris.chess.game.action.Cell;


import org.junit.Before;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;


public class TestKnightBehavour extends TestHelper {
    private Piece testKnight;
    private List<Cell> correctPositions;


    //общая подготовка данных, выполняется перед каждым тестом
    @Before
    public void prepareBoardDirector() {
        initialPrepare();
        testKnight = new Knight(Colors.WHITE, initialPosition);
        correctPositions = new LinkedList<>();
        correctPositions.add(new Cell(3, 2));
        correctPositions.add(new Cell(3, 0));
        correctPositions.add(new Cell(2, 3));
        correctPositions.add(new Cell(0, 3));

    }

    @Override
    public void figureCantMove() {
        correctPositions.clear();
        when(director.getFigure(new Cell(3, 2))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(3, 2), initialPosition)).thenReturn(true);

        when(director.getFigure(new Cell(3, 0))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(3, 0), initialPosition)).thenReturn(true);

        when(director.getFigure(new Cell(2, 3))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(2, 3), initialPosition)).thenReturn(true);

        when(director.getFigure(new Cell(0, 3))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(0, 3), initialPosition)).thenReturn(true);
        testPosition(testKnight, correctPositions);

    }

    @Override
    public void figureCanEat() {
        when(director.getFigure(new Cell(0, 3))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(0, 3), initialPosition)).thenReturn(false);
        testPosition(testKnight, correctPositions);
    }

    @Override
    public void figureOnEmptyBoard() {
        testPosition(testKnight, correctPositions);
    }
}
