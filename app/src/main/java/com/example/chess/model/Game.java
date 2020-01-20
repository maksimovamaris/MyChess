package com.example.chess.model;


import static com.example.chess.model.Figures.KNIGHT;
import static com.example.chess.model.Figures.values;

public class Game {

    private ChessFigure[][] mBoard;
    private ChessFigure[] army;

    public void startGame() {
        mBoard = new ChessFigure[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                mBoard[i][j] = null;
            }
        createArmy();
    }

    //помнить что нумерация в матрице происходит от 0 до 7, а не от 1 до 8, как в шахматах!
    //иниацилизация матрицы в конструкторе

    private void createBoard() {
        //когда игра начинается с начала фигуры встают в исходные позиции
        for (Colors color : Colors.values()) {

            int row;
            if (color == Colors.WHITE)
                row = 0;
            else
                row = 7;

            for (Figures fig : values()) {
                switch (fig) {
                    case KING:
                        mBoard[row][4] = generateNewFigure(color, fig);
                        break;

                    case QUEEN:
                        mBoard[row][3] = generateNewFigure(color, fig);
                        break;

                    case ROOK:
                        mBoard[row][0] = generateNewFigure(color, fig);
                        mBoard[row][7] = generateNewFigure(color, fig);
                        break;

                    case BISHOP:
                        mBoard[row][2] = generateNewFigure(color, fig);
                        mBoard[row][5] = generateNewFigure(color, fig);
                        break;

                    case KNIGHT:

                        mBoard[row][1] = generateNewFigure(color, fig);
                        mBoard[row][6] = generateNewFigure(color, fig);
                        break;

                    case PAWN:

                        for (int i = 0; i < 8; i++) {
                            mBoard[row + color.getI()][i] = generateNewFigure(color, fig);
                        }
                        break;

                }
            }

        }


    }

    private void createArmy() {
        int index = 0;
        int row;

//        for ()
        for (Colors color : Colors.values()) {
            if (color == Colors.WHITE)
                row = 0;
            else
                row = 6;

            for (int i = row; i < row + 2; i++)
                for (int j = 0; j < 8; j++) {
                    army[index] = mBoard[i][j];
                    army[index].setRow(i);
                    army[index].setColumn(j);
                    index++;
                }
        }
    }

    private ChessFigure generateNewFigure(Colors color, Figures name) {
        ChessFigure fig = new ChessFigure();
        fig.setColor(color);
        fig.setName(name);
        return fig;
    }


    public boolean checkPosition(int row, int column, int rowTo, int columnTo) {
        boolean flag = false;
        ChessFigure figure = mBoard[row][column];
        ChessFigure move = mBoard[rowTo][columnTo];
        if(rowTo>0&&columnTo>0&&rowTo<8&&columnTo<8)
        {
            switch (figure.getName()) {
                case KING:

                    break;
                case QUEEN:
                    flag = ((checkFreeLine(row, column, rowTo, columnTo) || checkFreeDiagonal(row, column, rowTo, columnTo))) && checkMovePoint(row, rowTo, column, columnTo);
                    break;
                case KNIGHT:
                    flag = ((Math.abs(rowTo - row) == 1 && Math.abs(columnTo - column) == 2) || (Math.abs(rowTo - row) == 2 && Math.abs(columnTo - column) == 1)) &&
                            checkMovePoint(row, rowTo, column, columnTo);
                    break;
                case ROOK:
                    flag = checkFreeLine(row, rowTo, column, columnTo) && checkMovePoint(row, rowTo, column, columnTo);
                    break;
                case PAWN:
                    //пешки не ходят назад, поэтому рассматриваем только случаи вперед
                    if (columnTo == column) // если движемся в той же колонке
                    {
                        if ((rowTo - row) == 2)//если прыгаем через 2 клетки

                        {
                            flag = (!mBoard[row][column].isMoved() && (move == null));//проверяем что пешка не ходила и поле свободно
                            if (flag)
                                mBoard[row][column].setMoved(true);
                            break;

                        } else if ((rowTo - row) == 1)//на 1 клетку вперед
                        {
                            flag = (move == null);//проверяем что поле свободно
                            if (flag)
                                mBoard[row][column].setMoved(true);
                            break;
                        }


                    } else {
                        if (Math.abs(columnTo - column) == 1)//если надо съесть фигуру
                        {
                            if (move != null)//проверяем что едим не пустое поле
                            {
                                flag = (move.getColor() != figure.getColor());//проверяем что едим не свою фигуру
                                if (flag)
                                    mBoard[row][column].setMoved(true);
                                break;
                            }
                        }

                    }
                    break;
                case BISHOP:
                    flag = checkFreeDiagonal(row, column, rowTo, columnTo) && checkMovePoint(row, rowTo, column, columnTo);
                    break;
                default:
                    flag = false;
                    break;
            }
        }
        return flag;
    }

    private boolean checkFreeLine(int row, int column, int rowTo, int columnTo) {
        boolean flag=false;
        if (row == rowTo )
        {
            int i;
for(i= Math.min(column,columnTo)+1;i<Math.max(column,columnTo)&&mBoard[row][i]==null;i++);
            if(i==Math.max(column,columnTo))
                flag=true;
        }
        else
                if(column==columnTo)
            {
                int i;
                for(i= Math.min(row,rowTo)+1;i<Math.max(row,rowTo)&&mBoard[i][column]==null;i++);
                if(i==Math.max(row,rowTo))
                    flag=true;

            }

        return flag;
        }

    private boolean checkFreeDiagonal(int row, int column, int rowTo, int columnTo) {
        boolean flag = false;

        if (Math.abs(row - rowTo) == Math.abs(column - columnTo)) {
            int i;
            int col = Math.min(column, columnTo) + 1;
            for (i = Math.min(row, rowTo) + 1; i < Math.max(rowTo, row) && mBoard[i][col++] == null; i++)
                ;
            if (i == Math.max(row, rowTo)) {
                if (mBoard[rowTo][columnTo] == null)
                    flag = true;
                else
                    flag = mBoard[rowTo][columnTo].getColor() != mBoard[row][column].getColor();
            }
        }
        return flag;
    }
    private boolean checkMovePoint(int row, int column, int rowTo, int columnTo)
    {
        boolean flag;
            if (mBoard[rowTo][columnTo] == null)
                flag = true;
            else
                flag = mBoard[rowTo][columnTo].getColor() != mBoard[row][column].getColor();

             return flag;
}
}



