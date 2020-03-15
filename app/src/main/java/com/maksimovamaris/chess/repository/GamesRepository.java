package com.maksimovamaris.chess.repository;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.maksimovamaris.chess.data.GameData;
import com.maksimovamaris.chess.data.MoveData;
import com.maksimovamaris.chess.game.action.Cell;

import java.util.Date;
import java.util.List;

public interface GamesRepository {
    @WorkerThread
    LiveData<List<GameData>> loadFromDatabase();

    @WorkerThread
    GameData addGame(Date date, String name, String payer1, String player2);

    MoveData addMove(String figureName, Cell c0, Cell c1, String savedFigureName, Date gameDate);

    @WorkerThread
    LiveData<List<GameData>> getGameOrRecord(boolean isRecord);

    @WorkerThread
    void updateGame(GameData gameData);

    @WorkerThread
    void deleteGameByDate(Date date);

    @WorkerThread
    List<MoveData> getGameNotation(Date date);

    @WorkerThread
    GameData getGameByDate(Date date);

}
