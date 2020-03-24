package com.maksimovamaris.chess.view.records;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maksimovamaris.chess.OnBackPressedListener;
import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.data.DateConverter;
import com.maksimovamaris.chess.data.GameData;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.repository.GamesRepositoryImpl;
import com.maksimovamaris.chess.repository.RepositoryHolder;
import com.maksimovamaris.chess.utils.Runner;
import com.maksimovamaris.chess.view.DeleteDialogFragment;
import com.maksimovamaris.chess.view.GameListAdapter;
import com.maksimovamaris.chess.view.RecyclerTouchListener;
import com.maksimovamaris.chess.view.games.GameListFragment;
import com.maksimovamaris.chess.view.notation.GameNotationActivity;


import java.util.List;

public class RecordsListFragment extends Fragment implements OnBackPressedListener {
    private GamesRepositoryImpl repository;
    private LiveData<List<GameData>> gameData;
    private View root;
    private RecyclerView gamesRecyclerView;
    private Runner runner;
    private GameListAdapter adapter;
    private TextView noRecords;


    @Override
    public void onBackPressed() {
        getActivity().onBackPressed();
        getChildFragmentManager().popBackStack();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //получаем раннер
        runner = ((GameHolder) (getContext().getApplicationContext())).getRunner();
        runner.runInBackground(() -> {
            //получаем репозиторий
            repository = ((RepositoryHolder) (getContext().getApplicationContext())).getRepository();
            gameData = repository.getGameOrRecord(true);
            runner.runOnMain(() -> {
                //в основном потоке прикрепляем адаптер со считанным из базы списком партий
                updateView(gameData);
            });
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_list, container, false);
        gamesRecyclerView = root.findViewById(R.id.game_list);
        noRecords = root.findViewById(R.id.empty_list_text);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        gamesRecyclerView.setLayoutManager(linearLayoutManager);
        gamesRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                gamesRecyclerView, new GameListFragment.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(getContext(), GameNotationActivity.class);
                intent.putExtra(getString(R.string.key_notation), gameData.getValue().get(position).getGame_date());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(getResources().getString(R.string.delete_title), "Do you want to delete Notation?");
                bundle.putLong(getResources().getString(R.string.delete_parameter),
                        new DateConverter().fromDate(gameData.getValue().get(position).getGame_date()));
                FragmentManager manager = getChildFragmentManager();
                DeleteDialogFragment deleteDialog = new DeleteDialogFragment();
                deleteDialog.setArguments(bundle);
                deleteDialog.show(manager, getResources().getString(R.string.delete_dialog_show));

            }
        }));
        return root;
    }


    private void updateView(LiveData<List<GameData>> games) {
        gameData = games;

        noRecords.setVisibility(View.GONE);
        if (getView() != null) {
            gameData.observe(getViewLifecycleOwner(), new Observer<List<GameData>>() {
                @Override
                public void onChanged(List<GameData> gamesList) {
                    Log.d("Games", "size() = " + gamesList.size());
                    if (gamesList.size() == 0) {
                        noRecords.setVisibility(View.VISIBLE);
                        noRecords.setText(R.string.no_records);
                    } else {
                        noRecords.setVisibility(View.GONE);
                    }
                    adapter = new GameListAdapter(gamesList, R.drawable.ic_description, "winner");
                    adapter.notifyDataSetChanged();
                    gamesRecyclerView.setAdapter(adapter);
                }
            });
        }
//        }
    }
}


