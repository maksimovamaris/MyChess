package com.maksimovamaris.chess.view;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;


import androidx.recyclerview.widget.RecyclerView;

import com.maksimovamaris.chess.R;
import com.maksimovamaris.chess.data.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameListViewHolder> {
    private ArrayList<GameData> gamesDataset;
    private int imageId;

    private String title;

    public GameListAdapter(List<GameData> games, @DrawableRes int image, String title) {
        this.title = title;

        gamesDataset = (ArrayList) (games);
        imageId = image;
    }


    public class GameListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView gameTime;
        private TextView gameTitle;
        private TextView gameWinner;
        private TextView player1;
        private TextView player2;
        private ImageView gameImage;



        public GameListViewHolder(View v) {

            super(v);
            gameTime = v.findViewById(R.id.game_time);
            gameImage = v.findViewById(R.id.game_icon);
            gameTitle = v.findViewById(R.id.game_title);
            gameTitle=v.findViewById(R.id.game_title);
            gameWinner=v.findViewById(R.id.winner);
            player1=v.findViewById(R.id.player1);
            player2=v.findViewById(R.id.player2);


        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public GameListAdapter.GameListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        GameListViewHolder gameListViewHolder = new GameListViewHolder(v);
        return gameListViewHolder;
    }

    @Override
    public int getItemCount() {
        return gamesDataset.size();
    }

    @Override
    public void onBindViewHolder(@NonNull GameListViewHolder holder, int position) {
        String color;
        holder.gameTime.setText(gamesDataset.get(position).getGame_date().toString());
        if (title == "winner") {
            if (gamesDataset.get(position).getTurn().toLowerCase().equals("white"))
                color = "black";
            else
                color = "white";
        } else
            color = gamesDataset.get(position).getTurn().toLowerCase();
        holder.gameWinner.setText(color + "      " + title);
        holder.gameTitle.setText(gamesDataset.get(position).getName());
        if(gamesDataset.get(position).getBot_player().toLowerCase().equals("white"))
        {holder.player1.setText("Bot");
        holder.player2.setText(gamesDataset.get(position).getHuman_player());}
        if(gamesDataset.get(position).getBot_player().toLowerCase().equals("black"))
        {
            holder.player2.setText("Bot");
            holder.player1.setText(gamesDataset.get(position).getHuman_player());
        }
        if(gamesDataset.get(position).getBot_player().toLowerCase().equals(""))
        {
            holder.player1.setText(gamesDataset.get(position).getHuman_player());
            holder.player2.setText(gamesDataset.get(position).getHuman_player());
        }
        holder.gameImage.setBackgroundResource(imageId);

    }
}

