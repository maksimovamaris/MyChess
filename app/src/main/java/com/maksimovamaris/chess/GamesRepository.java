package com.maksimovamaris.chess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GamesRepository {
    private List<Date> gameDate;

    public GamesRepository() {
        this.gameDate = new ArrayList<>();
        Date date=new Date();
        gameDate.add(date);

    }
    public List<Date> getGameDate()
    {
        return gameDate;
    }

//    private final int iconId=R.drawable.ic_launcher_background;

//    public int getIconId() {
//        return iconId;
//    }
}
