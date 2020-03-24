package com.maksimovamaris.chess.game.action;

import android.content.Context;

import android.util.Log;

import com.maksimovamaris.chess.data.GameData;
import com.maksimovamaris.chess.data.MoveData;
import com.maksimovamaris.chess.game.pieces.*;
import com.maksimovamaris.chess.repository.GamesRepositoryImpl;
import com.maksimovamaris.chess.repository.RepositoryHolder;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author машуля
 * класс-посредник между
 * @see com.maksimovamaris.chess.repository.GamesRepositoryImpl и
 * @see Game
 * хранит в себе доску - матрицу 8х8 с фигурами
 * @see Board
 */
public class BoardDirector {
    private Board board;
    private GamesRepositoryImpl repository;
    private Date date;
    Map<Colors, Piece> kings;
    private boolean whiteFront;
    private static String TAG = "In TestGameBehavour";
    String botPlayer;
    String gameName;
    String humanPlayer;
    double score;
    private int longRookX;
    private int shortRookX;
    private int longRookX1;
    private int shortRookX1;
    private int longKingX;
    private int shortKingX;


    public BoardDirector(Context context) {
        longRookX = 0;
        longRookX1 = 3;
        shortRookX = 7;
        shortRookX1 = 5;
        longKingX = 2;
        shortKingX = 6;
        board = new Board();
        date = new Date();
        //по умолчанию перед игроком белые фигуры
        whiteFront = true;
        try {
            repository = ((RepositoryHolder) (context.getApplicationContext())).getRepository();
        } catch (NullPointerException e) {
            Log.d(TAG, "BoardDirector() called with: context = [" + context + "]");
        }
    }

    public boolean isWhiteFront() {
        return whiteFront;
    }

