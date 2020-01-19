package com.example.chess.model;

public enum Figures {

    KING(false,false),QUEEN(false,false),ROOK(false,false),BISHOP(false,false),KNIGHT(false,false),PAWN(false,false);
    private boolean kingMoved;
    private boolean curMove;
    Figures(boolean kingMoved, boolean curMove)
    {
        kingMoved=kingMoved;//нужен только королю
        curMove=curMove;
    }

    public boolean isKingMoved() {
        return kingMoved;
    }

    public void setKingMoved(boolean kingMoved) {
        this.kingMoved = kingMoved;
    }

    public boolean isCurMove() {
        return curMove;
    }

    public void setCurMove(boolean flagCurMove) {
        this.curMove = flagCurMove;
    }
}

