package com.maksimovamaris.chess;

import android.app.Application;
import android.app.Presentation;

import androidx.room.Room;

import com.maksimovamaris.chess.data.GamesDataBase;
import com.maksimovamaris.chess.game.action.Game;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.presenter.GamePresenter;
import com.maksimovamaris.chess.presenter.GamePresenterHolder;
import com.maksimovamaris.chess.repository.GamesRepositoryImpl;
import com.maksimovamaris.chess.repository.RepositoryHolder;
import com.maksimovamaris.chess.utils.Runner;
import com.maksimovamaris.chess.utils.TaskRunner;

public class ChessApplication extends Application implements GameHolder, RepositoryHolder, GamePresenterHolder {
    private Game game;
    private GamesDataBase gamesDataBase;
    private Runner runner;
    private GamesRepositoryImpl repository;
    private GamePresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        runner = new TaskRunner();
        game = new Game(runner);
        presenter=new GamePresenter();
        gamesDataBase = Room.databaseBuilder(this, GamesDataBase.class, "games_db")
                .build();
        repository = new GamesRepositoryImpl(gamesDataBase);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Runner getRunner() {
        return runner;
    }

    @Override
    public GamesRepositoryImpl getRepository() {
        return repository;
    }

    @Override
    public GamePresenter getGamePresenter() {
        return presenter;
    }
}
