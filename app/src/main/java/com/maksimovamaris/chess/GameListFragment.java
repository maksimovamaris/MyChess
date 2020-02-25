package com.maksimovamaris.chess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class GameListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gamelist, container, false);


      List <GamesRepository> gamesRepository=new ArrayList<>();
      gamesRepository.add(new GamesRepository());
        RecyclerView gameList= root.findViewById(R.id.game_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        gameList.setLayoutManager(linearLayoutManager);
        MyListAdapter adapter=new MyListAdapter(gamesRepository,  R.drawable.ic_board);

        gameList.setAdapter(adapter);
        return root;

    }
}
