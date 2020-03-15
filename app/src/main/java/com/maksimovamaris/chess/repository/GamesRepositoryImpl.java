package com.maksimovamaris.chess.repository;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.maksimovamaris.chess.data.DateConverter;
import com.maksimovamaris.chess.data.GameData;
import com.maksimovamaris.chess.data.GamesDataBase;
import com.maksimovamaris.chess.data.MoveData;
import com.maksimovamaris.chess.game.action.Cell;

import java.util.Date;
import java.util.List;

public class GamesRepositoryImpl implements GamesRepository {
    private GamesDataBase gamesDataBase;
    public GamesRepositoryImpl(GamesDataBase dataBase) {
        gamesDataBase = dataBase;
    }

    @Override
    @WorkerThread
    public LiveData<List<GameData>> loadFromDatabase() {
        return gamesDataBase.getGameDao().getAllGames();
    }

    @Override
    @WorkerThread
    public GameData addGame(Date date, String gameName, String player1Name, String player2Name) {
        GameData gameData = new GameData();
        gameData.setGame_date(date);
        gameData.setTurn("unknown");
        gameData.setNotation(false);
        gameData.setName(gameName);
        gameData.setHuman_player(player1Name);
        gameData.setBot_player(player2Name);
        gamesDataBase.getGameDao().addGame(gameData);
        return gameData;
    }

    @Override
    @WorkerThread
    public LiveData<List<GameData>> getGameOrRecord(boolean isRecord) {
        return gamesDataBase.getGameDao().getGameOrRecord(isRecord);
    }

    @Override
    @WorkerThread
    public MoveData addMove(String figureName, Cell c0, Cell c1, String savedFigureName,Date gameDate) {
        MoveData moveData = new MoveData();
        moveData.setFigireName(figureName);
        moveData.setX0(c0.getX());
        moveData.setY0(c0.getY());
        moveData.setX1(c1.getX());
        moveData.setY1(c1.getY());
        moveData.setNewFigureName(savedFigureName);
        moveData.setGameDate(gameDate);
        gamesDataBase.getGameDao().addMove(moveData);
        return moveData;
    }

    @Override
    @WorkerThread
    public void updateGame(GameData gameData) {
        gamesDataBase.getGameDao().updateGame(gameData);
    }

    @Override
    @WorkerThread
    public void deleteGameByDate(Date date) {
        gamesDataBase.getGameDao().deleteGameByDate(new DateConverter().fromDate(date));
    }

    @Override
    @WorkerThread
    public List<MoveData> getGameNotation(Date date) {
        List<MoveData> moveData = gamesDataBase.getGameDao().getMovesForGame(new DateConverter().fromDate(date));
        return moveData;
    }

    @Override
    @WorkerThread
    public GameData getGameByDate(Date date) {
        return gamesDataBase.getGameDao().getGameForDate(date);
    }
}
