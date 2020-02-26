package com.maksimovamaris.chess.view;

import com.maksimovamaris.chess.game.figures.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.maksimovamaris.chess.game.action.Cell;
import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.game.action.Game;

import java.util.LinkedList;
import java.util.List;

public class BoardView extends View {
    private static final float STROKE_WIDTH = 2.0f;
    private static final float HINT_WIDTH = 5.0f;
    private static final float TEXT_SIZE = 26.0f;
    private Paint boardPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint hintPaint = new Paint();
    private int colorDark;
    private int colorLight;
    private Cell selection;
    private Game game;
    private FigureInfo info;

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setStrokeWidth(STROKE_WIDTH);
        hintPaint.setColor(Color.YELLOW);
        hintPaint.setStyle(Paint.Style.STROKE);
        hintPaint.setStrokeWidth(HINT_WIDTH);
        info = new FigureInfo();

    }

    public void setGame(Game g) {
        game = g;
        game.createGame(getContext());
    }

    public void setColors(int colorDark, int colorLight) {
        this.colorDark = colorDark;
        this.colorLight = colorLight;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        List<Cell> hintArray = new LinkedList<>();
        Cell c;
        float cellWidth = canvas.getWidth() / 9;
        String letters = getResources().getString(R.string.letters);
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                c = new Cell(x, y);
                //рисование надписей
                if (x == 0 || y == 0) {
                    if (x == 0 && y != 0) {
                        drawCapture(String.valueOf(y), c, canvas, cellWidth);
                    } else if (x != 0 && y == 0) {
                        drawCapture(String.valueOf(letters.charAt(x - 1)), c, canvas, cellWidth);

                    }
                }
                //рисование доски
                else {
                    if ((x + y) % 2 == 0) //если координаты одинаковой четности
                        boardPaint.setColor(colorDark);//черная клетка
                    else
                        boardPaint.setColor(colorLight);//белая клетка
                    Cell c1 = new Cell(x - 1, y - 1);
                    drawCell(c, canvas, cellWidth);

                    if (game.getBoardDirector().getFigure(c1) != null) {
                        //фигура рисуется поверх только что нарисованной клетки и должна бдыть всегда видна
                        drawFigure(game.getBoardDirector().getFigure(c1), canvas, c, cellWidth);
                        if (selection != null && selection.equals(c1)) {
                            if (game.getBoardDirector().getFigure(c1).getPossiblePositions(game.getBoardDirector()).size() != 0)
                                hintArray.addAll(game.getBoardDirector().getFigure(c1).getPossiblePositions(game.getBoardDirector()));
                            else
                                Toast.makeText(getContext(), info.getName(game.getBoardDirector().getFigure(c1)).toString().toLowerCase() + " can't move!", Toast.LENGTH_SHORT).show();

                        }
                    }


                }
            }
        }

        //рисуем хинты после рисования поля, чтобы они были видны
        if (hintArray.size() != 0)
            for (Cell possible : hintArray)
                drawHint(canvas, possible, cellWidth);

    }

    private void drawHint(Canvas canvas, Cell c, float cellWidth) {
        canvas.drawCircle((c.getX() + 1) * cellWidth + cellWidth * 0.5f,
                (7 - c.getY()) * cellWidth + cellWidth * 0.5f,
                cellWidth * 0.5f,
                hintPaint);

    }

    private void drawCapture(String capture, Cell c, Canvas canvas, float cellWidth) {

        canvas.drawText(capture, c.getX() * cellWidth + cellWidth / 2,
                (9 - c.getY()) * cellWidth - cellWidth / 2, textPaint);
    }


    private void drawCell(Cell c, Canvas canvas, float cellWidth) {
        canvas.drawRect((c.getX()) * cellWidth,
                (9 - c.getY()) * cellWidth,
                (c.getX() + 1) * cellWidth,
                (8 - c.getY()) * cellWidth, boardPaint);
    }

    private void drawFigure(ChessFigure figure, Canvas canvas, Cell c, float cellWidth) {

        Bitmap img = BitmapFactory.decodeResource(getResources(), new FigureInfo().getImageId(figure));
        img = Bitmap.createScaledBitmap(img, (int) (cellWidth), (int) (cellWidth), false);
        canvas.drawBitmap(img, cellWidth * (c.getX()), cellWidth * (8 - c.getY()), boardPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(Math.min(getMeasuredWidth(), getMeasuredHeight()),
                Math.min(getMeasuredWidth(), getMeasuredHeight()));
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        float cellWidth = getWidth() / 9;
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            int x = (int) (event.getX() / cellWidth) - 1;
            int y = 7 - (int) (event.getY() / cellWidth);
            Cell c = new Cell(x, y);

            if (c.isCorrect())//ячейка существует
            {
                game.viewAction(selection, c);

            } else
                selection = null;

        }
        return true;
    }


    public void updateView(Cell c) {
        selection = c;
        invalidate();
    }

    public void printMessage(String message) {
        //для теста
    }
}

