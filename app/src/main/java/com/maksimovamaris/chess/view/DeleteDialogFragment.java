package com.maksimovamaris.chess.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.data.DateConverter;
import com.maksimovamaris.chess.game.action.GameHolder;
import com.maksimovamaris.chess.repository.GamesRepositoryImpl;
import com.maksimovamaris.chess.repository.RepositoryHolder;
import com.maksimovamaris.chess.utils.Runner;

public class DeleteDialogFragment extends DialogFragment {
    private Runner runner;
    private GamesRepositoryImpl repository;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        runner = ((GameHolder) (getContext().getApplicationContext())).getRunner();
        String title = getArguments().getString(getResources().getString(R.string.delete_title));
        long date = getArguments().getLong(getResources().getString(R.string.delete_parameter));
        String button1String = getResources().getString(R.string.dialog_yes);
        String button2String = getResources().getString(R.string.dialog_cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            //если да, удаляем запись об игре
            public void onClick(DialogInterface dialog, int id) {
                runner.runInBackground(() -> {
                    repository = ((RepositoryHolder) (getContext().getApplicationContext())).getRepository();
                    repository.deleteGameByDate(new DateConverter().toDate(date));
                });
            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getContext(), "Deletion Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }
}
