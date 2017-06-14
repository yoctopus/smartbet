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

package me.yoctopus.cac.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;


public abstract class Scheduler {
    public static final String MESSAGE_TITLE = "Message";
    public static final int INTERVAL_DAY = 1;
    public static final int INTERVAL_WEEK = 2;
    private Context context;
    private Class<? extends BroadcastReceiver> receiver;

    public Scheduler(Context context,
              Class<? extends BroadcastReceiver> receiver) {
        this.context = context;
        this.receiver = receiver;
    }

    public abstract Params onGetParam();

    public void schedule() {
        Params params = onGetParam();
        saveSchedule(params);
    }

    @SuppressLint("ShortAlarm")
    private void saveSchedule(Params params) {
        AlarmManager manager = (AlarmManager)
                context.getSystemService(
                        Context.ALARM_SERVICE);
        Intent intent = new Intent(
                context.getApplicationContext(),
                receiver);
        if (params.intent_action != null) {
            intent.setAction(params.intent_action);
        }
        if (params.intent_message != null) {
            intent.putExtra(MESSAGE_TITLE,
                    params.intent_message);
        }
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        context.getApplicationContext(),
                        0,
                        intent,
                        0);
        Calendar calendar = params.calendar;
        if (params.repeating) {
            long myInterval =
                    0;
            switch (params.interval) {
                case INTERVAL_DAY: {
                    myInterval = AlarmManager.INTERVAL_DAY;
                    break;
                }
                case INTERVAL_WEEK: {
                    for (int i = 0;
                         i < 7;
                         i++) {
                        myInterval += AlarmManager.INTERVAL_DAY;
                    }
                }
            }
            manager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    myInterval,
                    pendingIntent);
        } else {
            manager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent);
        }
    }

    private void scheduleUsingJobScheduler(Params params) {
        ComponentName name = new ComponentName(
                context.getApplicationContext(),
                receiver);
        LollipopScheduler scheduler =
                new LollipopScheduler(params,
                        name);
        scheduler.schedule();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public class LollipopScheduler extends JobService {
        private JobInfo.Builder builder;
        private int id = 0;
        private Calendar calendar;
        private Params params;

        public LollipopScheduler(Params params,
                                 ComponentName name) {
            builder = new JobInfo.Builder(id,
                    name);
            builder.setPeriodic(params.interval);
            builder.setPersisted(params.repeating);
            this.calendar = params.calendar;

        }

        public void schedule() {
            builder.build();
        }

        @Override
        public boolean onStartJob(JobParameters jobParameters) {
            return false;
        }

        @Override
        public boolean onStopJob(JobParameters jobParameters) {
            return true;
        }
    }

    public class Params {
        private String intent_action;
        private String intent_message;
        private boolean repeating = false;
        private int interval = 0;
        private Calendar calendar;

        public Params(String intent_action,
               String intent_message,
               Calendar calendar) {
            this.intent_action = intent_action;
            this.intent_message = intent_message;
            this.calendar = calendar;
        }

        public void setRepeating(boolean repeating) {
            this.repeating = repeating;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }
    }
}
