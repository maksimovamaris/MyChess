package com.maksimovamaris.chess.utils;

import com.maksimovamaris.chess.utils.Runner;

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
