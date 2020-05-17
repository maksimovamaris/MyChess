package com.maksimovamaris.chess.game.action;


import android.content.Context;

import com.maksimovamaris.chess.game.pieces.Colors;

public class HumanPlayer {
    Colors color;
    protected Context context;
    private boolean playerCanMove = false;

    public HumanPlayer(Colors color, Context context) {
        this.color = color;
        this.context=context;
    }

    public Colors getColor() {
        return color;
    }

    public void setFlag_move(boolean flag_move) {
        this.playerCanMove = flag_move;
    }

}
