package com.maksimovamaris.chess.utils;

public interface Runner {
    public void runInBackground(Runnable task);
    public void runOnMain(Runnable task);
}
