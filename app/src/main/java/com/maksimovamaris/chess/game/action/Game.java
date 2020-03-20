package com.maksimovamaris.chess.game.action;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.maksimovamaris.chess.game.pieces.Bishop;
import com.maksimovamaris.chess.game.pieces.Piece;
import com.maksimovamaris.chess.game.pieces.FigureInfo;
import com.maksimovamaris.chess.game.pieces.King;
import com.maksimovamaris.chess.game.pieces.Knight;
import com.maksimovamaris.chess.game.pieces.Pawn;
import com.maksimovamaris.chess.game.pieces.Rook;
import com.maksimovamaris.chess.utils.Runner;
import com.maksimovamaris.chess.game.pieces.Colors;
import com.maksimovamaris.chess.view.games.BoardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author машуля
 * класс, выполняющий основную логику игры,
 * анализирует ситуацию на доске, общается с
 * @see BoardView и c
 * @see BoardDirector
 * Единственный экземпляр класса вместе с дополнительным потоком,
 * отведенным для сложных задач, создается в
 * @see com.maksimovamaris.chess.ChessApplication
 */
public class Game {
    private Player mPlayer1;
    private Player mPlayer2;
    private Player currentPlayer;
    private BoardDirector boardDirector;
    private BoardView boardView;
    private Runner runner;
    private Boolean isMate;
    private Boolean isDraw;
    private Cell attack;
    private boolean notation;
    private GameEndListener gameEndListener;
    private GameLocker locker;
    private FigureChoiceListener figureChoiceListener;
    private List<List<Cell>> bot_rescue;
    private List<List<Cell>> opponent_rescue;
    private boolean botPlays;
    private boolean virtualOpponent;
    private HashMap<Double, List<Cell>> weightedMoves;
    private List<Cell> hints;
    private CountDownLatch latch;
    //сохраняем для хода пешки
    private Cell selection;
    private Cell movingPoint;
    private int shortKingX = 6;
    private int shortRookX = 7;

    private int longKingX = 2;
    private int longRookX = 0;

    public Game(Runner runner) {
        this.runner = runner;
    }

    private void createPlayers(String botPlayer) {
        mPlayer1 = new Player(Colors.WHITE, false);
        mPlayer1.setFlag_move(true);
        mPlayer2 = new Player(Colors.BLACK, false);
        if (botPlayer.equals(Colors.WHITE.toString()))
            mPlayer1.setBot(true);
        else if (botPlayer.equals(Colors.BLACK.toString()))
            mPlayer2.setBot(true);
        currentPlayer = mPlayer1;

    }

    /**
     * процедура выполняется при каждом восстановлении/ создании новой игры
     * так как экземпляр игры единственный, его нужно приготовить к использованию
     * после того, как его достали из Application
     *
     * @param context контекст для runner'а
     */
    public void initGame(Context context) {
        isMate = null;
        isDraw = null;
        attack = null;
        botPlays = false;
        selection = null;
        movingPoint = null;
        virtualOpponent = false;
        bot_rescue = new ArrayList<>();
        opponent_rescue = new ArrayList<>();
        hints = new ArrayList<>();
        weightedMoves = new HashMap<>();
        boardDirector = new BoardDirector(context);
        boardDirector.startGame();
        setNotation(false);
    }

    /**
     * выполняется если нужно сделать запись в базе данных
     *
     * @param gameName    название игры
     * @param humanPlayer имя живого игрока
     * @param botPlayer   через него узнаем, нужно ли записывать бота
     */
    public void createGame(String gameName, String humanPlayer, String botPlayer) {
        runner.runInBackground(() -> {
            //проставили параметры в директор
            boardDirector.setBotPlayer(botPlayer);
            boardDirector.setHumanPlayer(humanPlayer);
            boardDirector.setGameName(gameName);
            //сделали запись в базе данных
            boardDirector.firstWrite();
            setCastling();
        });

        runner.runOnMain(() -> {
            createPlayers(botPlayer);
            notifyView(null, true, boardDirector);
        });

    }

