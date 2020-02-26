package com.maksimovamaris.chess.game.action;

import android.content.Context;
import android.util.Log;

import com.maksimovamaris.chess.game.figures.*;
import com.maksimovamaris.chess.repository.GamesRepositoryImpl;
import com.maksimovamaris.chess.repository.RepositoryHolder;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoardDirector {
    private Board board;
    private GamesRepositoryImpl repository;
    private Date date;
    Cell whiteKingPos;
    Cell blackKingPos;
    private static String TAG="In TestGameBehavour";

    public BoardDirector(Context context) {

        board = new Board();
        date = new Date();
        try {
            repository = ((RepositoryHolder) (context.getApplicationContext())).getRepository();
        } catch (NullPointerException e) {
            Log.d(TAG, "BoardDirector() called with: context = [" + context + "]");
        }
    }

    /**
     * Расставляет фигуры в первоначальное положение
     */

    public void startGame() {
        whiteKingPos = new Cell(4, 0);
        blackKingPos = new Cell(4, 7);

        //когда игра начинается с начала, фигуры встают в исходные позиции
        for (Colors color : Colors.values()) {

            int y;
            if (color == Colors.WHITE)
                y = 0;
            else
                y = 7;

            for (Figures fig : Figures.values()) {
                switch (fig) {

                    case KING:
                        board.field[4][y] = new King(color, new Cell(4, y));
                        break;

                    case QUEEN:
                        board.field[3][y] = new Queen(color, new Cell(3, y));
                        break;

                    case ROOK:
                        board.field[0][y] = new Rook(color, new Cell(0, y));
                        board.field[7][y] = new Rook(color, new Cell(7, y));
                        break;

                    case BISHOP:
                        board.field[2][y] = new Bishop(color, new Cell(2, y));
                        board.field[5][y] = new Bishop(color, new Cell(5, y));
                        break;

                    case KNIGHT:

                        board.field[1][y] = new Knight(color, new Cell(1, y));
                        board.field[6][y] = new Knight(color, new Cell(6, y));
                        break;
                    case PAWN:

                        for (int i = 0; i < 8; i++) {
                            board.field[i][y + color.getI()] = new Pawn(color, new Cell(i, y + color.getI()));
                        }
                        break;

                }
            }
        }
    }

    /**
     * @return возвращает фигуры, оставшиеся на доске
     */
    List<ChessFigure> getArmy() {
        List<ChessFigure> army = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                //если ячейка не пуста и это не король
                if ((board.field[i][j] != null) && !(board.field[i][j] instanceof King)) {
                    army.add(board.field[i][j]);
                }
        return army;
    }

    public ChessFigure getFigure(Cell c) {
        return board.field[c.getX()][c.getY()];
    }

    public void restoreGame() {

    }

    /**
     * процедура обновляет доску
     *
     * @param c0          обнуляется
     * @param c1          сюда становится фигура, занимавшая позицию с0
     * @param savedFigure что нужно поставить на месте того, что стало null (если это нужно)
     */
    public void updateBoard(Cell c0, Cell c1, ChessFigure savedFigure) {
        if (this.getFigure(c0) instanceof King) {
            if (new FigureInfo().getColor(this.getFigure(c0)) == Colors.BLACK)
                blackKingPos = c1;
            else
                whiteKingPos = c1;
        }
        new FigureInfo().setPosition(board.field[c0.getX()][c0.getY()], c1);
        board.field[c1.getX()][c1.getY()] = board.field[c0.getX()][c0.getY()];
        board.field[c0.getX()][c0.getY()] = savedFigure;

    }

    //c1- проверяемая ячейка, с2 - позиция фигуры для которой проверяем цвет
    public boolean sameColor(Cell c1, Cell c2) {
        return ((this.getFigure(c1) != null) && (new FigureInfo().getColor(this.getFigure(c1)) == new FigureInfo().getColor(this.getFigure(c2))));
    }

    /**
     * для тестирования
     *
     * @param b доска с нужной позицией
     */
    public void setBoard(Board b, Cell whiteKingPos, Cell blackKingPos) {
        board.field = b.field;
        this.whiteKingPos = whiteKingPos;
        this.blackKingPos = blackKingPos;
    }

    /**
     * Записывает игру в базу данных, начиная с первого хода
     */
    void firstWrite() {
        try {
            repository.addGame(date);
        } catch (NullPointerException e) {//для тестов
            Log.d(TAG, "firstWrite() called");
        }
    }

    /**
     * Записывает ход в таблицу moves
     * @param figureName имя фигуры
     * @param c0 откуда пошла
     * @param c1 куда пошла
     */
    void writeMove(String figureName, Cell c0, Cell c1) {
        try {
            repository.addMove(figureName, c0, c1);
        }
        catch (NullPointerException e)
        {
            Log.d(TAG, "writeMove() called with: figureName = [" + figureName + "], c0 = [" + c0 + "], c1 = [" + c1 + "]");
        }
    }

    void cleanGame()
    {
        try{
        repository.deleteGame(date);}
        catch (NullPointerException e)
        {
            Log.d(TAG, "cleanGame() called");
        }
    }
}
