package com.example.learn_map.async;

import java.util.concurrent.ExecutorService;

public abstract class AsyncTask<INPUT, PROGRESS, OUTPUT> {
    private boolean cancelled = false;
//
    public AsyncTask() {
        cancelled = !AsyncWorker.getInstance().newAsyncWorker();
        //Log.e("AsyncTask cancel=", cancelled?"1":"0");
    }

    /**
     * @see #execute(Object)
     */
    public AsyncTask<INPUT, PROGRESS, OUTPUT> execute() {
        return execute(null);
    }

    /**
     * Starts is all
     * @param input Data you want to work with in the background
     */
    public AsyncTask<INPUT, PROGRESS, OUTPUT> execute(final INPUT input) {
        onPreExecute();

        ExecutorService executorService = AsyncWorker.getInstance().getExecutorService();
        executorService.execute(() -> {

            /*if (executorService instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor pool = (ThreadPoolExecutor) executorService;
                Log.e("AsyncTask =", "ThreadPoolExecutor active="+ pool.getActiveCount());
                Log.e("AsyncTask =", "ThreadPoolExecutor pool="+ pool.getPoolSize());
                Log.e("AsyncTask =", "ThreadPoolExecutor core pool="+ pool.getCorePoolSize());
            }*/

            final OUTPUT output = doInBackground(input);
            if (!isCancelled()) {
                AsyncWorker.getInstance().getHandler().post(() -> onPostExecute(output));
            }/*else{
                Log.e("AsyncTask", " doInBackground canceled");
            }*/
        });
        return this;
    }

    /**
     * Call to publish progress from background
     * @param progress  Progress made
     */
    protected void publishProgress(final PROGRESS progress) {
        AsyncWorker.getInstance().getHandler().post(() -> {
            onProgress(progress);

            if (onProgressListener != null) {
                onProgressListener.onProgress(progress);
            }
        });
    }

    protected void onProgress(final PROGRESS progress) {

    }

    /**
     * Call to cancel background work
     */
    public void cancel() {
        cancelled = true;
        //AsyncWorker.getInstance().getExecutorService().shutdownNow();
    }

    /**
     *
     * @return Returns true if the background work should be cancelled
     */
    public boolean isCancelled() {
//        return AsyncWorker.getInstance().getExecutorService().isShutdown();
        return cancelled;
    }

    /**
     * Call this method after cancelling background work
     */
    protected void onCancelled() {
        AsyncWorker.getInstance().getHandler().post(() -> {
            if (onCancelledListener != null) {
                onCancelledListener.onCancelled();
            }
        });
    }

    /**
     * Work which you want to be done on UI thread before {@link #doInBackground(Object)}
     */
    protected void onPreExecute() {

    }

    /**
     * Work on background
     * @param input Input data
     * @return      Output data
     */
    protected abstract OUTPUT doInBackground(INPUT input);

    /**
     * Work which you want to be done on UI thread after {@link #doInBackground(Object)}
     * @param output    Output data from {@link #doInBackground(Object)}
     */
    protected void onPostExecute(OUTPUT output) {

    }

    private OnProgressListener<PROGRESS> onProgressListener;
    public interface OnProgressListener<PROGRESS> {
        void onProgress(PROGRESS progress);
    }

    public void setOnProgressListener(OnProgressListener<PROGRESS> onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    private OnCancelledListener onCancelledListener;
    public interface OnCancelledListener {
        void onCancelled();
    }

    public void setOnCancelledListener(OnCancelledListener onCancelledListener) {
        this.onCancelledListener = onCancelledListener;
    }
}
