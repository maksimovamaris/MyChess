package com.maksimovamaris.chess.game.pieces;


import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;


public class DiagonalCells {
    private Cell initialPosition;

    DiagonalCells() {
        initialPosition = new Cell(1, 1);
    }

    List<Cell> getPositions() {
        List<Cell> pos = new LinkedList<>();
        for (int i = 0; i < 8; i++)
            if (i != 1)
                pos.add(new Cell(i, i));

        for (int i = 0; i < 3; i++)
            if (i != 1)
                pos.add(new Cell(2 - i, i));
        return pos;
    }

    void block(Piece anyFigure, BoardDirector director) {

        when(director.getFigure(new Cell(2, 2))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(2, 2), initialPosition)).thenReturn(true);

        when(director.getFigure(new Cell(0, 2))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(0, 2), initialPosition)).thenReturn(true);

        when(director.getFigure(new Cell(0, 0))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(0, 0), initialPosition)).thenReturn(true);

        when(director.getFigure(new Cell(2, 0))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(2, 0), initialPosition)).thenReturn(true);

    }

    void setEat(Piece anyFigure, BoardDirector director) {

        when(director.getFigure(new Cell(7, 7))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(7, 7), initialPosition)).thenReturn(false);

        when(director.getFigure(new Cell(0, 2))).thenReturn(anyFigure);
        when(director.sameColor(new Cell(0, 2), initialPosition)).thenReturn(false);
    }
}
