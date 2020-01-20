package com.example.chess.model;


public class ChessFigure {
    private Colors color;
    private Figures name;
    private int column;
    private int row;
private boolean moved=false;

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color)
    {
        this.color = color;
    }


    public Figures getName()
    {
        return name;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setName(Figures name)
    {
        this.name = name;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