    /**
     * настраивает позицию для рокировки,
     * так как с поворотом доски ситуация с короткой/длинной рокировкой меняется
     */
    private void setCastling() {
        if (!boardDirector.isWhiteFront()) {
            shortKingX = 7 - shortKingX;
            shortRookX = 7 - shortRookX;
            longKingX = 7 - longKingX;
            longRookX = 7 - longRookX;
        }
    }

    /**
     * восстанавливаем игру из бд по дате
     *
     * @param date дата по которой находим игру в бд
     */
    public void restoreGame(Date date) {
        runner.runInBackground(() -> {
            int num_moves = boardDirector.restoreGame(date);
            setCastling();
            runner.runOnMain(() -> {
                //тянем из директора информацию о боте
                createPlayers(boardDirector.botPlayer);
                //определяем игрока по цвету последней ходившей фигуры
                //если количество ходов нечетно, ходят черные
                //то есть меняем игрока
                if (num_moves % 2 == 1)
                    changePlayer();
                //если король в опасности, оповещаем об этом шахом
                if (!kingProtected(getKingPos()))
                    Toast.makeText(boardView.getContext(), "Check!", Toast.LENGTH_SHORT).show();
                notifyView(null, false, boardDirector);
            });

        });
    }

    public void setLocker(GameLocker locker) {
        this.locker = locker;
    }

    public void setGameEndListener(GameEndListener gameEndListener) {
        this.gameEndListener = gameEndListener;
    }

    public void setFigureChoiceListener(FigureChoiceListener figureChoiceListener) {
        this.figureChoiceListener = figureChoiceListener;
    }

    public void setNotation(boolean notation) {
        this.notation = notation;
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

    public boolean isNotation() {
        return notation;
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
        return (new FigureInfo().getPosition
                (boardDirector.kings.get(getCurrentPlayer().getColor())));
    }

    /**
     * @return находится ли король под боем
     */
    boolean kingProtected(Cell kingPos) {

        int colorIndex = getCurrentPlayer().getColor().getI();
        if (!boardDirector.isWhiteFront())
            colorIndex = colorIndex * (-1);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) { //сначала проверяем что ячейка не пуста
                if ((boardDirector.getFigure(new Cell(i, j)) != null) &&
                        //проверяем что фигура не того же цвета, что король
                        (new FigureInfo().getColor(boardDirector.getFigure(new Cell(i, j))) !=
                                //именно getKingPos() потому что в kingPos короля может не быть, а мы только предполагаем, что он там есть
                                new FigureInfo().getColor(boardDirector.getFigure(getKingPos()))) &&
                        //что фигура не пешка
                        (!(boardDirector.getFigure(new Cell(i, j)) instanceof Pawn)) &&
                        //проверяем что фигура может сходить на место короля
                        (checkFigure(new Cell(i, j), kingPos))) {
                    //если все условия выполнены, заходим сюда
                    attack = new Cell(i, j);
                    return false;
                } else
                    //если это пешка
                    if ((boardDirector.getFigure(new Cell(i, j)) instanceof Pawn)
                            //не того же цвета что король
                            && (new FigureInfo().getColor(boardDirector.getFigure(new Cell(i, j))) !=
                            new FigureInfo().getColor(boardDirector.getFigure(getKingPos())))
                            //стоит по диагонали и может "убить" короля
                            &&
                            (Math.abs(i - kingPos.getX()) == 1) && (Math.abs(j - kingPos.getY()) == 1) &&
                            //связано с направлением, по которому пешка может взять фигуру
                            ((j - kingPos.getY()) == colorIndex)) {
                        attack = new Cell(i, j);
                        return false;
                    }
            }
        return true;
    }


