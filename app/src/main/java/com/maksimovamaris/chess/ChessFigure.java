package com.maksimovamaris.chess;


public class ChessFigure {
    private Colors color;
    private Figures name;

    private boolean moved = false;

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }


    public Figures getName() {
        return name;
    }


    public void setName(Figures name) {
        this.name = name;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
