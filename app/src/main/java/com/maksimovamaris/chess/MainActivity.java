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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.maksimovamaris.chess.preferences.PreferencesActivity;
import com.maksimovamaris.chess.presenter.AddGameView;
import com.maksimovamaris.chess.presenter.RestoreGameView;
import com.maksimovamaris.chess.view.games.GameActivity;
import com.maksimovamaris.chess.view.games.GameStartDialog;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements AddGameView, RestoreGameView {
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
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);


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
        FragmentManager manager=getSupportFragmentManager();
        GameStartDialog dialog=(GameStartDialog)(manager.findFragmentByTag(getResources().getString(R.string.tag_start_game)));
        if(dialog!=null)
        dialog.getDialog().dismiss();
    }

    @Override
    public void onError(String name) {
        Toast.makeText(this, name+" > 10 signs", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGameRestored(Date date, String human, String bot) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(getString(R.string.game_date),date);
        intent.putExtra(getString(R.string.recycler_human),human);
        intent.putExtra(getString(R.string.recycler_bot),bot);
        startActivity(intent);
    }
}

