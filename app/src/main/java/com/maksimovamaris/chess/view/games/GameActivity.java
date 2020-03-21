package com.maksimovamaris.chess.view.games;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.data.DateConverter;
import com.maksimovamaris.chess.game.action.Cell;
import com.maksimovamaris.chess.game.action.FigureChoiceListener;
import com.maksimovamaris.chess.game.action.Game;
import com.maksimovamaris.chess.game.action.GameEndListener;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.game.action.GameLocker;
import com.maksimovamaris.chess.game.action.GameNotationListener;
import com.maksimovamaris.chess.game.pieces.Colors;


import java.util.Date;

public class GameActivity extends AppCompatActivity implements FigureChoiceListener, GameEndListener, GameLocker, GameNotationListener {

    private BoardView boardView;
    private Game game;
    private Date savedDate;
    private Date gameDate;
    private View lockView;
    private View spaceView;
    private String gameName;
    private String humanPlayer;
    private String botPlayer;
    private TextView playerWhite;
    private TextView playerBlack;
    private String savedResult;
    private Cell selected;
    private Cell moved;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        lockView = findViewById(R.id.shadow_view);
        spaceView = findViewById(R.id.space_view);
        boardView = findViewById(R.id.board_view);
        playerWhite = findViewById(R.id.player_white);
        playerBlack = findViewById(R.id.player_black);
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
        state.putString(getString(R.string.key_save_gameWhite), playerWhite.getText().toString());
        state.putString(getResources().getString(R.string.key_save_gameBlack), playerBlack.getText().toString());
        state.putString(getResources().getString(R.string.key_saved_result), savedResult);
        if (selected != null && moved != null) {
            state.putInt(getString(R.string.key_save_selectedX), selected.getX());
            state.putInt(getString(R.string.key_save_selectedY), selected.getY());
            state.putInt(getString(R.string.key_save_movedX), moved.getX());
            state.putInt(getString(R.string.key_save_movedY), moved.getY());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedDate = new DateConverter().toDate(savedInstanceState.getLong(getResources().getString(R.string.key_save_gameDate)));
        //если не успели закончить игру, сохраняем результатт игры, чтобы его восстановить
        savedResult = savedInstanceState.getString(getResources().getString(R.string.key_saved_result));
//восстановили координаты превращающейся пешки
        selected = new Cell(savedInstanceState.getInt(getResources().getString(R.string.key_save_selectedX)),
                savedInstanceState.getInt(getResources().getString(R.string.key_save_selectedY)));

        moved = new Cell(savedInstanceState.getInt(getResources().getString(R.string.key_save_movedX)),
                savedInstanceState.getInt(getResources().getString(R.string.key_save_movedY)));

        String white = savedInstanceState.getString(getResources().getString(R.string.key_save_gameWhite));
        String black = savedInstanceState.getString(getResources().getString(R.string.key_save_gameBlack));
        playerWhite.setText(white);
        playerBlack.setText(black);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
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
        if ((botPlayer != null) && (humanPlayer != null)) {
            if (botPlayer.equals(Colors.WHITE.toString())) {
                playerWhite.setText("Bot");
                playerBlack.setText(humanPlayer);
            } else if (botPlayer.equals(Colors.BLACK.toString())) {
                playerWhite.setText(humanPlayer);
                playerBlack.setText("Bot");
            } else {
                playerWhite.setText(humanPlayer);
                playerBlack.setText(humanPlayer);
            }
        }

        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.dialog_show)) != null) {
            spaceView.setVisibility(View.VISIBLE);
        }

        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.choice_dialog)) != null) {
            //сначала уничтожаем предыдущий диалогфрагмент
            ((DialogFragment) (getSupportFragmentManager().findFragmentByTag
                    (getResources().getString(R.string.choice_dialog)))).dismiss();

            onChoiceStarted(selected, moved);
        }
    }


    /**
     * когда пользователь вышел из игры запоминаем,
     * кто сделал последний ход
     * чтобы отобразить это в адаптере
     */
    @Override
    protected void onStop() {
        super.onStop();

        game.updateTurn();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void endGame(String result) {
        spaceView.setVisibility(View.VISIBLE);
        FragmentManager manager = getSupportFragmentManager();
        GameEndDialog gameEndDialog = new GameEndDialog();
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.game_result), result);
        gameEndDialog.setArguments(bundle);
        gameEndDialog.setRetainInstance(true);
        if (savedResult == null)
            gameEndDialog.show(manager, getResources().getString(R.string.dialog_show));
        this.savedResult = result;

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
    public void onChoiceStarted(Cell selected, Cell moved) {
        this.selected = selected;
        this.moved = moved;
        spaceView.setVisibility(View.VISIBLE);
        FragmentManager manager = getSupportFragmentManager();
        FigureChoiceDialog dialog = new FigureChoiceDialog();
        dialog.show(manager, getResources().getString(R.string.choice_dialog));
    }

    @Override
    public void onChoiceMade(String figureName) {
        spaceView.setVisibility(View.GONE);
        game.pawnTurning(selected, moved, figureName);
        selected = null;
        moved = null;
    }

    @Override
    public void modifyNotation(boolean notation) {
        //если нотация не нужна, удаляем игру
        if(!notation)
            game.clean();
        else
            game.setNotation(notation);
        //детачимся от вью только когда мы точно вышли из игры
        game.detachView();
    }
}