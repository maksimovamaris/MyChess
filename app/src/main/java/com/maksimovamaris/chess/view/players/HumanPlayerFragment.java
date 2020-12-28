package com.maksimovamaris.chess.view.players;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.presenter.GamePresenter;
import com.maksimovamaris.chess.presenter.GamePresenterHolder;


public class HumanPlayerFragment extends PlayerContainer {
    private TextView playerName;
    private EditText playerEdit;
    private Button applyButton;
    private GamePresenter presenter;

    public static HumanPlayerFragment newInstance() {
        HumanPlayerFragment fragment = new HumanPlayerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_player, container, false);

        presenter = ((GamePresenterHolder) (getContext().getApplicationContext())).getGamePresenter();
        presenter.setGameView(addGameView);

        playerName = view.findViewById(R.id.apply_player_name);
        playerName.setText(getResources().getString(R.string.your_name_is) + " " + presenter.getHumanName());

        playerEdit = view.findViewById(R.id.edit_player_name);
        applyButton = view.findViewById(R.id.player_apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //удаляем все пробелы в начале и конце
                String name = playerEdit.getText().toString().trim();
                presenter.setHumanName(name);
                playerName.setText(getResources().getString(R.string.your_name_is) + " " + presenter.getHumanName());
                playerEdit.setText("");
            }
        });
        return view;
    }

}
