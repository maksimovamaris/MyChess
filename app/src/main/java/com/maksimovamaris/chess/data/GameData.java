package com.maksimovamaris.chess.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "games")
public class GameData {
    @PrimaryKey
    @ColumnInfo(name = "date")
    private Date game_date;

    private String turn;
    @ColumnInfo(name = "notation")
    private boolean isNotation;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name="human_player")
    private String human_player;

    @ColumnInfo(name="bot_player")
    private String bot_player;

//    @ColumnInfo(name="bot_level")
//    private String bot_level;
//
//    public String getBot_level() {
//        return bot_level;
//    }
//
//    public void setBot_level(String bot_level) {
//        this.bot_level = bot_level;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHuman_player() {
        return human_player;
    }

    public void setHuman_player(String human_player) {
        this.human_player = human_player;
    }

    public String getBot_player() {
        return bot_player;
    }

    public void setBot_player(String bot_player) {
        this.bot_player = bot_player;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public boolean isNotation() {
        return isNotation;
    }

    public void setNotation(boolean isNotation) {
        this.isNotation = isNotation;
    }

    public Date getGame_date() {
        return game_date;
    }

    public void setGame_date(Date game_date) {
        this.game_date = game_date;
    }
}
