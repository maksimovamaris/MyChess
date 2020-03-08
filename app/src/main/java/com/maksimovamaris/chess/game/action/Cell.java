package com.maksimovamaris.chess.game.action;

public class Cell implements Comparable {
    private int x;
    private int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isCorrect() {
        return ((x < 8 && x >= 0) && (y < 8 && y >= 0));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Cell && ((Cell) other).x == x &&
                ((Cell) other).y == y;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Cell) {
            if (((Cell) o).getX() == x)
                return (Integer.compare(((Cell) o).getY(), y));
            else
                return (Integer.compare(((Cell) o).getX(), x));
        } else
            return 1;
    }

}
