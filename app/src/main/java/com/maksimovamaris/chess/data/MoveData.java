package com.maksimovamaris.chess.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.maksimovamaris.chess.game.action.Cell;

import java.util.Date;

@Entity(tableName = "moves", foreignKeys =
@ForeignKey(entity = GameData.class,
        parentColumns = "date",
        childColumns = "game_date",
        onDelete = ForeignKey.CASCADE))
public class MoveData implements Comparable{
    @PrimaryKey(autoGenerate = true)
    private long moveId;

    @ColumnInfo(name = "figure_name")
    public String figire_name;
    @ColumnInfo(name = "x0")
    private int x0;
    @ColumnInfo(name = "y0")
    private int y0;
    @ColumnInfo(name = "x1")
    private int x1;
    @ColumnInfo(name = "y1")
    private int y1;
    @ColumnInfo(name="saved figure")
    private String newFigureName;

    public String getNewFigureName() {
        return newFigureName;
    }

    public void setNewFigureName(String savedFigureName) {
        this.newFigureName = savedFigureName;
    }

    @ColumnInfo(name = "game_date")
    private Date gameDate;

    public long getMoveId() {
        return moveId;
    }

    public void setMoveId(long moveId) {
        this.moveId = moveId;
    }

    public String getFigire_name() {
        return figire_name;
    }

    public void setFigireName(String figire_name) {
        this.figire_name = figire_name;
    }

    public int getX0() {
        return x0;
    }

    public void setX0(int x0) {
        this.x0 = x0;
    }

    public int getY0() {
        return y0;
    }

    public void setY0(int y0) {
        this.y0 = y0;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    @Override
    public int compareTo(Object o) {
        Cell compareCell1= new Cell(this.getX0(),this.getY0());
        Cell compareCell2= new Cell(this.getX1(),this.getY1());
        return (compareCell1.compareTo(compareCell2));
    }
}
