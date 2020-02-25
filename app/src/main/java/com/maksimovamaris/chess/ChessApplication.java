package com.maksimovamaris.chess;

import android.app.Application;

import com.maksimovamaris.chess.game.action.Game;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.utils.Runner;
import com.maksimovamaris.chess.utils.TaskRunner;

public class ChessApplication extends Application implements GameHolder {
    private Game game;
    private Runner runner;

    @Override
    public void onCreate() {
        super.onCreate();
        runner = new TaskRunner();
        game = new Game(runner);
    }

    @Override
    public Game getGame() {
        return game;
    }
}
