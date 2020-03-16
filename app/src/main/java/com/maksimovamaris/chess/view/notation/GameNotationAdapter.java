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

    public GameNotationAdapter(@NonNull List<MoveData> moves) {
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
        String moveText = "";
        if (position % 2 == 0) {
            moveText = ((position+2)/2)+".   "+setNotation(movesDataset.get(position));
            if ((position + 1) < movesDataset.size())
                moveText = moveText + "   " +setNotation(movesDataset.get(position + 1));
        }

        holder.move.setText(moveText);
    }

    private String setNotation(MoveData data) {
        String figure;
        String newFigure = "";
        String alphabet = "abcdefgh";
        figure = setFigureName(data.getFigureName());
        if (data.getNewFigureName().length() != 0)
            newFigure = setFigureName(data.getNewFigureName());
        String moveText = "";
        //ход считается после обоюдного движения белых и черных
        //если это король
        if (figure.equals("K")&&(Math.abs(data.getX0() - data.getX1()) > 1)) {
            //длинная рокировка
            if ((data.getX0() - data.getX1()) > 1)
                moveText = moveText + "0-0-0";
            else
                //короткая рокировка
                if ((data.getX0() - data.getX1()) < -1)
                    moveText = moveText + "0-0";
        } else
            //кто ходит
            moveText = moveText + figure + " "
                    //откуда ходит
                    + alphabet.charAt(data.getX0())
                    //к координатам по оси у не забываем +1
                    // т к шахматная доска не нумеруется с 0
                    + (data.getY0() + 1)
                    + "" + data.getCapture() + ""
                    //куда ходит
                    + alphabet.charAt(data.getX1())
                    + (data.getY1() + 1)
                    + " " + newFigure + "" + data.getThreat();
        return moveText;
    }

    private String setFigureName(String name) {
        if (name.equals("KNIGHT"))
            return "N";
        else if (name.equals("PAWN"))
            return "";
        else
            return String.valueOf(name.charAt(0));
    }

    @Override
    public int getItemCount() {
        return movesDataset.size();
    }
}

