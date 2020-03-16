package com.maksimovamaris.chess.game.pieces;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.game.action.Cell;

public class FigureInfo {

    private int kingWeight = 900;
    private double[][] kingScores = {
            {-3, -4, -4, -5, -5, -4, -4, -3},
            {-3, -4, -4, -5, -5, -4, -4, -3},
            {-3, -4, -4, -5, -5, -4, -4, -3},
            {-3, -4, -4, -5, -5, -4, -4, -3},
            {-2, -3, -3, -4, -4, -3, -3, -2},
            {-1, -2, -2, -2, -2, -2, -2, -1},
            {2, 2, 0, 0, 0, 0, 2, 2},
            {2, 3, 1, 0, 0, 1, 3, 2}};
    private int queenWeight = 90;
    private double[][] queenScores = {
            {-2, -1, -1, -0.5, -0.5, -1, -1, -2},
            {-1, 0, 0, 0, 0, 0, 0, -1},
            {-1, 0, 0.5, 0.5, 0.5, 0.5, 0, -1},
            {-0.5, 0, 0.5, 0.5, 0.5, 0.5, 0, -0.5},
            {0, 0, 0.5, 0.5, 0.5, 0.5, 0, -0.5},
            {-1, 0.5, 0.5, 0.5, 0.5, 0.5, 0, -1},
            {-1, 0, 0.5, 0, 0, 0, 0, -1},
            {-2, -1, -1, -0.5, -0.5, -1, -1, -2}};
    private int knightWeight = 30;
    private double[][] knightScores = {
            {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
            {-4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0},
            {-3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0},
            {-3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0},
            {-3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0},
            {-3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0},
            {-4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0},
            {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}};
    private int rookWeight = 50;
    private double[][] rookScores = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0}
    };
    private int bishopWeight = 30;
    private double[][] bishopScores = {
            {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 1, 1, 0.5, 0.0, -1.0},
            {-1, 0.5, 0.5, 1, 1, 0.5, 0.5, -1},
            {-1, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1},
            {-1.0, 1, 1, 1, 1, 1, 1, -1.0},
            {-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0},
            {-2.0, -1.0, -1.0, -1, -1, -1.0, -1.0, -2.0}

    };
    private int pawnWeight = 10;
    private double[][] pawnScores = {
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0},
            {1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 1.0, 1.0},
            {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
            {0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0},
            {0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
            {0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
    };

    public int getImageId(Piece figure) {

        switch (figure.name) {
            case KING:
                if (figure.color == Colors.BLACK)
                    return (R.drawable.kb);
                else
                    return (R.drawable.kw);
            case QUEEN:
                if (figure.color == Colors.BLACK)
                    return (R.drawable.qb);
                else
                    return (R.drawable.qw);

            case ROOK:
                if (figure.color == Colors.BLACK)
                    return (R.drawable.rb);
                else
                    return (R.drawable.rw);

            case BISHOP:
                if (figure.color == Colors.BLACK)
                    return (R.drawable.bb);
                else
                    return (R.drawable.bw);

            case KNIGHT:
                if (figure.color == Colors.BLACK)
                    return (R.drawable.nb);
                else
                    return (R.drawable.nw);

            case PAWN:
                if (figure.color == Colors.BLACK)
                    return (R.drawable.pb);
                else
                    return (R.drawable.pw);


        }
        return 0;
    }

    public Figures getName(Piece figure) {
        return figure.name;
    }

    public Colors getColor(Piece figure) {
        return figure.color;
    }

    public Cell getPosition(Piece figure) {
        return figure.position;
    }

    public void setPosition(Piece figure, Cell pos) {
        figure.position = pos;
    }

    public double[][] getScore(Piece figure) {
        switch (figure.name) {
            case KING:
                return kingScores;
            case KNIGHT:
                return knightScores;
            case ROOK:
                return rookScores;
            case PAWN:
                return pawnScores;
            case QUEEN:
                return queenScores;
            case BISHOP:
                return bishopScores;
            default:
                return pawnScores;
        }

    }

    public int getWeight(Piece figure) {
        switch (figure.name) {
            case KING:
                return kingWeight;
            case KNIGHT:
                return knightWeight;
            case ROOK:
                return rookWeight;
            case PAWN:
                return pawnWeight;
            case QUEEN:
                return queenWeight;
            case BISHOP:
                return bishopWeight;
            default:
                return 0;
        }

    }
}
