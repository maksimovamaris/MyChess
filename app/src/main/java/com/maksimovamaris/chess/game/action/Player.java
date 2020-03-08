package com.maksimovamaris.chess.game.action;


import com.maksimovamaris.chess.game.figures.Colors;

public class Player {
    private Colors color;
    private boolean isBot;
    private boolean canMove = false;

    public Player(Colors color, boolean isBot) {
        this.color = color;
        this.isBot = isBot;
    }

    public boolean isBot() {
        return isBot;
    }

    public Colors getColor() {
        return color;
    }


    public void setBot(boolean bot) {
        isBot = bot;
    }

    public void setFlag_move(boolean flag_move) {
        this.canMove = flag_move;
    }
}
