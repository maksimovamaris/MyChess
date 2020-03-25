package com.maksimovamaris.chess.game.action;


import com.maksimovamaris.chess.game.pieces.Colors;

public class Player {
    Colors color;
    private boolean playerCanMove = false;

    public Player(Colors color) {
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    public void setFlag_move(boolean flag_move) {
        this.playerCanMove = flag_move;
    }
}
