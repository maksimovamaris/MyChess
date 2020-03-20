package com.maksimovamaris.chess.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRunner implements Runner {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void runInBackground(Runnable task) {
        executor.submit(task);
    }

    @Override
    public void runOnMain(Runnable task) {
        handler.post(task);
    }

}
