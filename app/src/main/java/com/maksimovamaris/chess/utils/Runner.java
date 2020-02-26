package com.maksimovamaris.chess.utils;

public interface Runner {
    void runInBackground(Runnable task);
    void runOnMain(Runnable task);

}
