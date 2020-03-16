package com.maksimovamaris.chess.game.pieces;


import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;

import java.util.ArrayList;

import java.util.List;

public class DiagonalMovingHelper extends Bishop {
    public DiagonalMovingHelper(Colors color, Cell c) {
        super(color, c);
    }

    public List<Cell> moveDiagonal(Piece figure, BoardDirector boardDirector) {
        List<Cell> correctPositions = new ArrayList<>();
        Cell[] cells = new Cell[2];
        cells[0] = new Cell(figure.position.getX() + 1, figure.position.getY() + 1);
        cells[1] = new Cell(figure.position.getX() - 1, figure.position.getY() + 1);
        //проверка свободного места наверху
        int i = 0;
        for (Cell c : cells) {
            while (c.isCorrect() && boardDirector.getFigure(c) == null) {
//не то прибавляется
                correctPositions.add(c);
                c = new Cell((int) (c.getX() + Math.pow((-1), i)), c.getY() + 1);

            }
            i++;
            if (c.isCorrect())
                if (boardDirector.getFigure(c) != null && !boardDirector.sameColor(c, figure.position))
                    correctPositions.add(c);
        }

        //проверка свободного места внизу
        cells[0] = new Cell(figure.position.getX() + 1, figure.position.getY() - 1);
        cells[1] = new Cell(figure.position.getX() - 1, figure.position.getY() - 1);
        for (Cell c : cells) {

            while (c.isCorrect() && boardDirector.getFigure(c) == null) {

                correctPositions.add(c);
                c = new Cell((int) (c.getX() + Math.pow(-1, i)), c.getY() - 1);

            }
            if (c.isCorrect())
                if (boardDirector.getFigure(c) != null && !boardDirector.sameColor(c, figure.position))
                    correctPositions.add(c);
            i++;
        }


        return correctPositions;
    }
}
