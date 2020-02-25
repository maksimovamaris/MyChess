package com.maksimovamaris.chess;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.maksimovamaris.chess.game.action.Game;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.view.BoardView;

public class GameActivity extends AppCompatActivity {

    private BoardView boardView;

    private Game game;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        boardView = findViewById(R.id.board_view);
        game = ((GameHolder) (getApplication())).getGame();
        game.attachView(boardView);


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
    protected void onStop() {
        game.detachView();
        super.onStop();
    }
}
