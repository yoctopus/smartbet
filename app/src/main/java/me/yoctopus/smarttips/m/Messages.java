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
import java.util.List;

import me.yoctopus.fastdb.Column;
import me.yoctopus.fastdb.Model;
import me.yoctopus.smarttips.Message;



public class Messages extends Model<Message> {
    private static final String tb_name = "msg";
    public static Column<Integer> id;
    public static Column<String> title, message;
    public static Column<Long> date;
    static {
        id = new Column<>("id", Column.Type.INTEGER.PRIMARY_KEY_AUTOINCREMENT());
        title = new Column<>("tt", Column.Type.TEXT.NOT_NULL(), 1);
        message = new Column<>("mm", Column.Type.TEXT.NOT_NULL(), 2);
        date = new Column<>("dd", Column.Type.REAL.NULLABLE(), 3);
    }
    @Override
    public List<Column> onGetColumns() {
        List<Column> columns = new ArrayList<>();
        columns.add(id);
        columns.add(message);
        columns.add(title);
        columns.add(date);
        return columns;
    }

    @Override
    public String onGetName() {
        return tb_name;
    }

    @Override
    public Message onLoad(Cursor cursor) {
        Message message = new Message(cursor.getString(title.getIndex()),
                cursor.getString(Messages.message.getIndex()),
                cursor.getLong(date.getIndex()));
        message.setId(cursor.getInt(id.getIndex()));
        return message;
    }

    @Override
    public ContentValues onGetValues(Message message) {
        ContentValues values = new ContentValues();
        values.put(title.getName(), message.getTitle());
        values.put(Messages.message.getName(), message.getMessage());
        values.put(date.getName(), message.getDate());
        return values;
    }
}
