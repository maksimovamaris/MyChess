package com.maksimovamaris.chess;

import androidx.appcompat.app.AppCompatActivity;

import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.maksimovamaris.chess.preferences.PreferencesActivity;
import com.maksimovamaris.chess.view.BoardView;


public class MainActivity extends AppCompatActivity {
    private BoardView mBoardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBoardView = findViewById(R.id.board_view);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chess_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_settings):
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString(
                getString(R.string.color_pref_key),
                getString(R.string.color_def_value)
        );
        switch (theme) {
            case "0":
                mBoardView.setColors(getResources().getColor(R.color.cellDarkWarm), getResources().getColor(R.color.cellLightWarm));
                break;
            case "1":
                mBoardView.setColors(getResources().getColor(R.color.cellDarkCool), getResources().getColor(R.color.cellLightCool));
                break;
            case "2":
                mBoardView.setColors(getResources().getColor(R.color.cellDarkMint), getResources().getColor(R.color.cellLightMint));
                break;

        }
        super.onStart();
    }
}
