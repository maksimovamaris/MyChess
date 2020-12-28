package com.maksimovamaris.chess;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.maksimovamaris.chess.preferences.PreferencesActivity;
import com.maksimovamaris.chess.presenter.AddGameView;
import com.maksimovamaris.chess.presenter.BotView;
import com.maksimovamaris.chess.presenter.RestoreGameView;
import com.maksimovamaris.chess.view.game.GameActivity;
import com.maksimovamaris.chess.view.game.GameStartDialog;


import java.util.Date;

public class MainActivity extends AppCompatActivity implements AddGameView, RestoreGameView, BotView {
    private AppBarConfiguration mAppBarConfiguration;
    private GameStartDialog gameStartDialog;
    private FloatingActionButton addGameBut;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            Log.d("supportActionBar", "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }


        addGameBut = findViewById(R.id.fab);
        addGameBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameStartDialog = new GameStartDialog();
                gameStartDialog.show(fragmentManager, getResources().getString(R.string.tag_start_game));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_games, R.id.nav_gamerecords)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) findViewById(R.id.collapsing_toolbar_layout)
                .getLayoutParams();

        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL + AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        CollapsingToolbarLayout.LayoutParams collapseParams = (CollapsingToolbarLayout.LayoutParams) findViewById(R.id.image)
                .getLayoutParams();
        collapseParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);

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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onGameAdded(String gameName, String human, String bot) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(getResources().getString(R.string.game_name), gameName);
        intent.putExtra(getResources().getString(R.string.dialog_human), human);
        intent.putExtra(getResources().getString(R.string.dialog_bot), bot);
        startActivity(intent);
        detachGameAddDialog();

    }

    private void detachGameAddDialog() {
        FragmentManager manager = getSupportFragmentManager();
        GameStartDialog dialog = (GameStartDialog) (manager.findFragmentByTag(getResources().getString(R.string.tag_start_game)));
        if (dialog != null)
            dialog.getDialog().dismiss();
    }

    @Override
    public void onError(String name,String message) {
        Toast.makeText(this, name + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGameRestored(Date date, String human, String bot) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(getString(R.string.game_date), date);
        intent.putExtra(getString(R.string.recycler_human), human);
        intent.putExtra(getString(R.string.recycler_bot), bot);
        startActivity(intent);
    }

    @Override
    public void onLevelAdded(String level) {
        Toast.makeText(getApplicationContext(), "Bot level changed!", Toast.LENGTH_SHORT).show();
//        botImage.setBackgroundResource(presenter.imageForBotLevel(level));

//        public int imageForBotLevel(String level) {
//            if (getArrayIndex(level) == 0)
//                return R.drawable.bot_random;
//            else if (getArrayIndex(level) == 1)
//                return R.drawable.bot_fast_and_stupid;
//            else
//                return R.drawable.bot_thoughtful;
//        }


//        String[] array = getResources().getStringArray(R.array.bot_levels);
//        completely bot_random
//        if (level.equals(array[0])) {
//
//        } else if (level.equals(array[1])) {
//
//        } else if (level.equals(array[2])) {
    }


}

