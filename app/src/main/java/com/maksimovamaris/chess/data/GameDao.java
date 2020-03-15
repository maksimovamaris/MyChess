package com.maksimovamaris.chess.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface GameDao {
    @Query("SELECT * FROM games")
    LiveData<List<GameData>> getAllGames();

    @Query("SELECT * FROM games where notation=:isNotation")
    LiveData<List<GameData>> getGameOrRecord(boolean isNotation);

    @Insert
    long addGame(GameData gameData);

    @Update
    void updateGame(GameData gameData);

    @Query("SELECT * FROM games where date=:gameDate")
    GameData getGameForDate(Date gameDate);

    @Query("DELETE FROM games WHERE date=:gameDate")
    void deleteGameByDate(long gameDate);

    @Query("SELECT * FROM moves WHERE game_date=:date")
    List<MoveData> getMovesForGame(long date);

    @Insert
    long addMove(MoveData moveData);
}
