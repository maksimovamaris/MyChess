package com.maksimovamaris.chess.game.figures;
import com.maksimovamaris.chess.game.BoardDirector;
import com.maksimovamaris.chess.game.Cell;
import com.maksimovamaris.chess.game.Colors;

import java.util.List;

public abstract class ChessFigure {
    private Colors color;
    public Cell position;
public ChessFigure(Colors color,Cell c)
{
    this.position=c;
    this.color=color;
}

    public Colors getColor() {
        return color;
    }


    public abstract   Figures getName();

    public boolean sameColor(Cell c)
    {
        return ((BoardDirector.getFigure(c)!=null)&&(BoardDirector.getFigure(c).getColor()==color));
    }

    public abstract int  getImageId();

    public abstract List<Cell> getPossiblePositions();

}
