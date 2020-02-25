package com.maksimovamaris.chess;

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

import com.maksimovamaris.chess.data.GameData;
import com.maksimovamaris.chess.presenter.GamesRepository;
import com.maksimovamaris.chess.presenter.RepositoryListener;


import java.util.List;


public class GameListFragment extends Fragment implements RepositoryListener {
    private GamesRepository repository;
    private LiveData<List<GameData>> gameData;
    private View root;
    private RecyclerView gamesRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gamelist, container, false);
        gamesRecyclerView = root.findViewById(R.id.game_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        gamesRecyclerView.setLayoutManager(linearLayoutManager);
        repository = new GamesRepository(this);
        repository.loadFromDatabase(getContext());
        //происходит действие со списком при обновлении базы данных

        return root;
    }

    @Override
    public void updateView(LiveData<List<GameData>> games) {
        gameData = games;
        gameData.observe(this, new Observer<List<GameData>>() {
            @Override
            public void onChanged(List<GameData> gamesList) {
                Log.d("Games", "size() = " + gamesList.size());
                GameListAdapter adapter = new GameListAdapter(gamesList, R.drawable.ic_board);
                gamesRecyclerView.setAdapter(adapter);
            }
        });

    }
}
