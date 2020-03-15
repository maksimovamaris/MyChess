package com.maksimovamaris.chess.game.action;
import android.content.Context;

public class GameHelper implements GameNotationListener {
    private Game game;
    private Context context;
    public GameHelper(Context context)
    {
        game=((GameHolder) (context.getApplicationContext())).getGame();
    }

    @Override
    public void modifyNotation(boolean notation) {
        game.setNotation(notation);
    }

}
