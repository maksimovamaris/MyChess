package com.maksimovamaris.chess.game.pieces;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;


import java.util.ArrayList;
import java.util.List;

/**
 * Пешка. Ходит только вперед по линии на 1 или 2 клетки, ест по диагонали
 *
 * @author машуля
 */
public class Pawn extends Piece {

    public Pawn(Colors color, Cell c) {
        super(color, c);
        name = Figures.PAWN;
    }

    @Override
    public List<Cell> getPossiblePositions(BoardDirector boardDirector) {

        List correctPositions = new ArrayList<Cell>();
        Cell c;
        int x = position.getX();
        int y = position.getY();
        int colorIndex = color.getI();
        //если доска перевернута, пешки "двигаются в обратную сторону"
        if (!boardDirector.isWhiteFront())
            colorIndex = colorIndex * (-1);
        //проверяем, может ли пешка сходить на 1 клетку вперед
        c = new Cell(x, y + colorIndex);
        if (c.isCorrect() && boardDirector.getFigure(c) == null) {
            correctPositions.add(c);
            //проверяем, может ли пешка сходить на 2 клетки вперед
            if ((y - colorIndex) % 7 == 0) {
                c = new Cell(x, y + 2 * colorIndex);
                if (boardDirector.getFigure(c) == null)
                    correctPositions.add(c);
            }
        }
        //проверяем, может ли пешка кушать
        c = new Cell(x - 1, y + colorIndex);
        if (c.isCorrect() && boardDirector.getFigure(c) != null && !boardDirector.sameColor(c, this.position))
            correctPositions.add(c);
        c = new Cell(x + 1, y + colorIndex);
        if (c.isCorrect() && boardDirector.getFigure(c) != null && !boardDirector.sameColor(c, this.position))
            correctPositions.add(c);

        return correctPositions;
    }

}
