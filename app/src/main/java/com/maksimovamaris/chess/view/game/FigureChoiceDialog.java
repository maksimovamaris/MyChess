package com.maksimovamaris.chess.view.game;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.game.action.FigureChoiceListener;

public class FigureChoiceDialog extends DialogFragment {
    private String newFigure;
    private FigureChoiceListener listener;
    private Spinner figureSpinner;

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
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 0;
        window.setAttributes(params);
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select figure");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_choice, null);
        builder.setView(view);
        figureSpinner = view.findViewById(R.id.figure_spinner);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.figures, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        figureSpinner.setAdapter(adapter);
        figureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                String[] choose = getResources().getStringArray(R.array.figures);
                newFigure = choose[selectedItemPosition];
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                listener.onChoiceMade(newFigure);
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        newFigure=savedInstanceState.getString(getResources().getString(R.string.key_saved_newFigure));
    }
}
