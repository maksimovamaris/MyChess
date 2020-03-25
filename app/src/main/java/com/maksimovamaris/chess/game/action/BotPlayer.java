package com.maksimovamaris.chess.game.action;

import com.maksimovamaris.chess.game.pieces.Colors;
import com.maksimovamaris.chess.game.pieces.FigureInfo;
import com.maksimovamaris.chess.game.pieces.Piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BotPlayer extends Player {
    private Game game;
    private Boolean isMate;
    private Boolean isDraw;
    private HashMap<Double, List<Cell>> weightedMoves;

    public BotPlayer(Colors color, Game game) {

        super(color);
        this.game = game;
        isMate = null;
        isDraw = null;
        weightedMoves = new HashMap<>();

    }

    /**
     * @return список возможных ходов бота
     */
    private List<List<Cell>> bot_rescue() {
        List<List<Cell>> botMoves = new ArrayList<>();
        List<Piece> currentArmy = new ArrayList<>();
        //отделяем фигуры, которые нужны
        for (Piece figure : game.getBoardDirector().getArmy())
            if (new FigureInfo().getColor(figure) == game.getCurrentPlayer().getColor())
                currentArmy.add(figure);
        //добавляем короля
        currentArmy.add(game.getBoardDirector().getFigure(game.getKingPos()));
        //проходимся по фигурам текущего игрока
        for (Piece figure : currentArmy)
            //проверяем каждую из них, может ли она сходить
            if (figure.getPossiblePositions(game.getBoardDirector()).size() != 0)
                for (Cell c : figure.getPossiblePositions(game.getBoardDirector()))
                    //проходимся по возможным ходам фигуры, проверяем безопасность короля после потенциального хода
                    if (game.canMove(new FigureInfo().getPosition(figure), c)) {
// добавляем в массив возможные ходы бота
                        ArrayList<Cell> rescue = new ArrayList<>();
                        rescue.add(new FigureInfo().getPosition(figure));
                        rescue.add(c);
                        botMoves.add(rescue);
                    }
        return botMoves;
    }


    /**
     * выбирает лучший ход, на основе минмакс алгоритма, просчитывается сумма
     * очков доски
     * очки считаются как вес фигуры + очки за ее положение на доске
     * после каждого возможного хода вперед
     * и с минимальной суммой для черных
     *
     * @param virtualOpponent - нужно ли нам просчитывать на ход вперед
     *                        или нет
     * @return очки за ход с максимальной суммой для белых
     * и с минимальной суммой для черных
     */
    private Double selectBestMove(boolean virtualOpponent) {
        try {
            List<List<Cell>> moves = bot_rescue();
            //начинаем проверять рокировку
            List shortCastling = new ArrayList<Cell>();
            if (game.checkCastling(game.shortKingX, game.shortRookX)) {
                shortCastling.add(game.getKingPos());
                shortCastling.add(new Cell(game.shortKingX, game.getKingPos().getY()));
            }

            if (shortCastling.size() != 0)
                moves.add(shortCastling);

            List longCastling = new ArrayList<Cell>();
            if (game.checkCastling(game.longKingX, game.longRookX)) {
                longCastling.add(game.getKingPos());
                longCastling.add(new Cell(game.longKingX, (game.getKingPos().getY())));
            }

            if (longCastling.size() != 0)
                moves.add(longCastling);

            List<Double> opponentScore = new ArrayList<>();
            for (List<Cell> l : moves) {
                Piece savedFigure = game.getBoardDirector().getFigure(l.get(1));
                game.changePlayer();
                game.updateGame(l.get(0), l.get(1), null);
                if (!game.kingProtected(game.getKingPos())) {
                    isMate = game.checkMate(game.attack);
                    if (isMate) {
                        game.changePlayer();
//                        if ((game.getBoardDirector().getFigure(l.get(1)) instanceof King) && (Math.abs(l.get(1).getX() - l.get(0).getX()) > 1)) {
//                        } else {
                            game.updateGame(l.get(1), l.get(0), null);
                            game.getBoardDirector().restoreFigure(savedFigure, l.get(1));
//                        }
                        double mateScore = 900;
                        weightedMoves.put(mateScore, l);
                        return mateScore;
                    }
                }
                if (game.checkDraw()) {
                    isDraw = true;
                    game.changePlayer();
                    game.updateGame(l.get(1), l.get(0), null);
                    double drawScore = 800;
                    weightedMoves.put(drawScore, l);
                    return drawScore;
                }
//                //если мы не считали xод за противника
                if (virtualOpponent) {
//                    //посчитаем ход за противника
//                    //сумму очков за сделанный ход и "лучший", по нашим критериям, ход противника
                    weightedMoves.put(game.getBoardDirector().score
                            + selectBestMove(false), l);
//                    opponent_rescue.clear();
                } else
//                //если мы как раз считаем за противника
                {
                    opponentScore.add(game.getBoardDirector().score);
                }
//            }
//            //после каждого просчитанного вперед хода откатываемся обратно

                game.updateGame(l.get(1), l.get(0), null);
                game.changePlayer();
                game.getBoardDirector().restoreFigure(savedFigure, l.get(1));
                isDraw = isMate = null;
            }
//        //если считаем за противника
            if (!virtualOpponent) {
                if (game.getCurrentPlayer().getColor() == Colors.WHITE)
                    return Collections.max(opponentScore);
                else
                    return Collections.min(opponentScore);
            } else {
                if (game.getCurrentPlayer().getColor() == Colors.WHITE)
                    return (Collections.max(weightedMoves.keySet()));
                else
                    return (Collections.min(weightedMoves.keySet()));

            }
        }
        catch (Exception e)
        {e.printStackTrace();}
return 0.0;
    }

    List<Cell> getBestMove() {
        weightedMoves.clear();

        return weightedMoves.get(selectBestMove(true));
    }
}
