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

import java.util.List;

import me.yoctopus.cac.tx.Tx;
import me.yoctopus.smarttips.n.ServerConnect;

class FetchTips extends Tx<Tips.TipsData, Integer> {
    private String TAG = LogUtil.makeLogTag(FetchTips.class);
    private ServerConnect connect;
    FetchTips(Context context, int id) {
        super(context, id);
        connect = new ServerConnect(context);
    }

    @Override
    public Progress<Tips.TipsData, Integer> getProgress() {
        return null;
    }

    @Override
    public CallBacks<Tips.TipsData, Integer> getCallBacks() {
        return new CallBacks<Tips.TipsData, Integer>() {
            @Override
            public void onStart() {

            }

            @Override
            public Tips.TipsData onExecute() {
                LogUtil.d(TAG, "getting tips");
                connect.getTips(new me.yoctopus.json.Complete<List<Tips>>() {
                    @Override
                    public void complete(List<Tips> tipses) {
                        Tips.TipsData data = new
                                Tips.TipsData();
                        data.setTipses(tipses);
                        FetchTips.this.finalize(data);
                    }
                });
                return null;
            }

            @Override
            public void onProgress(Integer... x) {

            }

            @Override
            public void onEnd(Tips.TipsData tipsData) {
            }
        };
    }


}
