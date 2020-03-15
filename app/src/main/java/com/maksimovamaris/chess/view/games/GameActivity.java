package com.maksimovamaris.chess.view.games;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.data.DateConverter;
import com.maksimovamaris.chess.game.action.FigureChoiceListener;
import com.maksimovamaris.chess.game.action.Game;
import com.maksimovamaris.chess.game.action.GameEndListener;
import com.maksimovamaris.chess.game.action.GameHelper;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.game.action.GameLocker;
import com.maksimovamaris.chess.game.figures.Colors;


import java.util.Date;

public class GameActivity extends AppCompatActivity implements FigureChoiceListener, GameEndListener, GameLocker {

    private BoardView boardView;
    private Game game;
    private Date savedDate;
    private Date gameDate;
    private View lockView;
    private String gameName;
    private String humanPlayer;
    private String botPlayer;
    private TextView player1Text;
    private TextView player2Text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        lockView = findViewById(R.id.shadow_view);
        boardView = findViewById(R.id.board_view);
        player1Text = findViewById(R.id.player1_text);
        player2Text = findViewById(R.id.player2_text);
        //получаем игру
        game = ((GameHolder) (getApplication())).getGame();
        gameDate = null;

    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString(
                getString(R.string.color_pref_key),
                getString(R.string.color_def_value)
        );
        switch (theme) {
            case "0":
                boardView.setColors(getResources().getColor(R.color.cellDarkWarm), getResources().getColor(R.color.cellLightWarm));
                break;
            case "1":
                boardView.setColors(getResources().getColor(R.color.cellDarkCool), getResources().getColor(R.color.cellLightCool));
                break;
            case "2":
                boardView.setColors(getResources().getColor(R.color.cellDarkMint), getResources().getColor(R.color.cellLightMint));
                break;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putLong(getResources().getString(R.string.key_save_gameDate), (new DateConverter()).fromDate(game.getBoardDirector().getDate()));
        state.putString(getString(R.string.key_save_gameHuman), player1Text.getText().toString());
        state.putString(getResources().getString(R.string.key_save_gameBot), player2Text.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedDate = new DateConverter().toDate(savedInstanceState.getLong(getResources().getString(R.string.key_save_gameDate)));
        humanPlayer = savedInstanceState.getString(getResources().getString(R.string.key_save_gameHuman));
        botPlayer = savedInstanceState.getString(getResources().getString(R.string.key_save_gameBot));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //в случае восстановления активити
        if (savedDate != null) {
            gameDate = savedDate;

        } else {
            Bundle arguments = getIntent().getExtras();
            if (arguments != null) {

                gameDate = (Date) (arguments.get(getResources().getString(R.string.game_date)));
                gameName = (String) (arguments.get(getResources().getString(R.string.game_name)));

                //получаем из диалога
                humanPlayer = (String) (arguments.get(getResources().getString(R.string.dialog_human)));
                botPlayer = (String) (arguments.get(getResources().getString(R.string.dialog_bot)));
                //если не удалось получить их из диалога, значит мы кликнули на item recyclerView
                //а там другой ключ у интента
                if (botPlayer == null || humanPlayer == null) {
                    humanPlayer = (String) (arguments.get(getResources().getString(R.string.recycler_human)));
                    botPlayer = (String) (arguments.get(getResources().getString(R.string.recycler_bot)));
                }
            }
        }

        game.setGameEndListener(this);
        game.setLocker(this);
        game.setFigureChoiceListener(this);
        game.initGame(getBaseContext());
        if (gameDate == null)
            game.createGame(gameName, humanPlayer, botPlayer);
        else
            game.restoreGame(gameDate);
        //после того, как игра приобрела нужное состояние,
        //прикрепляем ее к view

        game.attachView(boardView);
        if (botPlayer == null || humanPlayer == null) {
            botPlayer = game.getBoardDirector().getBotPlayer();
            humanPlayer = game.getBoardDirector().getHumanPlayer();
        }


        if (botPlayer.equals(Colors.WHITE.toString())) {
            player1Text.setText("Bot");
            player2Text.setText(humanPlayer);
        } else if (botPlayer.equals(Colors.BLACK.toString())) {
            player1Text.setText(humanPlayer);
            player2Text.setText("Bot");
        } else {
            player1Text.setText(humanPlayer);
            player2Text.setText(humanPlayer);
        }


    }

    /**
     * когда пользователь вышел из игры запоминаем,
     * кто сделал последний ход
     * чтобы отобразить это в адаптере
     */
    @Override
    protected void onDestroy() {
        game.updateTurn();
        super.onDestroy();

    }

    @Override
    public void endGame(String result) {
        FragmentManager manager = getSupportFragmentManager();
        GameEndDialog gameEndDialog = new GameEndDialog();
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.game_result), result);
        gameEndDialog.setArguments(bundle);
        gameEndDialog.setGameNotationListener(new GameHelper(getApplicationContext()));
        gameEndDialog.show(manager, getResources().getString(R.string.dialog_show));
        game.detachView();
//        gameEndDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * для бота
     */
    @Override
    public void lock() {
        lockView.setVisibility(View.VISIBLE);
    }

    @Override
    public void unlock() {
        lockView.setVisibility(View.GONE);
    }

    @Override
    public void onChoiceStarted() {
        FragmentManager manager = getSupportFragmentManager();
        FigureChoiceDialog dialog = new FigureChoiceDialog();
        dialog.show(manager, getResources().getString(R.string.choice_dialog));
    }

    @Override
    public void onChoiceMade(String figureName) {
        game.pawnTurning(figureName);
    }
}

