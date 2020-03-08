package com.maksimovamaris.chess.game.figures;

public enum Colors {
    WHITE(1), BLACK(-1);
    private int i;
    Colors(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }
}