    /**
     * посредник между игрой и view
     * проверяет ход живого игрока
     *
     * @param c0 откуда ходим
     */
    void setHints(Cell c0) {
        List<Cell> hints = new ArrayList<>();
        if (c0 != null)
            for (Cell c1 : boardDirector.getFigure(c0).getPossiblePositions(boardDirector)) {
                if (canMove(c0, c1))
                    hints.add(c1);
            }
        this.hints = hints;


        //если король был выбран
        if ((boardDirector.getFigure(c0) instanceof King) &&
                //и не двигался
                ((!((King) (boardDirector.getFigure(c0))).isMoved()))) {
            if
                //проверяем короткую 6 7
            (checkCastling(shortKingX, shortRookX))
                this.hints.add(new Cell(shortKingX, getKingPos().getY()));
//            и длинную рокировки 2 0
            if (
                    checkCastling(longKingX, longRookX))
                this.hints.add(new Cell(longKingX, getKingPos().getY()));
        }

    }

    void clean() {
        runner.runInBackground(() -> {
            boardDirector.cleanGame();
        });
    }


    /**
     * выбирает лучший ход, на основе минмакс алгоритма, просчитывается сумма
     * очков доски
     * очки считаются как вес фигуры + очки за ее положение на доске
     * после каждого возможного хода вперед
     * и с минимальной суммой для черных
     *
     * @param moves           сет ходов из которых выбираем лучший
     * @param virtualOpponent - нужно ли нам просчитывать на ход вперед
     *                        или нет
     * @return очки за ход с максимальной суммой для белых
     * и с минимальной суммой для черных
     */
    private Double selectBestMove(List<List<Cell>> moves, boolean virtualOpponent) {


//        List<Double> opponentScore = new ArrayList<>();
        for (List<Cell> l : moves) {
//            //перед каждым ходом проверяем, нужно ли просчитывать на ход вперед или нет
//            this.virtualOpponent = virtualOpponent;
            Piece savedFigure = boardDirector.getFigure(l.get(1));
            changePlayer();
            updateGame(l.get(0), l.get(1), null);
            if (!kingProtected(getKingPos())) {
                isMate = checkMate(attack);
                if (isMate) {
                    changePlayer();
                    updateGame(l.get(1), l.get(0), null);
                    boardDirector.restoreFigure(savedFigure, l.get(1));
                    double mateScore = 900;
                    weightedMoves.put(mateScore, l);
                    return mateScore;
                }
            }
            if (checkDraw()) {
                isDraw = true;
                changePlayer();
                updateGame(l.get(1), l.get(0), null);
                double drawScore = 800;
                weightedMoves.put(drawScore, l);
                return drawScore;
            }
//                //если мы не считали xод за противника
//                if (virtualOpponent) {
//                    //посчитаем ход за противника
//                    //сумму очков за сделанный ход и "лучший", по нашим критериям, ход противника
            weightedMoves.put(boardDirector.score, l);
//                                    +
//                            selectBestMove(opponent_rescue, false), l);
//                    opponent_rescue.clear();
//                } else
//                //если мы как раз считаем за противника
//                {
//                    opponentScore.add(boardDirector.score);
//                }
//            }
//            //после каждого просчитанного вперед хода откатываемся обратно
            changePlayer();
            updateGame(l.get(1), l.get(0), null);
            boardDirector.restoreFigure(savedFigure, l.get(1));
            isDraw = isMate = null;

//        //если считаем за противника
//        if (!virtualOpponent) {
//            if (getCurrentPlayer().getColor() == Colors.WHITE)
//                return Collections.max(opponentScore);
//            else
//                return Collections.min(opponentScore);
//        } else {
        }
//
        if (getCurrentPlayer().getColor() == Colors.WHITE)
            return (Collections.max(weightedMoves.keySet()));
        else
            return (Collections.min(weightedMoves.keySet()));

    }

