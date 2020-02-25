package com.maksimovamaris.chess.game.figures;


import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.game.action.Cell;

public class FigureInfo {

    public int getImageId(ChessFigure figure) {

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

    public Figures getName(ChessFigure figure) {
        return figure.name;
    }

    public Colors getColor(ChessFigure figure) {
        return figure.color;
    }

    public Cell getPosition(ChessFigure figure) {
        return figure.position;
    }

    public void setPosition(ChessFigure figure, Cell pos) {
        figure.position = pos;
    }
}
