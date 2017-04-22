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
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

class FetchTips extends Tx.NetTx<Tips.TipsData> {
    private String TAG = LogUtil.makeLogTag(FetchTips.class);
    FetchTips(Context context,
              int id) {
        super(context,
                id);
    }

    @Override
    public OnUpdateListener<Tips.TipsData,
            Integer> getOnUpdateListener() {
        return null;
    }

    @Override
    public CallBacks<Tips.TipsData,
            Integer> getCallBacks() {
        return new CallBacks<Tips.TipsData, Integer>() {
            @Override
            public void OnStart() {

            }

            @Override
            public Tips.TipsData OnExecute() {
                LogUtil.d(TAG, "getting actions");
                final ArrayList<Tips> tipses =
                        new ArrayList<>();
                String url = Address.TIPS.toString();
                StringRequest sRequest =
                        new StringRequest(url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(
                                            String response) {
                                        LogUtil.d(TAG, "get tips successful");
                                        JSONObject jsonObject;
                                        JSONArray tips;
                                        try {
                                            jsonObject =
                                                    new JSONObject(
                                                            response);
                                            tips =
                                                    jsonObject.getJSONArray("result");
                                            for (int i = 0; i
                                                    < tips.length();
                                                 i++) {
                                                JSONObject jo =
                                                        tips.getJSONObject(i);
                                                Tips tips1 = new Tips();
                                                tips1.setLeague(jo.getString("league"));
                                                String verses = jo.getString("team");
                                                String[] teams = verses.split("vs");
                                                tips1.setTeamA(teams[0]);
                                                tips1.setTeamB(teams[1]);
                                                tips1.setPrediction(jo.getString("prediction"));
                                                tips1.setDateTime(jo.getString("time"));
                                        /*tips1.setTime(
                                                new Date(Date.parse(jo.getString("time"))));
                                                */
                                                if (!TextUtils.isEmpty(jo.getString("flag"))) {
                                                    tips1.setFlag_name(jo.getString("flag"));
                                                }
                                                long time = Long.valueOf(jo.getString("datelong"));
                                                tips1.setTime(new Date(time));
                                                tips1.setId(Integer.parseInt(jo.getString("ID")));
                                                tipses.add(tips1);
                                            }
                                            Tips.TipsData data = new
                                                    Tips.TipsData();
                                            data.setTipses(
                                                    tipses);
                                            returnData(data);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            LogUtil.e(TAG, "get tips error");
                                            returnData(null);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        LogUtil.e(TAG, "Network error");
                                        returnData(null);
                                    }
                                });
                RequestQueue requestQueue =
                        Volley.newRequestQueue(getContext());
                requestQueue.add(sRequest);
                SmartTipsApp.getInstance().addToRequestQueue(sRequest);
                return null;
            }

            @Override
            public void OnProgress(Integer... x) {

            }

            @Override
            public void OnEnd(Tips.TipsData tipsData) {

            }
        };
    }
    private enum Address {
        TIPS("results.php");

        String url;

        Address(String url) {
            this.url = Address.ServerAddress.getUrl().concat(url);
        }


        @Override
        public String toString() {
            return url;
        }


        static class ServerAddress {
            private static final String url =
                    "http://greenbelemyafrica.com/";

            static String getUrl() {
                return url;
            }
        }
    }
}
