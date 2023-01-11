package com.example.learn_map.async;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncWorker {
    private static AsyncWorker instance = new AsyncWorker();
    private static final int NUMBER_OF_THREADS = 50;

    private ExecutorService executorService;
    protected Handler handler;

    public boolean newAsyncWorker(){
        if (executorService!=null){
            executorService.shutdownNow();
        }
        executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        handler = new Handler(Looper.getMainLooper());
        return true;
    }

    public static AsyncWorker getInstance() {
        return instance;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Handler getHandler() {
        return handler;
    }
}
