package com.maksimovamaris.chess.game.action;

public interface FigureChoiceListener {
    void onChoiceStarted(Cell selected,Cell moved);
    void onChoiceMade(String figureName);
}
