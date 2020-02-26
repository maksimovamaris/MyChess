package com.maksimovamaris.chess.view.games;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.data.GameData;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.repository.GamesRepositoryImpl;
import com.maksimovamaris.chess.repository.RepositoryHolder;
import com.maksimovamaris.chess.utils.Runner;

import java.util.List;

public class GameListFragment extends Fragment {
    private GamesRepositoryImpl repository;
    private LiveData<List<GameData>> gameData;
    private View root;
    private RecyclerView gamesRecyclerView;
    private Runner runner;
@Override
public void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    //получаем раннер
    runner = ((GameHolder) (getContext().getApplicationContext())).getRunner();
    runner.runInBackground(() -> {
        //получаем репозиторий
        repository = ((RepositoryHolder) (getContext().getApplicationContext())).getRepository();
        gameData = repository.loadFromDatabase();
        runner.runOnMain(() -> {
            //в основном потоке прикрепляем адаптер со считанным из базы списком партий
            updateView(gameData);
        });
    });
}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gamelist, container, false);
        gamesRecyclerView = root.findViewById(R.id.game_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        gamesRecyclerView.setLayoutManager(linearLayoutManager);
        return root;
    }


    private void updateView(LiveData<List<GameData>> games) {
        gameData = games;
        gameData.observe(getViewLifecycleOwner(), new Observer<List<GameData>>() {
            @Override
            public void onChanged(List<GameData> gamesList) {
                Log.d("Games", "size() = " + gamesList.size());
                GameListAdapter adapter = new GameListAdapter(gamesList, R.drawable.ic_board);
                gamesRecyclerView.setAdapter(adapter);
            }
        });

    }

}
