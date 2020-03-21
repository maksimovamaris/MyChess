package com.maksimovamaris.chess.view.games;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.game.action.GameNotationListener;

public class GameEndDialog extends DialogFragment {
    private String result;
    private GameNotationListener gameNotationListener;
    private boolean choice;


    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        if (context instanceof GameNotationListener) {
            gameNotationListener = (GameNotationListener) context;
        } else {
            gameNotationListener = (GameNotationListener) getParentFragment();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.TOP);
        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 0;
        window.setAttributes(params);
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] choiseArray = {"Save game notation"};
        result = getArguments().getString(getResources().getString(R.string.game_result));
        choice = true;
        final boolean[] checkedItemsArray = {true};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(result)
                .setMultiChoiceItems(choiseArray, checkedItemsArray,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                checkedItemsArray[which] = isChecked;
                            }
                        })
                .setPositiveButton("Exit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                //записываем или не записываем игру, в зависимости от флажка
                                for (int i = 0; i < choiseArray.length; i++) {
                                    {
                                        choice = checkedItemsArray[i];
                                        gameNotationListener.modifyNotation(choice);
                                    }
                                }
                                //выходим из активности в любом случае
                                getActivity().finish();

                            }
                        });


        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,
                                 android.view.KeyEvent event) {

                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN)
                        return true;
                    else {
                        gameNotationListener.modifyNotation(choice);
                        getActivity().finish();
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }

}
