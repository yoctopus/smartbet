/*
 * Copyright (C) 2017 Vincent Peter
 * Licensed under the Apache License, Version 2.0 Smart Bet Tips
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.smarttips;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public abstract class Tx<T, X> extends Handler {
    private T[] params;
    private boolean isCustom =
            false;
    private Custom custom;
    private final ThreadLocal<Runnable> threadLocal =
            new ThreadLocal<Runnable>() {
                @Override
                protected Runnable initialValue() {
                    return new Runnable() {
                        @Override
                        public void run() {
                            if (isCustom) {
                                custom.transact();
                                return;
                            }
                            Task task = new Task();
                            if (getParams() != null) {
                                task.execute(getParams());
                            } else {
                                task.execute();
                            }
                        }
                    };
                }
            };
    private OnUpdateListener<T, X> onUpdateListener;
    private OnCompleteListener<T> onCompleteListener
            = null;
    private Context context;
    private CallBacks<T, X> callBacks;
    private int id;

    private Tx() {
        super(new Handler().getLooper());
        onUpdateListener = getOnUpdateListener();
        callBacks = getCallBacks();
    }

    public Tx(Context context,
              int id) {
        this();
        this.setContext(context);
        this.id = id;
    }

    private Tx(Custom custom) {
        this();
        this.custom = custom;
        this.isCustom = true;
    }


    public void execute() {
        try {
            checkCallBacks();
        } catch (NoCallBacksFoundError error) {
            error.printStackTrace();
            return;
        }
        post(getThreadLocal());
    }

    public void execute(long millis) {
        try {
            checkCallBacks();
        } catch (NoCallBacksFoundError error) {
            error.printStackTrace();
            return;
        }
        postDelayed(getThreadLocal(),
                millis);
    }

    private T[] getParams() {
        return params;
    }

    public void setParams(T[] params) {
        this.params = params;
    }

    public abstract OnUpdateListener<T, X> getOnUpdateListener();

    protected void checkCallBacks()
            throws NoCallBacksFoundError {
        if (callBacks == null) {
            throw new NoCallBacksFoundError();
        }
    }

    private void finalize(T t) {
        if (getOnCompleteListener() != null) {
            getOnCompleteListener().OnComplete(id,
                    t);
        }
    }

    public abstract CallBacks<T, X> getCallBacks();

    private Runnable getThreadLocal() {
        return threadLocal.get();
    }

    private OnCompleteListener<T> getOnCompleteListener() {
        return onCompleteListener;
    }

    public void setOnCompleteListener(
            OnCompleteListener<T> onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public interface CallBacks<T, X> {
        /**
         * method is called
         * just before
         * the task is started
         */
        void OnStart();

        /**
         * OnExecute the main
         * logic here
         */
        T OnExecute();

        /**
         * show an updated
         * message
         * here while the task
         * is continuing
         */

        void OnProgress(X... x);

        /**
         * call this end the custom
         *
         * @param t the data returned
         */
        void OnEnd(T t);
    }

    public interface OnCompleteListener<T> {
        void OnComplete(int id,
                        T t);
    }

    public interface Custom {
        void transact();
    }

    public interface OnUpdateListener<T, X> {
        /**
         * @param t the array of <T parameters
         * @return array of X
         */
        X[] OnUpdate(T... t);
    }

    abstract static class NetTx<T> extends Tx<T, Integer>{
        OnComplete<T> onComplete;
        NetTx(Context context, int id) {
            super(context, id);
            onComplete = new OnComplete<T>() {
                @Override
                public void onComplete(T t) {
                    NetTx.super.finalize(t);
                }
            };
        }
        public void returnData(T t) {
            onComplete.onComplete(t);
        }
        interface OnComplete<T> {
            void onComplete(T t);

        }
    }

    private class Task extends AsyncTask<T, X, T> {
        @SafeVarargs
        @Override
        protected final T doInBackground(T... params) {
            if (onUpdateListener != null &&
                    params.length != 0) {
                publishProgress(onUpdateListener.OnUpdate(params));
            }
            return callBacks.OnExecute();
        }

        @Override
        protected void onPreExecute() {
            callBacks.OnStart();
        }

        @SafeVarargs
        @Override
        protected final void onProgressUpdate(X... values) {
            super.onProgressUpdate(values);
            callBacks.OnProgress(values);
        }

        @Override
        protected void onPostExecute(T t) {
            super.onPostExecute(t);
            callBacks.OnEnd(t);
            if (t == null) {
                return;
            }
            Tx.this.finalize(t);
        }
    }

    /**
     * Created by octopus on 10/16/16.
     */
    public static class NoCallBacksFoundError extends Exception {
        public NoCallBacksFoundError(String message) {
            super(message);
        }
        public NoCallBacksFoundError() {
            this("No callbacks Defined, exiting task");
        }
    }

}
