package com.maksimovamaris.chess.presenter;

public interface AddGameView {
    void onGameAdded(String gameName, String human, String bot);
    void onError(String name);
}
