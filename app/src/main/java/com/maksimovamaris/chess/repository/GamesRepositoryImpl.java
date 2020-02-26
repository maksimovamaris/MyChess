package com.maksimovamaris.chess.repository;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

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
    public GameData addGame(Date date) {
        GameData gameData = new GameData();
        gameData.setGame_date(date);
        gamesDataBase.getGameDao().addGame(gameData);
        return gameData;
    }

    @WorkerThread
    public MoveData addMove(String figureName, Cell c0, Cell c1) {
        MoveData moveData = new MoveData();
        moveData.setFigire_name(figureName);
        moveData.setX0(c0.getX());
        moveData.setY0(c0.getY());
        moveData.setX1(c1.getX());
        moveData.setY1(c1.getY());
        gamesDataBase.getGameDao().addMove(moveData);
        return moveData;
    }
@WorkerThread
   public void deleteGame(Date date)
{
    GameData gameData=new GameData();
    gameData.setGame_date(date);
    gamesDataBase.getGameDao().deleteGame(gameData);
}

@WorkerThread
public List<MoveData> getGameNotation(Date date)
{
    return gamesDataBase.getGameDao().getMovesForGame(date);

}

}
