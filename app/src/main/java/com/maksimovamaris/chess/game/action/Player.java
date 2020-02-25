package com.maksimovamaris.chess.game.action;


import com.maksimovamaris.chess.game.figures.Colors;

public class Player {
    private Colors color;
    private String name;
    private boolean isBot;
    private boolean canMove = false;

    public Player(Colors color, String name, boolean isBot) {
        this.color = color;
        this.name = name;
        this.isBot = isBot;
    }

    public boolean isBot() {
        return isBot;
    }

    public Colors getColor() {
        return color;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setFlag_move(boolean flag_move) {
        this.canMove = flag_move;
    }
}
