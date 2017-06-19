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

package me.yoctopus.smarttips.m;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.yoctopus.fastdb.Column;
import me.yoctopus.fastdb.Model;
import me.yoctopus.smarttips.Tips;


public class TipsTable extends Model<Tips> {
    public static final String tb_name = "tps";
    public static Column<Integer> id;
    public static Column<String>
            league,
            teamA,
            teamB,
            prediction,
            background_url,
            dateTime,
            flag_name;
    public static Column<Long> time;

    static {
        id = new Column<>("id",
                Column.Type.INTEGER.PRIMARY_KEY_AUTOINCREMENT());
        league = new Column<>("lg",
                Column.Type.TEXT.NULLABLE(), 1);
        teamA = new Column<>("ta",
                Column.Type.TEXT.NOT_NULL(), 2);
        teamB = new Column<>("tb",
                Column.Type.TEXT.NOT_NULL(), 3);
        prediction = new Column<>("pr",
                Column.Type.TEXT.NOT_NULL(), 4);
        background_url = new Column<>("br",
                Column.Type.TEXT.NULLABLE(), 5);
        dateTime = new Column<>("dtt",
                Column.Type.TEXT.NOT_NULL(), 6);
        flag_name = new Column<>("fn",
                Column.Type.TEXT.NULLABLE(), 7);
        time = new Column<>("tm",
                Column.Type.INTEGER.NULLABLE(), 8);
    }

    @Override
    public List<Column> onGetColumns() {
        List<Column> cols = new ArrayList<>();
        cols.add(id);
        cols.add(league);
        cols.add(teamA);
        cols.add(teamB);
        cols.add(prediction);
        cols.add(background_url);
        cols.add(dateTime);
        cols.add(flag_name);
        cols.add(time);
        return cols;
    }

    @Override
    public String onGetName() {
        return tb_name;
    }

    @Override
    public Tips onSet(Cursor cursor) {
        Tips tips = new Tips();
        tips.setId(cursor.getInt(id.getIndex()));
        tips.setLeague(cursor.getString(league.getIndex()));
        tips.setTeamA(cursor.getString(teamA.getIndex()));
        tips.setTeamB(cursor.getString(teamB.getIndex()));
        tips.setPrediction(cursor.getString(prediction.getIndex()));
        tips.setBackgroung_url(cursor.getString(background_url.getIndex()));
        tips.setDateTime(cursor.getString(dateTime.getIndex()));
        tips.setFlag_name(cursor.getString(flag_name.getIndex()));
        tips.setTime(new Date(cursor.getLong(time.getIndex())));
        return tips;
    }

    @Override
    public ContentValues onGet(Tips tips) {
        ContentValues values = new ContentValues();
        values.put(league.getName(), tips.getLeague());
        values.put(teamA.getName(), tips.getTeamA());
        values.put(teamB.getName(), tips.getTeamB());
        values.put(prediction.getName(), tips.getPrediction());
        values.put(background_url.getName(), tips.getBackgroung_url());
        values.put(dateTime.getName(), tips.getDateTime());
        values.put(flag_name.getName(), tips.getFlag_name());
        values.put(time.getName(), tips.getTime().getTime());
        return values;
    }
}
