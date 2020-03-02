package com.maksimovamaris.chess.view.games;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.data.DateConverter;
import com.maksimovamaris.chess.game.action.Game;
import com.maksimovamaris.chess.game.action.GameEndListener;
import com.maksimovamaris.chess.game.action.GameHelper;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.game.action.GameLocker;

import java.util.Date;


public class GameActivity extends AppCompatActivity implements GameEndListener, GameLocker {

    private BoardView boardView;
    private Game game;
    private Date savedDate;
    private Date gameDate;
    private View lockView;
    private Bundle savedState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedState = savedInstanceState;
        setContentView(R.layout.activity_game);
        lockView = findViewById(R.id.shadow_view);
        boardView = findViewById(R.id.board_view);
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
        state.putLong(getResources().getString(R.string.key_save_gameState), (new DateConverter()).fromDate(game.getBoardDirector().getDate()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedDate = new DateConverter().toDate(savedInstanceState.getLong(getResources().getString(R.string.key_save_gameState)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (savedDate != null) {
            gameDate = savedDate;
            game.attachView(boardView, gameDate);
        } else {
            Bundle arguments = getIntent().getExtras();
            if (arguments != null) {
                gameDate = (Date) (arguments.get(getResources().getString(R.string.key_game)));
            }
        }

        game.setGameEndListener(this);
        game.setLocker(this);
        game.attachView(boardView, gameDate);
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
    }

    /**
     * для бота
     */
    @Override
    public void lock() {
        lockView.setVisibility(View.VISIBLE);
    }
}

