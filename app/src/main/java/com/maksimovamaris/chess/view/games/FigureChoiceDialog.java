package com.maksimovamaris.chess.view.games;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maksimovamaris.chess.game.action.FigureChoiceListener;
import com.maksimovamaris.chess.game.pieces.Figures;

public class FigureChoiceDialog extends DialogFragment {
    private String newFigure;
    private FigureChoiceListener listener;

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        if (context instanceof FigureChoiceListener) {
            listener = (FigureChoiceListener) context;
        } else {
            listener = (FigureChoiceListener) getParentFragment();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        newFigure = "QUEEN";

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] figureNamesArray = {Figures.KNIGHT.toString(), Figures.BISHOP.toString(),
                Figures.ROOK.toString(), Figures.QUEEN.toString()};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select figure")
                // добавляем переключатели по умолчанию выбран ферзь
                .setSingleChoiceItems(figureNamesArray, 3,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                newFigure = figureNamesArray[item];
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onChoiceMade(newFigure);
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog


                        dismiss();

                    }
                });

        return builder.create();
    }


}
