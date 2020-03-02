package com.maksimovamaris.chess.game.figures;

import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;


import java.util.LinkedList;
import java.util.List;

/**
 * Конь. Скачет буквой Г
 */
public class Knight extends ChessFigure {
    private List<Cell> possibleCells;


    public Knight(Colors color, Cell c) {
        super(color, c);
        name = Figures.KNIGHT;
    }

    @Override
    public List<Cell> getPossiblePositions(BoardDirector boardDirector) {
        possibleCells = new LinkedList();
        possibleCells.add(new Cell(position.getX() - 2, position.getY() - 1));
        possibleCells.add(new Cell(position.getX() - 2, position.getY() + 1));
        possibleCells.add(new Cell(position.getX() - 1, position.getY() - 2));
        possibleCells.add(new Cell(position.getX() - 1, position.getY() + 2));
        possibleCells.add(new Cell(position.getX() + 1, position.getY() - 2));
        possibleCells.add(new Cell(position.getX() + 1, position.getY() + 2));
        possibleCells.add(new Cell(position.getX() + 2, position.getY() - 1));
        possibleCells.add(new Cell(position.getX() + 2, position.getY() + 1));
        List<Cell> correctPositions = new LinkedList<>();
        for (Cell c : possibleCells) {
            if (c.isCorrect() && !boardDirector.sameColor(c, this.position))
                correctPositions.add(c);
        }

        return correctPositions;
    }
}
