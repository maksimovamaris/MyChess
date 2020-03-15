package com.maksimovamaris.chess.view.notation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.data.MoveData;

import java.util.ArrayList;
import java.util.List;

public class GameNotationAdapter extends RecyclerView.Adapter<GameNotationAdapter.GameNotationViewHolder> {
    private ArrayList<MoveData> movesDataset;

    public static class GameNotationViewHolder extends RecyclerView.ViewHolder {
        private TextView move;

        public GameNotationViewHolder(@NonNull View v) {
            super(v);
            move = v.findViewById(R.id.move_text);
        }
    }

    public GameNotationAdapter(@NonNull  List<MoveData> moves) {
        movesDataset = (ArrayList) (moves);
    }

    @NonNull
    @Override
    public GameNotationAdapter.GameNotationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.move_item, viewGroup, false);
        GameNotationAdapter.GameNotationViewHolder gameNotationViewHolder = new GameNotationViewHolder(v);
        return gameNotationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameNotationAdapter.GameNotationViewHolder holder, int position) {
        String figure;
        String alphabet = "abcdefgh";
        if (movesDataset.get(position).getFigire_name().equals("KNIGHT"))
            figure = "N";
        else
        if (movesDataset.get(position).getFigire_name().equals("PAWN"))
            figure = "";
        else
            figure = String.valueOf(movesDataset.get(position).getFigire_name().charAt(0));
        //кто ходит
        String moveText = position + ". " + figure + " "
                //откуда ходит
                + alphabet.charAt(movesDataset.get(position).getX0())
                //к координатам по оси у не забываем +1
                // т к шахматная доска не нумеруется с 0
                + (movesDataset.get(position).getY0()+1)
                + " - "
                //куда ходит
                + alphabet.charAt(movesDataset.get(position).getX1())
                + (movesDataset.get(position).getY1()+1);

        holder.move.setText(moveText);
    }

    @Override
    public int getItemCount() {
        return movesDataset.size();
    }
}

