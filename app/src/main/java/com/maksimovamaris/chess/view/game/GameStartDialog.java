package com.maksimovamaris.chess.view.game;

import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.presenter.AddGameView;
import com.maksimovamaris.chess.presenter.GamePresenter;
import com.maksimovamaris.chess.presenter.GamePresenterHolder;


public class GameStartDialog extends DialogFragment {
    private Button startPlayButton;
    private RadioGroup colorGroup;
    private RadioButton whiteButton;
    private RadioButton blackButton;
    private EditText gameName;
    private EditText playerName;
    private Switch switchBot;
    private boolean botWhite = false;
    private boolean botBlack = false;
    private AddGameView addGameView;
    private GamePresenter presenter;

    @Override
    public void onStart() {
        super.onStart();
        presenter = ((GamePresenterHolder) (getContext().getApplicationContext())).getGamePresenter();
        presenter.setGameView(addGameView);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof AddGameView) {
            addGameView = (AddGameView) context;
        } else {
            addGameView = (AddGameView) getParentFragment();
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.frament_start, null);
        startPlayButton = view.findViewById(R.id.play_button);
        whiteButton = view.findViewById(R.id.button_white);
        whiteButton.setOnClickListener(radioClickListener);
        blackButton = view.findViewById(R.id.button_black);
        blackButton.setOnClickListener(radioClickListener);
        colorGroup = view.findViewById(R.id.color_group);
        gameName = view.findViewById(R.id.game_name);
        playerName = view.findViewById(R.id.player_name);
        switchBot = view.findViewById(R.id.switch_bot);
        switchBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchBot.isChecked()) {
                    colorGroup.setVisibility(View.VISIBLE);
                    botWhite = true;
                    botBlack = false;

                } else {
                    colorGroup.setVisibility(View.GONE);
                    botBlack = botWhite = false;
                }
            }
        });

        startPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendResult(gameName.getText().toString(),
                        playerName.getText().toString(), botWhite, botBlack);
            }
        });

        builder.setView(view);
        return builder.create();
    }

    View.OnClickListener radioClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton) v;
            switch (rb.getId()) {
                case (R.id.button_black):
                    botBlack = true;
                    botWhite = false;
                    break;
                case (R.id.button_white):
                    botBlack = false;
                    botWhite = true;
                    break;
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if (switchBot.isChecked()) {
            colorGroup.setVisibility(View.VISIBLE);
            botWhite = true;
            botBlack = false;

        } else {
            colorGroup.setVisibility(View.GONE);
            botBlack = botWhite = false;
        }

    }
}
