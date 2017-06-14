/*
 * Copyright 2017, Peter Vincent
 * Licensed under the Apache License, Version 2.0, Wallet.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.cac.tx;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

public abstract class Tx<T, X> implements Future {
    private T[] params;
    private Task task;
    private final ThreadLocal<Runnable> thread =
            new ThreadLocal<Runnable>() {
                @Override
                protected Runnable initialValue() {
                    return new Runnable() {
                        @Override
                        public void run() {
                            if (task != null) {
                                return;
                            }
                            task = new Task();
                            if (params != null) {
                                task.execute(params);
                            } else {
                                task.execute((T[]) null);
                            }
                        }
                    };
                }
            };
    private boolean completed = false;
    private Builder builder;
    private CallBacks<T, X> callBacks;
    private Progress<T, X> progress;
    protected List<OnComplete<T>> onComplete;
    private Handler handler;
    private Context context;
    private int id;

    public Tx(Context context, int id) {
        handler = new Handler(Looper.getMainLooper());
        this.context = context;
        this.id = id;
        callBacks = getCallBacks();
        progress = getProgress();
        onComplete = new ArrayList<>();
    }

    public <B> Tx(Builder<B> builder) {
        this(builder.getContext(), builder.getId());
        this.builder = builder;
    }

    public void execute() {
        execute(0);
    }


    public void execute(long millis) {
        try {
            checkCallBacks();
        } catch (NoCallBacksError error) {
            error.show();
            return;
        }
        handler.postDelayed(thread.get(), millis);
    }

    @Override
    public void cancel() {
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

    @Override
    public boolean isCancelled() {
        return task == null;
    }

    @Override
    public boolean isDone() {
        return completed;
    }

    public void setParams(T[] params) {
        this.params = params;
    }

    public abstract Progress<T, X> getProgress();

    private void checkCallBacks() throws NoCallBacksError {
        if (callBacks == null) {
            throw new NoCallBacksError();
        }
    }

    public void finalize(T t) {
        if (onComplete != null) {
            for (OnComplete<T> onComplete1 : onComplete) {
                onComplete1.onComplete(id, t);
            }
        }
    }

    public abstract CallBacks<T, X> getCallBacks();


    public void setOnComplete(OnComplete<T> onComplete) {
        if (this.onComplete == null) {
            this.onComplete = new ArrayList<>();
        }
        this.onComplete.add(onComplete);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Builder getBuilder() {
        return builder;
    }

    public interface CallBacks<T, X> {
        /**
         * callbacks to initialize this task
         */
        void onStart();

        /**
         * @return the result of the task
         */
        T onExecute();

        /**
         * @param x show progress while the task is executing
         */
        void onProgress(X... x);

        /**
         * @param t denote the end of this task
         */
        void onEnd(T t);
    }

    public interface OnComplete<T> {
        void onComplete(int id, T t);
    }

    public interface Progress<T, X> {
        X[] onProgress(T... t);
    }

    public interface Builder<B> {
        Context getContext();

        int getId();

        B get();
    }

    protected class Task extends AsyncTask<T, X, T> {
        @SafeVarargs
        @Override
        protected final T doInBackground(T... params) {
            if (progress != null && params.length != 0) {
                publishProgress(progress.onProgress(params));
            }
            T t = callBacks.onExecute();
            completed = true;
            return t;
        }

        @Override
        protected void onPreExecute() {
            callBacks.onStart();
        }

        @SafeVarargs
        @Override
        protected final void onProgressUpdate(X... values) {
            super.onProgressUpdate(values);
            callBacks.onProgress(values);
        }

        @Override
        protected void onPostExecute(T t) {
            super.onPostExecute(t);
            callBacks.onEnd(t);
            Tx.this.finalize(t);
        }
    }
}
