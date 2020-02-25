package com.maksimovamaris.chess;

import android.app.Application;

import androidx.room.Room;

import com.maksimovamaris.chess.data.GamesDataBase;
import com.maksimovamaris.chess.data.GamesDatabaseHolder;
import com.maksimovamaris.chess.game.action.Game;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.utils.Runner;
import com.maksimovamaris.chess.utils.TaskRunner;

//thread policy!
public class ChessApplication extends Application implements GameHolder, GamesDatabaseHolder {
    private Game game;
    private GamesDataBase gamesDataBase;
    private Runner moveAnalyser;
    private Runner dataWorker;
    @Override
    public void onCreate() {
        super.onCreate();
        moveAnalyser = new TaskRunner();
        dataWorker =new TaskRunner();
        game = new Game(moveAnalyser);
        gamesDataBase = Room.databaseBuilder(this,GamesDataBase.class,"games_db")
                .build();
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public GamesDataBase getGamesDatabase() {
        return gamesDataBase;
    }

    @Override
    public Runner getDataWorker() {
        return dataWorker;
    }


}
