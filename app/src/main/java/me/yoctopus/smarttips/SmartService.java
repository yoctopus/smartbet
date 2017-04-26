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

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import me.yoctopus.cac.notif.Notification;
import me.yoctopus.cac.tx.Tx;
import me.yoctopus.smarttips.m.AppDatabase;

public class SmartService extends Service {
    public SmartService() {
    }

    @Override
    public int onStartCommand(Intent intent,
                              int flags,
                              int startId) {
        switch (intent.getAction()) {
            case SmartReceiver.CHECK_TIPS : {
                checkTips();
                break;
            }
        }
        return START_STICKY;
    }
    private void checkTips() {
        FetchTips fetchTips = new FetchTips(this, 100);
        fetchTips.setOnComplete(
                new Tx.OnComplete<Tips.TipsData>() {
                    @Override
                    public void onComplete(int id, Tips.TipsData tipsData) {
                        if (tipsData == null) {
                            return;
                        }
                        if (!tipsData.getTipses().isEmpty()) {
                            notifyTips(tipsData.getTipses().size(), getApplicationContext());
                        }
                    }
                });
        fetchTips.execute();
    }
    private void notifyTips(int size, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.football);
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0,
                intent,
                0);
        Notification notification = new Notification(context);
        notification.showNotification(bitmap,"SmartBet",
                size+" tips are available",
                R.drawable.football_logo,
                pendingIntent);
        Message message = new Message("New tips",
                size + " tips are available");
        new AppDatabase(context).save(message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
