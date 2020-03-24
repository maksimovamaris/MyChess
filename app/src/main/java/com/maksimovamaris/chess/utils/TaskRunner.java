package com.maksimovamaris.chess.utils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TaskRunner implements Runner {


    final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("My thread-%d")
            .setDaemon(true)
            .build();

    private final ExecutorService executor = Executors.newFixedThreadPool(2,threadFactory);
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
