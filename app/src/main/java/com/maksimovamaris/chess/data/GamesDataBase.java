package com.maksimovamaris.chess.data;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {GameData.class,MoveData.class},version = 1,exportSchema = true)
@TypeConverters({DateConverter.class})
public abstract class GamesDataBase extends RoomDatabase {

public abstract GameDao getGameDao();

}
