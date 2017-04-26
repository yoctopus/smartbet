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

import me.yoctopus.fastdb.Column;
import me.yoctopus.fastdb.FastDB;
import me.yoctopus.fastdb.Model;
import me.yoctopus.fastdb.OnCorrupt;
import me.yoctopus.smarttips.Message;
import me.yoctopus.smarttips.Tips;


public class AppDatabase extends FastDB {
    private static final int version = 1;
    private static final String db_name = "db";
    private static TipsTable tipsTable;
    private static Messages messages;
    static {
        tipsTable = new TipsTable();
        messages = new Messages();
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
    public boolean shouldBackup(int oldVersion, int newVersion) {
        return oldVersion > newVersion;
    }

    @Override
    public List<Model> onGetModels() {
        List<Model> models = new ArrayList<>();
        models.add(tipsTable);
        models.add(messages);
        return models;
    }
    public boolean save(Tips tips) {
        return save(tips, tipsTable);
    }
    public boolean save(Message message) {
        return save(message, messages);
    }
    public List<Tips> getTips() {
        return loadList(tipsTable);
    }
    public List<Message> getMessages() {
        return loadList(messages);
    }
    public boolean update(Tips tips) {
        Column<Integer> col = TipsTable.id;
        col.set(tips.getId());
        return update(tips, tipsTable, col);
    }
    public boolean update(Message message) {
        Column<Integer> col = Messages.id;
        col.set(message.getId());
        return update(message, messages, col);
    }
    public boolean saveTips(List<Tips> list) {
        return save(list, tipsTable);
    }
    public boolean saveMessages(List<Message> list) {
        return save(list, messages);
    }
    public boolean delete(Tips tips) {
        Column<Integer> col = TipsTable.id;
        col.set(tips.getId());
        return delete(tipsTable, col);
    }
    public boolean delete(Message message) {
        Column<Integer> column = Messages.id;
        column.set(message.getId());
        return delete(messages, column);
    }
    public boolean deleteAllMessages() {
        return delete(messages);
    }
    public boolean deleteAllTips() {
        return delete(tipsTable);
    }
}
