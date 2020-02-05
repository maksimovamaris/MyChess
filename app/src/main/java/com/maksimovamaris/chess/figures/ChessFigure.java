package com.maksimovamaris.chess.figures;


import com.maksimovamaris.chess.Board;
import com.maksimovamaris.chess.Cell;

import java.util.List;

public abstract class ChessFigure {
    private Colors color;
//    private Figures name;
    public Cell position;
public ChessFigure(Colors color,Cell c)
{
    this.position=c;
    this.color=color;
}

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color)
    {
        this.color = color;
    }


    public abstract   Figures getName();
//    {
//        return this.name;
//    }

    boolean sameColor(Cell c)
    {
        return ((Board.getFigure(c)!=null)&&(Board.getFigure(c).getColor()==color));
    }

    public abstract int  getImageId();
    public abstract List<Cell> getPossiblePositions();
//
//
//    public void setName(Figures name) {
//        this.name = name;
//    }

//    public boolean isMoved() {
//        return moved;
//    }

//    public void setMoved(boolean moved) {
//        this.moved = moved;
//    }
}
