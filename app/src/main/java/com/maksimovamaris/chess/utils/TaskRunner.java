package com.maksimovamaris.chess.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRunner implements Runner {

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void runInBackground(Runnable task) {
        mExecutor.submit(task);
    }

    @Override
    public void runOnMain(Runnable task) {
        mHandler.post(task);
    }

}
