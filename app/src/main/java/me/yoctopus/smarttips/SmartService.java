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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
        fetchTips.setOnCompleteListener(
                new Tx.OnCompleteListener<Tips.TipsData>() {
                    @Override
                    public void OnComplete(int id,
                                           Tips.TipsData tipsData) {
                        if (tipsData == null) {
                            return;
                        }
                        if (!tipsData.getTipses().isEmpty()) {
                            notifyTips(tipsData.getTipses().size());
                        }
                    }
                });
        fetchTips.execute();
    }
    private void notifyTips(int size) {
        
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
