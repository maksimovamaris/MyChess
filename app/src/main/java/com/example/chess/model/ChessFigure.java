package com.example.chess.model;


public class ChessFigure {
    private Colors color;
    private int row;
    private int column;
    private Figures name;

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Figures getName() {
        return name;
    }

    public void setName(Figures name) {
        this.name = name;
    }
}