    public void moveBot(boolean firstMove) {
        locker.lock();
        runner.runInBackground(() -> {


            latch = new CountDownLatch(1);
            try {
                latch.await(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();


            //если первый ход, то получаем ходы для бота через checkDraw()
            if (firstMove)
                checkDraw();
            //достаем лучший ход по ключу - оптимальным очкам
            botPlays = true;
            List<Cell> bestMove = weightedMoves.get(selectBestMove(bot_rescue, true));
            //выберем ход для бота
            //запишем ход бота в базу данных

            boardDirector.writeMove(new FigureInfo().getName(boardDirector.getFigure(bestMove.get(0))).toString(),
                    bestMove.get(0), " - ", bestMove.get(1), "", "");
            //сделаем финальное обновление игры
            updateGame(bestMove.get(0), bestMove.get(1), null);
            changePlayer();
            //очищаем ходы бота
            weightedMoves.clear();
            bot_rescue.clear();
            opponent_rescue.clear();
            botPlays = false;
            virtualOpponent = false;
            runner.runOnMain(() -> {
                locker.unlock();
                notifyView(null, false, boardDirector);
                if (isMate != null) {
                    gameEndListener.endGame("Checkmate!");
                } else if (isDraw != null) {
                    gameEndListener.endGame("Draw");
                }
            });
        });
    }


    /**
     * @param attack позиция атакующей короля фигуры
     * @return если матовая ситуация, возвращает true, иначе false
     */
    private boolean checkMate(Cell attack) {
        //проверка, может ли король убежать
        Piece savedFigure;
        for (Cell c : boardDirector.getFigure(getKingPos()).getPossiblePositions(boardDirector)) {
            if (canMove(getKingPos(), c)) {
                if ((!botPlays && !boardDirector.botPlayer.equals("")) || (virtualOpponent))
                    addBotMove(getKingPos(), c);
                else {
                    return false;
                }
            }
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
            int xInc = (attack.getX() - getKingPos().getX()) / (Math.abs(attack.getX() - getKingPos().getX()));
            int yInc = (attack.getY() - getKingPos().getY()) / (Math.abs(attack.getY() - getKingPos().getY()));
            for (int i = 0; i < Math.abs((attack.getX()) - getKingPos().getX()); i++)
                if (!(new Cell(attack.getX(), i).equals(getKingPos())) && !(new Cell(attack.getX(), i).equals(attack)))
                    lineAttack.add(new Cell(attack.getX() - xInc * i, attack.getY() - yInc * i));
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
                        if (canMove(new Cell(i, j), c)) {
                            //если бот есть, записываем для него возможный ход,
                            //иначе не делаем лишнюю работу
                            if ((!botPlays && !boardDirector.botPlayer.equals(""))
                                    || (virtualOpponent))
                                addBotMove(new Cell(i, j), c);
                            else return false;
                        }
        if ((bot_rescue.size() > 0 && !botPlays) || (opponent_rescue.size() > 0 && !virtualOpponent))
            return false;
        else
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
            Piece savedFigure = boardDirector.getFigure(c1);
            //обновляем игру
            updateGame(c0, c1, null);
            //если король после данного хода защищен
            if (kingProtected(getKingPos())) {
                //все ок, мата нет, так ходить можно
                updateGame(c1, c0, null);
                boardDirector.restoreFigure(savedFigure, c1);
                return true;
            }
            updateGame(c1, c0, null);
            boardDirector.restoreFigure(savedFigure, c1);
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
        List<Piece> currentArmy = new ArrayList<>();
        //отделяем фигуры, которые нужны
        for (Piece figure : boardDirector.getArmy())
            if (new FigureInfo().getColor(figure) == getCurrentPlayer().getColor())
                currentArmy.add(figure);
        //добавляем короля
        currentArmy.add(boardDirector.getFigure(getKingPos()));
        //проходимся по фигурам текущего игрока
        for (Piece figure : currentArmy)
            //проверяем каждую из них, может ли она сходить
            if (figure.getPossiblePositions(boardDirector).size() != 0)
                for (Cell c : figure.getPossiblePositions(boardDirector))
                    //проходимся по возможным ходам фигуры, проверяем безопасность короля после потенциального хода
                    if (canMove(new FigureInfo().getPosition(figure), c)) {
                        //если бот присутствует и сейчас не играет
                        if ((!botPlays && !boardDirector.botPlayer.equals("")) || (virtualOpponent))
                            //добавляем в массив возможные ходы бота
                            addBotMove(new FigureInfo().getPosition(figure), c);
                        else
                            return false;
                    }
        if ((bot_rescue.size() > 0 && !botPlays) || (opponent_rescue.size() > 0 && virtualOpponent))
            return false;
        else
            return true;

    }

    /**
     * добавляет допутимый ход для бота в мап
     *
     * @param c0 откуда пойти
     * @param c1 куда пойти
     */
    void addBotMove(Cell c0, Cell c1) {
        ArrayList<Cell> rescue = new ArrayList<>();
        rescue.add(c0);
        rescue.add(c1);
        if (virtualOpponent)
            opponent_rescue.add(rescue);
        else
            bot_rescue.add(rescue);
    }


    /**
     * обновляет игровое поле
     * вызывается метод класса
     * boardDirector.updateBoard()
     */
    public void updateGame(Cell c0, Cell c1, Piece newFigure) {
        boardDirector.updateBoard(c0, c1, newFigure);
    }


    public void viewAction(Cell selection, Cell c) {
        FigureInfo info = new FigureInfo();
        //есть фигура
        if (boardDirector.getFigure(c) != null) {
            if (info.getColor(boardDirector.getFigure(c)) == getCurrentPlayer().getColor())
            //если фигура того же цвета, что игрок
            // обновляем подсказки
            {

                runner.runInBackground(() -> {

                    setHints(c);
                    runner.runOnMain(() -> {
                        notifyView(c, false, boardDirector);
                    });
                });
                return;
            } else //если фигуры разного цвета
            {
                if (selection == null) //не была выбрана фигура, которой ходим
                {
                    Toast.makeText(boardView.getContext(), "Don't touch your opponent's figures!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else {
            if (selection == null)// если ничего не выбрано
            {
                Toast.makeText(boardView.getContext(), "First choose a figure to move", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (hints.contains(c)) {

            //если это пешка и она дошла до крайней клетки доски
            if ((boardDirector.getFigure(selection) instanceof Pawn) && (c.getY() == 7 || c.getY() == 0)) {
                //сохраняем координаты хода
                this.selection = selection;
                this.movingPoint = c;
                //показываем диалог выбора фигуры
                figureChoiceListener.onChoiceStarted();
                return;
            } else
                processMove(selection, c, null);
        } else
            Toast.makeText(boardView.getContext(), "Incorrect move", Toast.LENGTH_SHORT).show();
    }

    /**
     * Изменяет игру в соотвествии с совершенным ходом
     *
     * @param c0 откуда
     * @param c1 куда
     */
    private void processMove(Cell c0, Cell c1, Piece savedFigure) {
        latch = null;
        //сохраняем копию на директор, и ее передаем во вью

//        BoardDirector d = boardDirector;
//        boardDirector = d;


        runner.runInBackground(() ->
        {
            //разберемся, что записать в бд о ходе
            String capture = "-";
            String threat = "";
            String newFigureName = "";
            String figureName = new FigureInfo().getName(boardDirector.getFigure(c0)).toString();

            //фиксируем взятие фигуры
            Piece capturedFigure = boardDirector.getFigure(c1);
            if (capturedFigure != null)
                capture = ":";
            if (savedFigure != null)
                newFigureName = new FigureInfo().getName(savedFigure).toString();


            //даже в случае рокировки сохраняется только 1 ход,
            // чтобы не было путаницы с очередью игрока, которая считается
            //исходя из
            //количества ходов

            updateGame(c0, c1, savedFigure);
            //смотрим, кто пошел в с1, если это король или ладья, отмечаем что они уже ходили
            boardDirector.updateMoves(c1);
            // запись хода
            //фигура, стоявшая в с0, УЖЕ переместилась в с1 - ее имя берем из с1
            changePlayer();
            isMate = null;
            isDraw = null;
            attack = null;

            // поменяли игрока, проверка противника на шах/мат
            //если король противника попал под шах после хода
            if (!kingProtected(getKingPos())) {
                //проверяем на мат
                isMate = checkMate(attack);
                //если определен мат
                if (isMate) {

                    threat = " X";
                    runner.runOnMain(() ->
                    {
                        changePlayer();
                        notifyView(null, false, boardDirector);
                        gameEndListener.endGame("Checkmate!");

                    });
                }
                //иначе только шах
                else {
                    threat = " +";
                    runner.runOnMain(() -> {
                        notifyView(null, false, boardDirector);
                        Toast.makeText(boardView.getContext(), "Check", Toast.LENGTH_SHORT).show();
                    });
                }
            }
            //если все хорошо, проверяем на ничью
            else {

                if (checkDraw()) {
                    isDraw = true;
                    threat = " =";
                    runner.runOnMain(() -> {
//
//                        if(getCurrentPlayer().isBot())
//                            locker.unlock();
                        changePlayer();
                        notifyView(null, false, boardDirector);
                        gameEndListener.endGame("Draw");
                    });
                } else {
                    latch = new CountDownLatch(1);
                    runner.runOnMain(() -> {
                        notifyView(null, false, boardDirector);
                    });

                }
            }

            boardDirector.writeMove(figureName, c0, capture, c1, newFigureName, threat);

        });
    }

    public void pawnTurning(String figureName) {
        Piece newFigure = boardDirector.pawnTurning(figureName,
                getCurrentPlayer().getColor(), movingPoint);
        //фиксируем ход
        processMove(selection, movingPoint, newFigure);
        //обнуляем сохраненные значения
        movingPoint = selection = null;
    }

    public void updateTurn() {
        runner.runInBackground(() -> {
                    if (isDraw != null)
                        boardDirector.updateGameTurn("draw", isNotation());
                    else
                        boardDirector.updateGameTurn(getCurrentPlayer().getColor().toString(), isNotation());
                }
        );
    }

    /**
     * Для Тестов!
     *
     * @param director - доска с позицией
     * @param turn     - опеделяет текущего игрока
     */
    void restoreTestGame(BoardDirector director, String turn) {
//бот не участвует в тестировании, его строка пустая
        createPlayers("");
        if (turn == "white")
            currentPlayer = mPlayer1;
        else
            currentPlayer = mPlayer2;
        this.boardDirector = director;
    }


    public void attachView(BoardView view) {
        boardView = view;
        view.setGame(this);
    }

    public void detachView() {
        boardView = null;
    }

    /**
     * сигнал от
     *
     * @param c - фигура которая была выбрана (null, есл иничего не было выбрано)
     * @see Game
     * для
     * @see BoardView
     */
    private void notifyView(Cell c, boolean firstTime, BoardDirector director) {
        if (c == null)
            hints.clear();
        boardView.updateView(c, hints, firstTime, director);
    }

    /**
     * рокировку можно делать
     * если король и ладья находились без движения
     * + если ни одно поле, которое проходит король,
     * не окажется под шахом
     *
     * @param kingX - предполагаемая позиция короля после рокировки
     * @param rookX - позиция рокируемой ладьи до рокировки
     * @return можно ли сделать рокировку в указанную сторону
     */
    private boolean checkCastling(int kingX, int rookX) {
        int y = getKingPos().getY();
        //если в углу стоит ладья и она не двигалась
        if ((boardDirector.getFigure(new Cell(rookX, y)) instanceof Rook) &&
                (!((Rook) (boardDirector.getFigure(new Cell(rookX, y)))).isMoved())) {

            //проверим, пусто ли между предполагаемой позицией короля и ладьей
            for (int i = Math.min(rookX, kingX); i <= Math.max(kingX, rookX); i++)
                if ((i != rookX) && (boardDirector.getFigure(new Cell(i, y)) != null))
                    return false;

            //проверим, пусто ли между текущей и предполагагемой
            //позициями короля
            // и безопасно ли выполнять рокировку
            for (int i = Math.min(getKingPos().getX(), kingX); i <= Math.max(kingX, getKingPos().getX()); i++) {
                //если король окажется под шахом на линии, рокировку делать нельзя
                if (((i != (getKingPos().getX())) && (boardDirector.getFigure(new Cell(i, y)) != null)) || !kingProtected(new Cell(i, y)))
                    return false;
            }
        } else
            return false;

        return true;
    }

    public void countLatch() {
        latch.countDown();
    }

    public void waitLatch() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
//еще не реализовано взятие на проходе