    public void setBotPlayer(String botPlayer) {
        this.botPlayer = botPlayer;
        //если бот играет белыми, поворачиваем доску черными фигурами к игроку
        if (botPlayer.equals(Colors.WHITE.toString())) {
            whiteFront = false;
            rotateBoard();
        }
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setHumanPlayer(String humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Расставляет фигуры в первоначальное положение
     */
    public void startGame() {

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
        countScores();

        King whiteKing = (King) (board.field[4][0]);
        King blackKing = (King) (board.field[4][7]);
        kings = new HashMap<>();

        kings.put(Colors.WHITE, whiteKing);
        kings.put(Colors.BLACK, blackKing);
    }

    void rotateBoard() {
        shortRookX = 7 - shortRookX;
        shortRookX1 = 7 - shortRookX1;
        longRookX = 7 - longRookX;
        longRookX1 = 7 - longRookX1;
        longKingX = 7 - longKingX;
        shortKingX = 7 - shortKingX;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 4; j++) {
                if (board.field[i][j] != null) {
                    Piece savedFigure = board.field[7 - i][7 - j];
                    updateBoard(new Cell(i, j), new Cell(7 - i, 7 - j), null);
                    restoreFigure(savedFigure, new Cell(i, j));
                    new FigureInfo().setPosition(savedFigure, new Cell(i, j));
                }

            }
    }

    /**
     * @return возвращает фигуры, оставшиеся на доске
     */
    List<Piece> getArmy() {
        List<Piece> army = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                //если ячейка не пуста и это не король
                if ((board.field[i][j] != null) && !(board.field[i][j] instanceof King)) {
                    army.add(board.field[i][j]);
                }
        return army;
    }

    public Piece getFigure(Cell c) {
        return board.field[c.getX()][c.getY()];
    }

    /**
     * @param date дата игры, которую хотим восстановить
     * @return возвращает позицию на доске, восстановленную по ходам
     */
    public int restoreGame(Date date) {
        //подготавливаем нужные ходы
        List<MoveData> moveData = repository.getGameNotation(date);
        GameData gameData = repository.getGameByDate(date);
        this.setGameName(gameData.getName());
        this.humanPlayer = gameData.getHuman_player();
        this.botPlayer = gameData.getBot_player();

        if (botPlayer.equals(Colors.WHITE.toString())) {
            whiteFront = false;

            rotateBoard();

        }

        //начинаем с чистой доски с начальными позициями фигур
        //(она у нас уже есть)
        //ставим ту же дату, что была в восстановленной игре
        this.date = date;
        for (MoveData d : moveData) {
            Cell c0 = new Cell(d.getX0(), d.getY0());
            Cell c1 = new Cell(d.getX1(), d.getY1());
            Piece newFigure;
            //если произошло превращение пешки
            //смотрим в какую фигуру она превратилась

            newFigure = pawnTurning(d.getNewFigureName(),
                    new FigureInfo().getColor(getFigure(c0)), c1);
            if (d.figure_name.equals(Figures.KING.toString()) && Math.abs(d.getX0() - d.getX1()) > 1) {
                updateCastling(c0, c1);
            } else
                updateBoard(c0, c1, newFigure);
            updateMoves(c1);
        }
        return moveData.size();
    }

    /**
     * возвращает фигуру в которую превратится пешка
     *
     * @param newFigure имя новой фигуры
     * @param color     цвет новой фигуры
     * @param c1        ячейка для новой фигуры
     * @return
     */
    Piece pawnTurning(String newFigure, Colors color, Cell c1) {

        if (newFigure.equals(Figures.KNIGHT.toString()))
            return new Knight(color, c1);

        if (newFigure.equals(Figures.BISHOP.toString()))
            return new Bishop(color, c1);

        if (newFigure.equals(Figures.QUEEN.toString()))
            return new Queen(color, c1);

        if (newFigure.equals(Figures.ROOK.toString()))

            return new Rook(color, c1);
        return null;

    }

    /**
     * самая важная процедура
     * обновляет доску
     *
     * @param c0 обнуляется
     * @param c1 сюда становится фигура, занимавшая позицию с0
     *           вместе с доской обновляется сумма очков на доске для фигур
     */
    public void updateBoard(Cell c0, Cell c1, Piece newFigure) {
        synchronized (board.field) {
            double scoreBeforeC0;
            double scoreBeforeC1;
            double scoreAfterC1;
//посчитали очки фигур в с0 и в с1 до перестановки - это будем отнимать
            scoreBeforeC0 = computeScoreInCell(c0);
            scoreBeforeC1 = computeScoreInCell(c1);

//        Piece f = this.getFigure(c0);

            //если произошло превращение пешки
            if (newFigure != null) {
                board.field[c1.getX()][c1.getY()] = newFigure;
            } else {
                new FigureInfo().setPosition(board.field[c0.getX()][c0.getY()], c1);
                board.field[c1.getX()][c1.getY()] = board.field[c0.getX()][c0.getY()];
            }
            board.field[c0.getX()][c0.getY()] = null;
            //переставили фигуры, посчитали, что теперь в с1, это прибавим

            scoreAfterC1 = computeScoreInCell(c1);
            score = score - scoreBeforeC0 - scoreBeforeC1 + scoreAfterC1;
        }
    }

    void updateCastling(Cell c0, Cell c1) {
        updateBoard(c0, c1, null);
        //если происходит восстановление после рокировки
        if ((c0.getX() == longKingX) || (c0.getX() == shortKingX)) {
            try {
                if (c0.getX() == longKingX) {
                    updateBoard(new Cell(longRookX1, c0.getY()), new Cell(longRookX, c0.getY()), null);
//                    board.field[longRookX][c0.getY()] = board.field[longRookX1][c0.getY()];
//                    board.field[longRookX1][c0.getY()] = null;
//                    new FigureInfo().setPosition(getFigure
//                            (new Cell(longRookX, c0.getY())), new Cell(longRookX, c0.getY()));
                } else {
                    updateBoard(new Cell(shortRookX1, c0.getY()), new Cell(shortRookX, c0.getY()), null);
//                    board.field[shortRookX][c0.getY()] = board.field[shortRookX1][c0.getY()];
//                    board.field[shortRookX1][c0.getY()] = null;
//                    new FigureInfo().setPosition(getFigure
//                            (new Cell(shortRookX, c0.getY())), new Cell(shortRookX, c0.getY()));
                }
//                        updateBoard(new Cell(shortRookX1, c0.getY()),
//                                new Cell(shortRookX, c0.getY()), null);
            } catch (Exception e) {
                Log.d("Exception", "Castling backward");
                e.printStackTrace();
            }
        }
        //если происходит сама рокировка
        else {
            try {
                if (c1.getX() == longKingX) {

                    updateBoard(new Cell(longRookX, c0.getY()),
                            new Cell(longRookX1, c0.getY()), null);

//                    board.field[longRookX1][c0.getY()] = board.field[longRookX][c0.getY()];
//                    board.field[longRookX][c0.getY()] = null;
//                    new FigureInfo().setPosition(getFigure
//                            (new Cell(longRookX1, c0.getY())), new Cell(longRookX1, c0.getY()));
                }
//
                else {
                    updateBoard(new Cell(shortRookX, c0.getY()),
                            new Cell(shortRookX1, c0.getY()), null);

//                    board.field[shortRookX1][c0.getY()] = board.field[shortRookX][c0.getY()];
//                    board.field[shortRookX][c0.getY()] = null;
//                    new FigureInfo().setPosition(getFigure
//                            (new Cell(shortRookX1, c0.getY())), new Cell(shortRookX1, c0.getY()));
                }

            } catch (Exception e) {
                Log.d("Exception forward", "");
                e.printStackTrace();
            }


        }
    }

    void updateMoves(Cell c) {
        try {
            if ((new FigureInfo().getName(getFigure(c)) == Figures.KING) && !(((King) getFigure(c)).isMoved()))
                ((King) getFigure(c)).setMoved(true);
            if ((new FigureInfo().getName(getFigure(c)) == Figures.ROOK) && !(((Rook) getFigure(c)).isMoved()))
                ((Rook) getFigure(c)).setMoved(true);
        } catch (Exception e) {
            Log.d("Exception", "updateMoves() called with: " + e.getStackTrace().toString());
        }
    }

    /**
     * процедура проверяет цвет фигуры в клетке доски
     *
     * @param c1- проверяемая ячейка,
     * @param c2  - позиция фигуры для которой проверяем цвет
     */
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
    }

