package com.maksimovamaris.chess.presenter;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.maksimovamaris.chess.data.GameData;
import com.maksimovamaris.chess.data.GamesDataBase;
import com.maksimovamaris.chess.data.GamesDatabaseHolder;
import com.maksimovamaris.chess.utils.Runner;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GamesRepository {
    private RepositoryListener listener;
    private LiveData<List<GameData>> games;
    private GamesDataBase gamesDataBase;
    private Runner dataWorker;

    public GamesRepository(RepositoryListener listener) {
        this.listener=listener;
    }

    public void loadFromDatabase(Context context) {
        dataWorker = ((GamesDatabaseHolder) (context.getApplicationContext())).getDataWorker();
        dataWorker.runInBackground(() -> {
            gamesDataBase = ((GamesDatabaseHolder) (context.getApplicationContext())).getGamesDatabase();
            gamesDataBase.getGameDao().addGame(new GameData(new Date(),"White"));
            games = gamesDataBase.getGameDao().getAllGames();
            Log.d("Hello!", "loadFromDatabase() called with: context = [" + games.getValue() + "]");
            dataWorker.runOnMain(() -> {
                listener.updateView(games);
            });
        });

    }

}
