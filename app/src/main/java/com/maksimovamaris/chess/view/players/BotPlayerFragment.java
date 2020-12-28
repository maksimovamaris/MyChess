package com.maksimovamaris.chess.view.players;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.presenter.BotView;
import com.maksimovamaris.chess.presenter.GamePresenter;
import com.maksimovamaris.chess.presenter.GamePresenterHolder;

public class BotPlayerFragment extends PlayerContainer{
    private Spinner botSpinner;
    private String level;
    private GamePresenter presenter;
    private BotView botView;
    private Button bot_apply_button;
    private TextView botDescr;
    private ImageView botImage;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BotView) {
            botView = (BotView) context;
        }
    }

    public static BotPlayerFragment newInstance() {
        BotPlayerFragment fragment = new BotPlayerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_bot, container, false);
        botDescr=view.findViewById(R.id.bot_text);
        botImage=view.findViewById(R.id.bot_image);
        presenter = ((GamePresenterHolder) (getContext().getApplicationContext())).getGamePresenter();
        presenter.setGameView(addGameView);
        presenter.setBotView(botView);
        bot_apply_button = view.findViewById(R.id.bot_apply_button);
        botSpinner = view.findViewById(R.id.bot_spinner);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.bot_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        botSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                String[] choose = getResources().getStringArray(R.array.bot_levels);
                level = choose[selectedItemPosition];
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        bot_apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setBotLevel(level);
                botImage.setBackgroundResource(presenter.imageForBotLevel(level));
            }
        });
// Вызываем адаптер
        botSpinner.setAdapter(adapter);
        botSpinner.setSelection(presenter.getBotLevelArrayItemPos());
        Log.d("Selected item",""+botSpinner.getSelectedItem());
        botImage.setBackgroundResource(presenter.imageForBotLevel((String)(botSpinner.getSelectedItem())));
        return view;
    }

}


