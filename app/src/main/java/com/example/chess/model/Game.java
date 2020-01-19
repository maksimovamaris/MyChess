package com.example.chess.model;

import static com.example.chess.model.Figures.*;

public class Game {
    private ChessFigure[] figures;

    public boolean checkMotion() {
        return true;
    }

    public void startGame() {
        figures = new ChessFigure[32];
        createArmy(Colors.WHITE, 0);
        createArmy(Colors.BLACK, 16);
    }
    public void checkPosition(ChessFigure figure,int row,int column)
    {
        switch (figure.getName()){
            case KING:
//                if(figure.getName().getFlag()==false) {
//                    figure.getName().setFlag(true);
//                    if(row==figure.getRow())
//                    {}

//                }
                break;
            case QUEEN:
                break;
            case KNIGHT:
                break;
            case ROOK:
                break;
            case PAWN:
                break;
            case BISHOP:
                break;
        }
    }
    private boolean checkFreeLine(ChessFigure figure,int row,int column)
    {
//    int i;
//    for ( i=0; i < 32 &&
//            (((figures[i].getColumn()>column || figures[i].getColumn()<figure.getColumn())&&(figures[i].getRow()==row))||(figures[i].getRow()!=row));i++);
//
//       if (i==32)
        return true;

    }
    private boolean checkFreeDiagonal(ChessFigure figure,int row,int column)
    {
        return true;
    }
    private void createArmy(Colors color, int index) {
        //когда игра начинается с начала
        int row;
        if (color == Colors.WHITE)
            row = 1;
        else
            row = 8;

        for (Figures fig : values())
        {
            switch (fig) {
                case KING:
                    figures[index] = generateNewFigure(color, fig, row, 5);
                    break;

                case QUEEN:
                    figures[++index] = generateNewFigure(color, fig, row, 4);
                    break;

                case ROOK:
                    figures[++index] = generateNewFigure(color, fig, row,1 );
                    figures[++index] = generateNewFigure(color, fig, row, 8);
                    break;

                case BISHOP:
                    figures[++index] = generateNewFigure(color, fig, row, 3);
                    figures[++index] = generateNewFigure(color, fig, row, 6);
                    break;

                case KNIGHT:

                    figures[++index] = generateNewFigure(color, fig, row, 2);
                    figures[++index] = generateNewFigure(color, fig, row, 7);
                    break;

                case PAWN:

                    for (int i = 1; i < 9; i++) {
                        figures[index + i] = generateNewFigure(color, fig, row + color.getI(), i);
                    }
                    break;

            }
        }
    }

    private ChessFigure generateNewFigure(Colors color, Figures name, int row, int column)
    {
        ChessFigure fig = new ChessFigure();
        fig.setColor(color);
        fig.setName(name);
        fig.setColumn(column);
        fig.setRow(row);
        return fig;
    }

    public ChessFigure[] restoreGame() {
        return new ChessFigure[32];
//востановление неоконченной игры
    }
}
