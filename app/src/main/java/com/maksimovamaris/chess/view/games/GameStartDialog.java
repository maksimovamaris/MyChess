package com.maksimovamaris.chess.view.games;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.game.figures.Colors;


public class GameStartDialog extends DialogFragment {
    private Button startPlayButton;
    private RadioGroup colorGroup;
    private RadioButton whiteButton;
    private RadioButton blackButton;
    private EditText gameName;
    private EditText playerName;
    private Switch switchBot;
    private String game = "Untitled game";
    private String humanPlayer = "Unknown player";
    private String botPlayer = "";
    private boolean botWhite = false;
    private boolean botBlack = false;

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
                Intent intent = new Intent(getContext(), GameActivity.class);
                setGame();
                intent.putExtra(getResources().getString(R.string.game_name), game);
                setPlayer();
                if (botBlack) {
                    botPlayer = Colors.BLACK.toString();

                } else if (botWhite) {
                    botPlayer = Colors.WHITE.toString();
                }
                intent.putExtra(getResources().getString(R.string.dialog_human), humanPlayer);
                intent.putExtra(getResources().getString(R.string.dialog_bot), botPlayer);
                getDialog().dismiss();
                startActivity(intent);

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

    private void setPlayer() {
        if (!playerName.getText().toString().equals(""))
            humanPlayer = playerName.getText().toString();

    }


    private void setGame() {
        if (!gameName.getText().toString().equals(""))
            game = gameName.getText().toString();
    }

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
