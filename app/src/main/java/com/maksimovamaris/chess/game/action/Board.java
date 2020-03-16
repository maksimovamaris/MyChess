package com.maksimovamaris.chess.game.action;

import com.maksimovamaris.chess.game.pieces.*;

public class Board {
    Piece[][] field;
    Board() {
        field = new Piece[8][8];
    }
}
