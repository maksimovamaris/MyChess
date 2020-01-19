package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chess.model.Game;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Game game=new Game();
        game.startGame();

    }


}
