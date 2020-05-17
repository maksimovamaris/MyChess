package com.maksimovamaris.chess.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.game.pieces.Colors;

public class GamePresenter {
    private AddGameView addGameView;
    private BotView botView;
    private SharedPreferences preferences;
    private Context context;

    public GamePresenter(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getString(R.string.key_preferences),
                Context.MODE_PRIVATE);
    }

    public void setGameView(AddGameView view) {
        addGameView = view;
    }
    public void setBotView(BotView view) {
        botView = view;
    }


    public void setBotLevel(String level) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_key_botlevel), level);
        editor.apply();
        botView.onLevelAdded(level);

//        addGameView.onComplete("Bot level changed!");
//        botView.onLevelAdded;
    }

    public int getBotLevelArrayItemPos() {
        return getArrayIndex(preferences.getString(
                context.getString(R.string.pref_key_botlevel),
                context.getResources().getStringArray(R.array.bot_levels)[0]));
    }

    public int imageForBotLevel(String level) {
        if (getArrayIndex(level) == 0)
            return R.drawable.bot_random;
        else if (getArrayIndex(level) == 1)
            return R.drawable.bot_fast_and_stupid;
        else
            return R.drawable.bot_thoughtful;
    }

    public int getArrayIndex(String item) {
        if (item.equals(context.getResources().getStringArray(R.array.bot_levels)[0]))
            return 0;
        else if (item.equals(context.getResources().getStringArray(R.array.bot_levels)[1]))
            return 1;
        else return 2;
    }

    private String setBotColor(boolean botWhite, boolean botBlack) {

        String botPlayer = "";
        if (botBlack) {
            botPlayer = Colors.BLACK.toString();

        } else if (botWhite) {
            botPlayer = Colors.WHITE.toString();
        }
        return botPlayer;
    }

    public void setHumanName(String human) {
        SharedPreferences.Editor editor = preferences.edit();
        if (human.equals("")) {
            addGameView.onError(context.getResources().getString(R.string.error_player), context.getResources().getString(R.string.error_empty));
        } else if (human.length() > 10) {
            addGameView.onError("Player name", " > 10 signs");
        } else {
            editor.putString(context.getResources().getString(R.string.prefs_player_name), human);
            editor.apply();
            addGameView.onComplete(context.getResources().
                    getString(R.string.error_player) + " changed");
        }
    }

    public String getHumanName() {
        return (preferences.getString(context.getResources().getString(R.string.prefs_player_name)
                , "Unknown player"));

    }


    private String setGameName(String gameName) {
        if (!gameName.equals(""))
            return gameName;
        else
            return ("Untitled game");
    }

    public void sendResult(String gameName, String playerName, boolean botWhite, boolean botBlack) {
        if (gameName.length() > 10) {
            addGameView.onError("Game name", " > 10 signs");
        } else if (playerName.length() > 10) {
            addGameView.onError("Player name", " > 10 signs");
        } else
            addGameView.onGameAdded(setGameName(gameName),
                    getHumanName(),
                    setBotColor(botWhite, botBlack));
    }
}
