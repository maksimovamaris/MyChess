package com.maksimovamaris.chess.game.action;

import android.content.Context;
import android.widget.Toast;

import com.maksimovamaris.chess.game.figures.Bishop;
import com.maksimovamaris.chess.game.figures.ChessFigure;
import com.maksimovamaris.chess.game.figures.FigureInfo;
import com.maksimovamaris.chess.game.figures.Knight;
import com.maksimovamaris.chess.utils.Runner;
import com.maksimovamaris.chess.game.figures.Colors;
import com.maksimovamaris.chess.view.BoardView;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player mPlayer1;
    private Player mPlayer2;
    private Player currentPlayer;
    private BoardDirector boardDirector;
    private BoardView boardView;
    private Runner runner;
    private Boolean isMate;
    private Cell attack;
    private boolean first_move;

    public Game(Runner runner) {
        isMate = null;
        this.runner = runner;
    }

    private void createPlayers() {

        mPlayer1 = new Player(Colors.WHITE, "Player_1", false);
        mPlayer1.setFlag_move(true);
        mPlayer2 = new Player(Colors.BLACK, "Player_2", false);
    }

    public void createGame(Context context) {
        boardDirector = new BoardDirector(context);
        boardDirector.startGame();
        createPlayers();
        currentPlayer = mPlayer1;
        attack = null;
        first_move = true;
    }

    public BoardDirector getBoardDirector() {
        return boardDirector;
    }

    public void changePlayer() {
        if (currentPlayer == mPlayer1)
            currentPlayer = mPlayer2;
        else
            currentPlayer = mPlayer1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @param c0 позиция фигуры
     * @param c1 куда фигура хочет пойти
     * @return может ли ходить фигура находящаяся в с0 в клетку с1
     */
    public boolean checkFigure(Cell c0, Cell c1) {
        List<Cell> list = boardDirector.getFigure(c0).getPossiblePositions(boardDirector);
        return list.contains(c1);
    }

    /**
     * @return возвращает позицию короля текущего игрока
     */
    private Cell getKingPos() {

        if (this.getCurrentPlayer().getColor() == Colors.BLACK)
            return boardDirector.blackKingPos;
        else
            return boardDirector.whiteKingPos;
    }

    /**
     * @return находится ли король под боем
     */
    boolean kingProtected(Cell kingPos) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                //сначала проверяем что ячейка не пуста
                if ((boardDirector.getFigure(new Cell(i, j)) != null) &&
                        //проверяем что фигура не того же цвета, что король
                        (new FigureInfo().getColor(boardDirector.getFigure(new Cell(i, j))) !=
                                //именно getKingPos() потому что в kingPos короля может не быть, а мы только предполагаем, что он там есть
                                new FigureInfo().getColor(boardDirector.getFigure(getKingPos()))) &&
                        //проверяем что фигура может сходить на место короля
                        (checkFigure(new Cell(i, j), kingPos))) {
                    //если все условия выполнены, заходим сюда
                    attack = new Cell(i, j);
                    return false;
                }
        return true;
    }

    /**
     * посредник между игрой и view
     * проверка хода отдельной фигуры с анализом ситуации на доске
     *
     * @param c0 откуда ходим
     * @param c1 куда ходим
     */
    void checkMove(Cell c0, Cell c1) {
        runner.runInBackground(() -> {
            ChessFigure savedFigure = null;
            if (checkFigure(c0, c1)) {
//                if (boardDirector.getFigure(c1) != null)
                savedFigure = boardDirector.getFigure(c1);//прихраняем то что есть в с1
                updateGame(c0, c1, savedFigure);
                //если король защищен
                if (kingProtected(getKingPos())) {
                    if (first_move) {
                        first_move = false;
                        //создание таблиц в базе данных
                        boardDirector.firstWrite();
                    } else {
                        // запись хода
                        //фигура, стоявшая в с0, УЖЕ переместилась в с1 - ее имя берем из с1
                        boardDirector.writeMove(new FigureInfo().getName(boardDirector.getFigure(c1)).toString(), c0, c1);
                    }
                    changePlayer();
                    isMate = null;
                    attack = null;
                    runner.runOnMain(() ->
                            notifyView(null));
                    // поменяли игрока, проверка противника на шах/мат
                    //если король противника попал под шах после хода
                    if (!kingProtected(getKingPos())) {
                        //проверяем на мат
                        isMate = checkMate(attack);
                        //если определен мат
                        if (isMate == true) {
                            try {
                                boardDirector.cleanGame();
                                runner.runOnMain(() -> {
                                    Toast.makeText(boardView.getContext(), "Mate!", Toast.LENGTH_SHORT).show();
                                    detachView();
                                });


                            } catch (NullPointerException e) {
                                boardView.printMessage("Mate!");
                            }
                        }
                        //иначе только шах
                        else {
                            try {
                                runner.runOnMain(() -> Toast.makeText(boardView.getContext(), "Check", Toast.LENGTH_SHORT).show());
                            } catch (NullPointerException e) {
                                boardView.printMessage("Check!");
                            }
                        }
                    }
                    //если все хорошо, проверяем на ничью
                    else {
                        if (checkDraw()) {
                            try {
                                boardDirector.cleanGame();
                                runner.runOnMain(() -> {
                                    Toast.makeText(boardView.getContext(), "Draw", Toast.LENGTH_SHORT).show();
                                    detachView();
                                });

                            } catch (NullPointerException e) {
                                boardView.printMessage("Draw");
                            }
                        }
                    }
                } else {
                    //откатываемся обратно, король в опасности
                    updateGame(c1, c0, savedFigure);
                    try {
                        runner.runOnMain(() ->
                                Toast.makeText(boardView.getContext(), "Protect your king!", Toast.LENGTH_SHORT).show());
                    } catch (NullPointerException e) {
                        boardView.printMessage("Protect your king!");
                    }
                }
            } else
                try {
                    runner.runOnMain(() ->
                            Toast.makeText(boardView.getContext(), "Incorrect move", Toast.LENGTH_SHORT).show());
                } catch (NullPointerException e) {
                    boardView.printMessage("Incorrect move");
                }
        });
    }

    /**
     * @param attack позиция атакующей короля фигуры
     * @return если матовая ситуация, возвращает true, иначе false
     */
    private boolean checkMate(Cell attack) {
        //проверка, может ли король убежать
        ChessFigure savedFigure;
        for (Cell c : boardDirector.getFigure(getKingPos()).getPossiblePositions(boardDirector)) {
            if (canMove(getKingPos(), c))
                return false;
        }
        //определение линии поражения
        List<Cell> lineAttack = new ArrayList();
        lineAttack.add(attack);
        //если вертикаль
        if (attack.getX() == getKingPos().getX())
            for (int i = Math.min(attack.getY(), getKingPos().getY()); i < Math.max(attack.getY(), getKingPos().getY()); i++)
                if (!(new Cell(attack.getX(), i).equals(getKingPos())) && !(new Cell(attack.getX(), i).equals(attack)))
                    lineAttack.add(new Cell(attack.getX(), i));

//горизонталь
        if (attack.getY() == getKingPos().getY())
            for (int i = Math.min(attack.getX(), getKingPos().getX()); i < Math.max(attack.getX(), getKingPos().getX()); i++)
                if ((new Cell(attack.getX(), i) != getKingPos()) && (new Cell(attack.getX(), i) != attack))
                    lineAttack.add(new Cell(i, attack.getY()));

        //если диагональ
        if (Math.abs(attack.getX() - getKingPos().getX()) == Math.abs(attack.getY() - getKingPos().getY())) {
            for (int i = 0; i < (attack.getX()) - getKingPos().getX(); i++)
                if (!(new Cell(attack.getX(), i).equals(getKingPos())) && !(new Cell(attack.getX(), i).equals(attack)))
                    lineAttack.add(new Cell(Math.min(attack.getX(), getKingPos().getX()) + i,
                            Math.min(attack.getY(), getKingPos().getY()) + i));
        }

        //проходим по линии поражения
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                //если это не атакуемый король
                if (!(new Cell(i, j).equals(getKingPos()))
                        //если в ячейке есть фигура
                        && (boardDirector.getFigure(new Cell(i, j)) != null)
                        //если фигура того же цвета, что и атакуемый король
                        && (new FigureInfo().getColor(boardDirector.getFigure(new Cell(i, j))) == new FigureInfo().getColor(boardDirector.getFigure(getKingPos()))))
                    for (Cell c : lineAttack)
                        //проверяем каждую ячейку линии на бой фигурой
                        //если фигура может туда сходить и после этого король не в опасности
                        if (canMove(new Cell(i, j), c))
                            return false;
        return true;
    }


    /**
     * Проверяет потенциальный ход фигуры
     *
     * @param c0 откуда ходим
     * @param c1 куда ходим
     * @return может ли пойти фигура в клетку, чтобы король не оказался после этого
     * под боем, true - может
     */
    private boolean canMove(Cell c0, Cell c1) {
        if (checkFigure(c0, c1))
        //фигура может по правилам сходить в клетку, осталось проверить короля
        {
            ChessFigure savedFigure = boardDirector.getFigure(c1);
            //обновляем игру
            updateGame(c0, c1, savedFigure);
            //если король после данного хода защищен
            if (kingProtected(getKingPos())) {
                //все ок, мата нет, так ходить можно
                updateGame(c1, c0, savedFigure);
                return true;
            }
            updateGame(c1, c0, savedFigure);
        }
        return false;
    }

    /**
     * @return есть ли ничья: если true - ничья есть, иначе false
     */
    private boolean checkDraw() {
        //смотрим ничью по оставшимся фигурам
        //если у каждого игрока осталось< 2 фигур, не считая короля

        //если только 2 короля
        if (boardDirector.getArmy().size() == 0)
            return true;

        //если осталось 1 фигура у кого-то
        if ((boardDirector.getArmy().size() == 1)
                //если эта фигура слон или конь
                && ((boardDirector.getArmy().get(0) instanceof Bishop)
                || (boardDirector.getArmy().get(0) instanceof Knight)))
            return true;

        //иначе если осталось произвольное количество однопольных слонов
        int i;
        int mult = 1;
        for (i = 0; (i < boardDirector.getArmy().size()) && (boardDirector.getArmy().get(i) instanceof Bishop); i++)
            //высчитываем произведение суммы координат каждого слона, чтобы понять, однопольные ли слоны
            mult = mult * (new FigureInfo().getPosition(boardDirector.getArmy().get(i)).getX() +
                    new FigureInfo().getPosition(boardDirector.getArmy().get(i)).getY());
        //если в армии все слоны и они все однопольные, то есть
        // каждый имеет сумму координат одинаковой четности
        if (i == boardDirector.getArmy().size() && (mult % 2 ==
                (new FigureInfo().getPosition(boardDirector.getArmy().get(0)).getX() +
                        new FigureInfo().getPosition(boardDirector.getArmy().get(0)).getY()) % 2))
            return true;

        //иначе смотрим на возможные ходы текущего игрока
        List<ChessFigure> currentArmy = new ArrayList<>();
        //отделяем фигуры, которые нужны
        for (ChessFigure figure : boardDirector.getArmy())
            if (new FigureInfo().getColor(figure) == getCurrentPlayer().getColor())
                currentArmy.add(figure);
        //добавляем короля
        currentArmy.add(boardDirector.getFigure(getKingPos()));
        //проходимся по фигурам текущего игрока
        for (ChessFigure figure : currentArmy)
            //проверяем каждую из них, может ли она сходить
            if (figure.getPossiblePositions(boardDirector).size() != 0)
                for (Cell c : figure.getPossiblePositions(boardDirector))  //проходимся по возможным ходам фигуры, проверяем безопасность короля после потенциального хода
                    if (canMove(new FigureInfo().getPosition(figure), c))
                        return false;


        return true;

    }

    /**
     * обновляет игровое поле
     * вызывается метод класса
     * boardDirector.updateBoard()
     */
    public void updateGame(Cell c0, Cell c1, ChessFigure savedFigure) {
        boardDirector.updateBoard(c0, c1, savedFigure);
    }


    public void viewAction(Cell selection, Cell c) {
        FigureInfo info = new FigureInfo();
        //есть фигура
        if (boardDirector.getFigure(c) != null) {
            if (info.getColor(boardDirector.getFigure(c)) == getCurrentPlayer().getColor())
            //если фигура того же цвета, что игрок
            //обновляем подсказки
            {

                notifyView(c);

            } else //если фигуры разного цвета
            {
                if (selection != null)//была ли выбрана фигура, которой ходим
                {
                    checkMove(selection, c);

                } else
                    try {
                        Toast.makeText(boardView.getContext(), "Don't touch your opponent's figures!", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        boardView.printMessage("Don't touch your opponent's figures!");
                    }
            }
        } else {
            if (selection != null)//если до этого было что-то выбрано
            {
                //проверяем ход на корректность
                checkMove(selection, c);

            } else //если ничего не выбрано
            {
                try {
                    Toast.makeText(boardView.getContext(), "First choose a figure to move", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    boardView.printMessage("First choose a figure to move");
                }
            }
        }
    }

    /**
     * @param director - доска с позицией
     */
    void restoreGame(BoardDirector director, String turn) {

        createPlayers();
        if (turn == "white")
            currentPlayer = mPlayer1;
        else
            currentPlayer = mPlayer2;
        this.boardDirector = director;
        // attack = null; - подумать что с этим делать
    }


    public void attachView(BoardView view) {
        boardView = view;
        view.setGame(this);
    }

    public void detachView() {
        //удаляем игру, она не нужна
        boardView = null;
    }

    private void notifyView(Cell c) {
        boardView.updateView(c);
    }
}
//еще не реализована рокировка, взятие на проходе, преврашение пешки

