package com.maksimovamaris.chess.game.action;

import com.maksimovamaris.chess.game.figures.*;

public class Board {
    ChessFigure[][] field;
    Board() {
        field = new ChessFigure[8][8];
    }
}