    /**
     * Записывает игру в базу данных, начиная с первого хода
     */
    void firstWrite() {
        try {
            repository.addGame(date, gameName, humanPlayer, botPlayer);

        } catch (NullPointerException e) {//для тестов
            Log.d(TAG, "firstWrite() called");
        }
    }

    /**
     * Записывает ход в таблицу moves
     *
     * @param figureName имя фигуры
     * @param c0         откуда пошла
     * @param c1         куда пошла
     */
    void writeMove(String figureName, Cell c0, String capture, Cell c1, String savedFigureName, String threat) {
        try {
            repository.addMove(figureName, c0, capture, c1, savedFigureName, threat, date);
        } catch (NullPointerException e) {
        }
    }

    void updateGameTurn(String turn, boolean notation) {
        GameData gameData = new GameData();
        gameData.setGame_date(date);
        gameData.setName(gameName);
        gameData.setHuman_player(humanPlayer);
        gameData.setBot_player(botPlayer);
        gameData.setNotation(notation);
        gameData.setTurn(turn);
        repository.updateGame(gameData);
    }

    void cleanGame() {
        repository.deleteGameByDate(date);
    }

    void restoreFigure(Piece figure, Cell c) {
        board.field[c.getX()][c.getY()] = figure;
        score = score + computeScoreInCell(c);
    }


    void countScores() {
        double sum = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                sum = sum + computeScoreInCell(new Cell(i, j));
            }
        this.score = sum;

    }

    double computeScoreInCell(Cell c) {
        double score = 0;
        if (getFigure(c) != null) {
            score = new FigureInfo().getColor(getFigure(c)).getI() *
                    (new FigureInfo().getWeight(getFigure(c)) +
                            (new FigureInfo().getScore(getFigure(c))[c.getX()][c.getY()]));
        }
        return score;
    }

}