package com.maksimovamaris.chess.game.pieces;


import org.junit.Before;


public class TestBishopBehavour extends TestHelper {
    private Bishop testBishop;
    private DiagonalCells diagonalCells;


    @Before
    public void prepareBoardDirector() {
        initialPrepare();
        testBishop = new Bishop(Colors.WHITE, initialPosition);
        diagonalCells = new DiagonalCells();
        correctPositions = diagonalCells.getPositions();
    }

    @Override
    public void figureCantMove() {
        correctPositions.clear();
        diagonalCells.block(anyFigure, director);
        testPosition(testBishop, correctPositions);
    }

    @Override
    public void figureCanEat() {

        diagonalCells.setEat(anyFigure, director);
        testPosition(testBishop, correctPositions);
    }

    @Override
    public void figureOnEmptyBoard() {
        testPosition(testBishop, correctPositions);
    }
}
