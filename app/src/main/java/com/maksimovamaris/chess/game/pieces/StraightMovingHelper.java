package com.maksimovamaris.chess.game.pieces;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;

import java.util.ArrayList;
import java.util.List;

public class StraightMovingHelper extends Rook {

    public StraightMovingHelper(Colors color, Cell c) {
        super(color, c);
    }

    public List<Cell> moveStraight(Piece figure, BoardDirector boardDirector) {
        List<Cell> correctPositions = new ArrayList<>();

        //проверяет по вертикали
        for (int i = 1; i < 3; i++) {
            Cell c = new Cell(figure.position.getX(), figure.position.getY() + (int) Math.pow(-1, i));
            while (c.isCorrect() && boardDirector.getFigure(c) == null) {
                correctPositions.add(c);
                c = new Cell(c.getX(), c.getY() + (int) Math.pow(-1, i));
            }
            if (c.isCorrect())
                if (boardDirector.getFigure(c) != null && !boardDirector.sameColor(c, figure.position))
                    correctPositions.add(c);
        }

        //проверяет по горизонтали
        for (int i = 1; i < 3; i++) {
            Cell c = new Cell(figure.position.getX() + (int) Math.pow(-1, i), figure.position.getY());
            while (c.isCorrect() && boardDirector.getFigure(c) == null) {
                correctPositions.add(c);
                c = new Cell(c.getX() + (int) Math.pow(-1, i), c.getY());

            }
            if (c.isCorrect())
                if (boardDirector.getFigure(c) != null && !boardDirector.sameColor(c, figure.position))
                    correctPositions.add(c);

        }
        return correctPositions;
    }

}
