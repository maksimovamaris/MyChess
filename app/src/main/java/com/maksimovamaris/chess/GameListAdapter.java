package com.maksimovamaris.chess;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.maksimovamaris.chess.data.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameListViewHolder> {
    private ArrayList<GameData> gamesDataset;
    private int imageId;


    public static class GameListViewHolder extends RecyclerView.ViewHolder {
        private TextView gameTime;
        private ImageView gameImage;
        private TextView gameTitle;
        private CardView gameCard;

        public GameListViewHolder(View v) {
            super(v);
            gameCard = v.findViewById(R.id.game_card_view);
            gameTime = v.findViewById(R.id.game_time);
            gameImage = v.findViewById(R.id.game_icon);
            gameTitle=v.findViewById(R.id.game_title);
        }
    }

    public GameListAdapter(List<GameData> games, @DrawableRes int image) {
        gamesDataset = (ArrayList) (games);
        imageId = image;
    }

    @NonNull
    @Override
    public GameListAdapter.GameListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.games_item, viewGroup, false);
        GameListViewHolder gameListViewHolder = new GameListViewHolder(v);
        return gameListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameListViewHolder holder, int position) {
        holder.gameTime.setText(gamesDataset.get(position).getGame_date().toString());
        holder.gameTitle.setText(gamesDataset.get(position).getTurn()+" turn");
        holder.gameImage.setBackgroundResource(imageId);
    }

    @Override
    public int getItemCount() {
        return gamesDataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

