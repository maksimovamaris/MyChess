package com.maksimovamaris.chess.game.action;

public interface FigureChoiceListener {
    void onChoiceStarted();
    void onChoiceMade(String figureName);
}
