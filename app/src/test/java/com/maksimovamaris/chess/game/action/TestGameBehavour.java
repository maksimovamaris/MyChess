package com.maksimovamaris.chess.game.action;

import android.content.Context;

import com.maksimovamaris.chess.game.figures.Bishop;
import com.maksimovamaris.chess.game.figures.Colors;
import com.maksimovamaris.chess.game.figures.King;
import com.maksimovamaris.chess.game.figures.Knight;
import com.maksimovamaris.chess.game.figures.Pawn;
import com.maksimovamaris.chess.game.figures.Queen;
import com.maksimovamaris.chess.game.figures.Rook;
import com.maksimovamaris.chess.utils.StubRunner;
import com.maksimovamaris.chess.view.BoardView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TestGameBehavour {
    private Game testGame;
    private BoardView boardView;
    private Context viewContext;
    private Board board;
    private BoardDirector director;
    private Cell upperKingPos;
    private Cell lowerKingPos;

    @Before
    public void prepare() {
        testGame = new Game(new StubRunner());
        boardView = mock(BoardView.class);
        viewContext = mock(Context.class);
        when(boardView.getContext()).thenReturn(viewContext);
        testGame.createGame();
        testGame.attachView(boardView);
        //так как класс Game плотно общается с BoardDirector'ом, без реального экземпляра BoardDirector'а логику Game не протестировать
        board = new Board();
        director = new BoardDirector();
        upperKingPos = new Cell(0, 7);
        lowerKingPos = new Cell(0, 0);
    }


    /**
     * тестирует первый ход пешки
     */

    @Test
    public void testUpdate() {
        testGame.viewAction(new Cell(1, 1), new Cell(1, 3));
        verify(boardView).updateView(null);
    }

    /**
     * тестирует отрисовку подсказок
     */

    @Test
    public void testHints() {
        testGame.viewAction(null, new Cell(1, 1));
        verify(boardView).updateView(new Cell(1, 1));
    }

    /**
     * тестирует неверный ход (потрогали фигуру противника)
     */

    @Test
    public void testOpponentFigure() {
        testGame.viewAction(null, new Cell(1, 6));
        verify(boardView).printMessage("Don't touch your opponent's figures!");
    }

    /**
     * тестирует неверный ход (нажата пустая клетка, а фигура перед этим не выбрана)
     */
    @Test
    public void testFigureNotSelected() {
        testGame.viewAction(null, new Cell(1, 5));
        verify(boardView).printMessage("First choose a figure to move");
    }

    /**
     * тестирует некорректный ход (выбранная фигура не может сходить в выбранную клетку)
     */
    @Test
    public void testIncorrectMove() {
        testGame.viewAction(new Cell(1, 1), new Cell(1, 5));
        verify(boardView).printMessage("Incorrect move");
    }

    void prepareCheckPosition() {
        testGame.viewAction(new Cell(4, 1), new Cell(4, 3));
        testGame.viewAction(new Cell(4, 6), new Cell(4, 4));

        testGame.viewAction(new Cell(5, 0), new Cell(2, 3));
        testGame.viewAction(new Cell(5, 7), new Cell(2, 4));

        testGame.viewAction(new Cell(3, 0), new Cell(7, 4));
        testGame.viewAction(new Cell(3, 6), new Cell(3, 5));
    }

    /**
     * тестируется шах, но не мат, ходит слон
     */
    @Test
    public void testCheck() {
        prepareCheckPosition();
        testGame.viewAction(new Cell(2, 3), new Cell(5, 6));
        verify(boardView).printMessage("Check!");
    }

    /**
     * тестируется детский мат
     */
    @Test
    public void testCheckAndMate() {
        prepareCheckPosition();
        testGame.viewAction(new Cell(7, 4), new Cell(5, 6));
        verify(boardView).printMessage("Mate!");
    }

    /**
     * подготовка позиции для линейного мата
     * 2 черных ладьи
     */
    void prepareLineMateState() {
        //короли
        board.field[0][7] = new King(Colors.WHITE, upperKingPos);
        board.field[0][0] = new King(Colors.BLACK, lowerKingPos);

        //слон
        board.field[2][5] = new Bishop(Colors.WHITE, new Cell(2, 5));

        //2 ладьи
        board.field[2][6] = new Rook(Colors.BLACK, new Cell(2, 6));
        board.field[7][6] = new Rook(Colors.BLACK, new Cell(7, 6));


    }

    /**
     * тестируется линейный мат
     * белый слон закрывает короля от черного ферзя и не может защитить короля от наступающей ладьи
     */
    @Test
    public void testLineMate() {
        prepareLineMateState();
        board.field[5][2] = new Queen(Colors.BLACK, new Cell(5, 2));
        director.setBoard(board, new Cell(0, 7), new Cell(0, 0));
        testGame.restoreGame(director, "Black");
        testGame.viewAction(new Cell(7, 6), new Cell(7, 7));
        verify(boardView).printMessage("Mate!");
    }

    /**
     * Линейного мата нет, тестируется шах, белые могут закрыться слоном
     */
    @Test
    public void testNoLineMate() {
        prepareLineMateState();
        director.setBoard(board, new Cell(0, 7), new Cell(0, 0));
        testGame.restoreGame(director, "Black");
        testGame.viewAction(new Cell(7, 6), new Cell(7, 7));
        verify(boardView).printMessage("Check!");
    }

    /**
     * готовит позицию для шаха белой ладьи
     */
    private void prepareRookCheckPosition() {
        //короли
        board.field[0][7] = new King(Colors.BLACK, upperKingPos);
        board.field[0][0] = new King(Colors.WHITE, lowerKingPos);


        //2 ладьи
        board.field[2][4] = new Rook(Colors.WHITE, new Cell(2, 4));
        board.field[7][6] = new Rook(Colors.WHITE, new Cell(7, 6));

        //черный слон
        board.field[1][7] = new Bishop(Colors.BLACK, new Cell(1, 7));
    }

    private void prepareGameState(Cell whiteKingPos, Cell blackKingPos, String turn) {
        director.setBoard(board, whiteKingPos, blackKingPos);
        testGame.restoreGame(director, turn);
    }

    /**
     * тестируется возможность черного короля убежать и сообщение,
     * всплывающее при неправильном ходе короля
     */
    @Test
    public void testRookCheck() {
        prepareRookCheckPosition();
        prepareGameState(lowerKingPos, upperKingPos, "White");
        testGame.viewAction(new Cell(2, 4), new Cell(0, 4));
        verify(boardView).printMessage("Check!");
        testGame.viewAction(upperKingPos, new Cell(1, 6));
        verify(boardView).printMessage("Protect your king!");
        testGame.viewAction(new Cell(1, 7), new Cell(0, 6));
        verify(boardView, Mockito.times(2)).updateView(null);
    }

    private void prepareBishopCkeckPosition() {
        //короли
        board.field[0][7] = new King(Colors.BLACK, upperKingPos);
        board.field[0][0] = new King(Colors.WHITE, lowerKingPos);
        //2 ладьи
        board.field[2][3] = new Bishop(Colors.WHITE, new Cell(2, 3));
    }

    /**
     * тестируется шах слона, и возможность короля убежать от шаха
     */
    @Test
    public void testBishopCheck() {
        prepareBishopCkeckPosition();
        prepareGameState(lowerKingPos, upperKingPos, "White");
        testGame.viewAction(new Cell(2, 3), new Cell(3, 4));
        verify(boardView).updateView(null);
        verify(boardView).printMessage("Check!");
        testGame.viewAction(upperKingPos, new Cell(1, 7));
        verify(boardView, Mockito.times(2)).updateView(null);
    }

    /**
     * подготовка ничейной позиции: 2 короля и 2 однопольных слона
     */
    private void prepareDrawBishop() {
        board.field[0][7] = new King(Colors.BLACK, upperKingPos);
        board.field[0][0] = new King(Colors.WHITE, lowerKingPos);

        board.field[0][1] = new Bishop(Colors.WHITE, new Cell(0, 1));
        board.field[0][5] = new Bishop(Colors.BLACK, new Cell(0, 5));

    }


    public void testDraw() {

        prepareGameState(lowerKingPos, upperKingPos, "Black");
        testGame.viewAction(upperKingPos, new Cell(1, 7));
        verify(boardView).printMessage("Draw");
    }

    @Test
    public void testDrawBishop() {
        prepareDrawBishop();
        testDraw();
    }

    /**
     * подготовка ничейной позиции: 2 короля и 1 конь
     */
    private void prepareDrawKnight() {
        board.field[0][7] = new King(Colors.BLACK, upperKingPos);
        board.field[0][0] = new King(Colors.WHITE, lowerKingPos);

        board.field[0][1] = new Knight(Colors.WHITE, new Cell(0, 1));
    }

    @Test
    public void testDrawKnight() {
        prepareDrawKnight();
        testDraw();
    }

    private void prepareDrawKings()
    {
        board.field[0][7] = new King(Colors.BLACK, upperKingPos);
        board.field[0][0] = new King(Colors.WHITE, lowerKingPos);
    }
    @Test
    public void testDrawKings()
    {
        prepareDrawKings();
        testDraw();
    }


    /**
     * подготовка позиции для пата
     * белым некуда ходить, после хода черных объявляется ничья
     */
    private void prepareStaleMate()
    {
        board.field[0][7] = new King(Colors.BLACK, upperKingPos);
        board.field[0][0] = new King(Colors.WHITE, lowerKingPos);
        board.field[0][2]=new Queen(Colors.BLACK,new Cell(0,2));
        board.field[0][1]=new Bishop(Colors.BLACK,new Cell(0,1));
        board.field[1][6]=new Pawn(Colors.WHITE,new Cell(1,6));

    }

    @Test
    public void testStaleMate()
    {
        prepareStaleMate();
        testDraw();
    }

    //не протестировано
    //getBoardDirector()
    //detachView()
    //


}