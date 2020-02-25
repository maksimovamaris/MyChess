package com.maksimovamaris.chess.game.figures;

import org.junit.Before;


public class TestQueenBehavour extends TestHelper {

    private Queen testQueen;
    private DiagonalCells diagonalCells;
    private StraightCells straightCells;

    @Before
    public void prepareBoardDirector() {
        initialPrepare();
        diagonalCells = new DiagonalCells();
        straightCells = new StraightCells();
        testQueen = new Queen(Colors.WHITE, initialPosition);
        correctPositions = diagonalCells.getPositions();
        correctPositions.addAll(straightCells.getPositions());
    }

    @Override
    public void figureCantMove() {
        correctPositions.clear();
        diagonalCells.block(anyFigure, director);
        straightCells.block(anyFigure, director);
        testPosition(testQueen, correctPositions);
    }

    @Override
    public void figureCanEat() {

        diagonalCells.setEat(anyFigure, director);
        straightCells.setEat(anyFigure, director);
        testPosition(testQueen, correctPositions);
    }

    @Override
    public void figureOnEmptyBoard() {
        testPosition(testQueen, correctPositions);
    }
}
