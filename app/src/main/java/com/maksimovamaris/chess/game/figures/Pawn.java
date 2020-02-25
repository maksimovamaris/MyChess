package com.maksimovamaris.chess.game.figures;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;


import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessFigure {


    public Pawn(Colors color, Cell c ) {
        super(color, c);
        name = Figures.PAWN;
    }

    @Override
    public List<Cell> getPossiblePositions(BoardDirector boardDirector) {
        List correctPositions = new ArrayList<Cell>();
            Cell c;
            int x = position.getX();
            int y = position.getY();

            //проверяем, может ли пешка сходить на 1 клетку вперед
            c = new Cell(x, y + color.getI());
            if (c.isCorrect() && boardDirector.getFigure(c) == null) {
                correctPositions.add(c);
                //проверяем, может ли пешка сходить на 2 клетки вперед
                if ((y - color.getI()) % 7 == 0) {
                    c = new Cell(x, y + 2 * color.getI());
                    if (boardDirector.getFigure(c) == null)
                        correctPositions.add(c);
                }
            }
            //проверяем, может ли пешка кушать
            c = new Cell(x - 1, y + color.getI());
            if (c.isCorrect() && boardDirector.getFigure(c) != null && !boardDirector.sameColor(c, this.position))
                correctPositions.add(c);
            c = new Cell(x + 1, y + color.getI());
            if (c.isCorrect() && boardDirector.getFigure(c) != null && !boardDirector.sameColor(c, this.position))
                correctPositions.add(c);

        return correctPositions;
    }

}
