package com.maksimovamaris.chess.game.figures;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;


import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;



public abstract class TestHelper {

    ChessFigure anyFigure;
    BoardDirector director;
    Cell initialPosition;
    List<Cell> correctPositions;

    @Before
    public void initialPrepare() {

        director = mock(BoardDirector.class);
        anyFigure = mock(ChessFigure.class);
        initialPosition = new Cell(1, 1);
        correctPositions = new ArrayList<>();
    }

    void testPosition(ChessFigure figure, List<Cell> correctPositions) {

        Collections.sort(correctPositions);
        List<Cell> expectedPositions = figure.getPossiblePositions(director);
        Collections.sort(expectedPositions);
        assertArrayEquals(correctPositions.toArray(), expectedPositions.toArray());
    }

    @Test
    public abstract void figureCantMove();

    @Test
    public abstract void figureCanEat();

    @Test
    public abstract void figureOnEmptyBoard();


}
