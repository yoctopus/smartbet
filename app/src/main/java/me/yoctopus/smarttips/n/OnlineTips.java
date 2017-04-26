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

package me.yoctopus.smarttips.n;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import me.yoctopus.json.EndPoint;
import me.yoctopus.json.JsonModel;
import me.yoctopus.smarttips.Tips;



public class OnlineTips extends JsonModel<Tips> {
    @Override
    public EndPoint onGetEndPoint() {
        return new EndPoint("results", "php");
    }

    @Override
    public Tips onLoad(JSONObject jo) throws JSONException {
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
        return tips1;
    }

    @Override
    public JSONObject onGetValues(Tips tips) {
        return null;
    }
}
