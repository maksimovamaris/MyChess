package com.maksimovamaris.chess.game.pieces;
import com.maksimovamaris.chess.game.action.BoardDirector;
import com.maksimovamaris.chess.game.action.Cell;

import java.util.List;

/**
 * Класс-родитель всех шахматных фигур на доске
 * @author машуля
 */
public abstract class Piece {

    FigureInfo info;
    Colors color;
    Cell position;
    Figures name;

    public Piece(Colors color, Cell c) {
        info = new FigureInfo();
        this.position = c;
        this.color = color;

    }

    public abstract List<Cell> getPossiblePositions(BoardDirector boardDirector);

}
