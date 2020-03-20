package com.maksimovamaris.chess.view.notation;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.data.MoveData;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.game.pieces.Colors;
import com.maksimovamaris.chess.repository.GamesRepositoryImpl;
import com.maksimovamaris.chess.repository.RepositoryHolder;
import com.maksimovamaris.chess.utils.Runner;

import java.util.Date;
import java.util.List;


public class GameNotationActivity extends Activity {
    private GamesRepositoryImpl repository;
    private Runner runner;
    private List<MoveData> moves;
    private Date gameDate;
    private RecyclerView notationView;
    private GameNotationAdapter adapter;
    private boolean whiteFront;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        whiteFront = true;
        runner = ((GameHolder) (getApplicationContext())).getRunner();
        Bundle arguments = getIntent().getExtras();
        gameDate = (Date) (arguments.get(getResources().getString(R.string.key_notation)));
        runner.runInBackground(() -> {
            //получаем репозиторий
            repository = ((RepositoryHolder) (getApplicationContext())).getRepository();
            moves = repository.getGameNotation(gameDate);
            if (repository.getGameByDate(gameDate).getBot_player().equals(Colors.WHITE.toString()))
                whiteFront = false;
            runner.runOnMain(() -> {
                //в основном потоке прикрепляем адаптер со считанным из базы списком ходов
                notationView = findViewById(R.id.game_list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                notationView.setLayoutManager(linearLayoutManager);
                adapter = new GameNotationAdapter(moves, whiteFront);
                notationView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
        });
    }
}


