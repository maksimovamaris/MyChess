package com.maksimovamaris.chess.game.pieces;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Король. Сердце армии. Ходит на одну клетку во все стороны
 *
 * @author машуля
 */
public class King extends Piece {
private boolean moved;

    public King(Colors color, Cell c) {
        super(color, c);
        name = Figures.KING;
        moved=false;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public List<Cell> getPossiblePositions(BoardDirector boardDirector) {
        List correctPositions = new ArrayList<Cell>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Cell c = new Cell(position.getX() + i, position.getY() + j);
                if (c.isCorrect() && !boardDirector.sameColor(c, this.position))
                    correctPositions.add(c);
            }
        }
        return correctPositions;
    }

}
