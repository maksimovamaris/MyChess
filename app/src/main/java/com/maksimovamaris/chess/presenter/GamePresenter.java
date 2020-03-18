package com.maksimovamaris.chess.presenter;

import com.maksimovamaris.chess.game.pieces.Colors;

public class GamePresenter {
    private AddGameView addGameView;

    public void setGameView(AddGameView view) {
        addGameView = view;
    }

    private String setBot(boolean botWhite, boolean botBlack) {

        String botPlayer = "";
        if (botBlack) {
            botPlayer = Colors.BLACK.toString();

        } else if (botWhite) {
            botPlayer = Colors.WHITE.toString();
        }
        return botPlayer;
    }

    private String setHuman(String human) {
        String humanPlayer = "Unknown player";
        if (!human.equals(""))
            humanPlayer = human;
        return humanPlayer;
    }

    private String setGameName(String gameName) {
        if (!gameName.equals(""))
            return gameName;
        else
            return ("Untitled game");
    }

    public void sendResult(String gameName, String playerName, boolean botWhite, boolean botBlack) {
        if (gameName.length() > 10) {
            addGameView.onError("Game name");
        } else if (playerName.length() > 10) {
            addGameView.onError("Player name");
        } else
            addGameView.onGameAdded(setGameName(gameName),
                    setHuman(playerName),
                    setBot(botWhite, botBlack));
    }
}
