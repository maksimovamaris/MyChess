package com.maksimovamaris.chess.utils;

public class StubRunner implements Runner {

    @Override
    public void runInBackground(Runnable task) {
        task.run();
    }

    @Override
    public void runOnMain(Runnable task) {
        task.run();
    }
}
