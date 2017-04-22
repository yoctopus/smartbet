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

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.yoctopus.smarttips.LogUtil;
import me.yoctopus.smarttips.Tips;
import me.yoctopus.smarttips.b.Col;
import me.yoctopus.smarttips.b.Database;
import me.yoctopus.smarttips.b.Model;
import me.yoctopus.smarttips.b.OnCorrupt;

/**
 * Created by yoctopus on 3/25/17.
 */

public class AppDatabase extends Database {
    private static final int version = 1;
    private static final String db_name = "app_db";
    private static TipsTable tipsTable;
    static {
        tipsTable = new TipsTable();
    }
    private AppDatabase(Context context,
                       String name, int version,
                       OnCorrupt listener) {
        super(context, name, version, listener);
    }

    public AppDatabase(Context context) {
        this(context,
                db_name,
                version,
                new OnCorrupt() {
                    @Override
                    public void onCorrupt() {

                    }
                });
    }

    @Override
    public int shouldBackup(int oldVersion,
                            int newVersion) {
        return REMOVE_ALL_ADD_ALL;
    }

    @Override
    public List<Model> onGetModels() {
        List<Model> models =
                new ArrayList<>();
        models.add(tipsTable);
        return models;
    }
    public boolean save(Tips tips) {
        return save(tips, tipsTable);
    }
    public List<Tips> getTips() {
        List<Tips> list  = loadList(tipsTable);
        for (Tips tips : list) {
            LogUtil.e("DB", tips.toString());
        }
        return list;
    }
    public boolean update(Tips tips) {
        Col<Integer> col = TipsTable.id;
        col.setData(tips.getId());
        return update(tips, tipsTable, col);
    }
    public boolean save(List<Tips> list) {
        for (Tips tips : list) {
            LogUtil.e("DB", tips.toString());
        }
        return save(list, tipsTable);
    }
    public boolean delete(Tips tips) {
        Col<Integer> col = TipsTable.id;
        col.setData(tips.getId());
        return delete(tipsTable, col);
    }
    public boolean deleteAllTips() {
        return delete(tipsTable);
    }
}
