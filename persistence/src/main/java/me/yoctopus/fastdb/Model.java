/*
 * Copyright 2017, Solutech RMS
 * Licensed under the Apache License, Version 2.0, "Solutech Limited".
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.fastdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.yoctopus.utils.LogUtil;
import me.yoctopus.utils.Te2;

public abstract class Model<T> implements Table<T, SQLiteDatabase>,
        Te2<T, Cursor, ContentValues> {
    private static final String CREATE_PREFIX =
            "CREATE TABLE IF NOT EXISTS ";
    private static final String DROP_PREFIX =
            "DROP TABLE IF EXISTS ";
    private static final String SELECT_PREFIX =
            "SELECT * FROM ";
    private String name = "`" + onGetName() + "`";
    private String TAG = LogUtil.makeTag(Model.class).concat(name);
    private Column primary;
    private List<T> backup;

    public abstract List<Column> onGetColumns();

    public abstract String onGetName();

    @Override
    public final boolean onCreate(SQLiteDatabase database) throws ModelError {
        LogUtil.i(TAG, "onCreate" + name);
        String sql = CREATE_PREFIX;
        sql = sql.concat(name + "(");
        List<Column> columns = onGetColumns();
        Collections.sort(columns, Column.ascending);
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (i == columns.size() - 1) {
                sql = sql.concat(column.toString());
            } else {
                sql = sql.concat(column.toString() + ", ");
            }
        }
        sql = sql.concat(");");
        try {
            LogUtil.e(TAG, sql);
            database.execSQL(sql);
        } catch (SQLException e) {
            LogUtil.e(TAG, e);
            throw new ModelError(e);
        }
        return true;
    }

    @Override
    public final List<T> onLoadList(SQLiteDatabase database, boolean close) {
        LogUtil.e(TAG, "onLoadList" + name);
        Cursor cursor;
        try {
            cursor = database.rawQuery(SELECT_PREFIX + name + ";", null);
        } catch (SQLiteException e) {
            return new ArrayList<>();
        }
        List<T> ts = new ArrayList<>();
        while (cursor.moveToNext()) {
            if (cursor.isClosed()) {
                throw new RuntimeException("Attempting to access an " +
                        "already closed cursor");
            }
            ts.add(onSet(cursor));
        }
        cursor.close();
        if (close) {
            database.close();
        }
        return ts;
    }


    @Override
    public List<T> onLoadList(SQLiteDatabase database, Column column) {
        LogUtil.e(TAG, "onLoadList" + name);
        if (column == null) {
            return onLoadList(database, true);
        }
        String where = column.getName() +
                " = " +
                column.getValue();
        Cursor cursor =
                database.query(name,
                        null,
                        where,
                        null,
                        null,
                        null,
                        null);
        List<T> ts = new ArrayList<>();
        while (cursor.moveToNext()) {
            if (cursor.isClosed()) {
                throw new RuntimeException("Attempting to access an " +
                        "already closed cursor");
            }
            ts.add(onSet(cursor));
        }
        cursor.close();
        database.close();
        return ts;
    }

    @Override
    public final boolean onUpdate(T t, SQLiteDatabase database, Column column) {
        LogUtil.e(TAG, "onProgress" + name);
        String where = null;
        if (column != null) {
            where = column.getName() +
                    " = " +
                    column.getValue();
        }
        return database.update(name,
                onGet(t),
                where,
                null) >= 0;
    }

    @Override
    public final <C> boolean onDelete(SQLiteDatabase database, Column<C> column, List<C> list) {
        boolean deleted = true;
        String where;
        for (C c : list) {
            where = column.getName() + " = " + c;
            deleted = database.delete(name, where, null) >= 0;
        }
        return deleted;
    }

    @Override
    public final boolean onDelete(SQLiteDatabase database, Column column) {
        return database.delete(name,
                column.getName() +
                        " = " +
                        column.getValue(),
                null) >= 0;
    }

    @Override
    public final boolean onDelete(SQLiteDatabase database) {
        return database.delete(name,
                null,
                null) >= 0;
    }

    @Override
    public final boolean onSave(T t, SQLiteDatabase database) {
        return database.insert(name,
                null,
                onGet(t)) >= 0;
    }

    @Override
    public final T onLoad(SQLiteDatabase database, Column column) {
        String where = column.getName() +
                " = " +
                column.getValue();
        Cursor cursor = database.query(name,
                null,
                where,
                null,
                null,
                null,
                null);
        T t = onSet(cursor);
        cursor.close();
        database.close();
        return t;
    }

    @Override
    public final T onLoad(SQLiteDatabase database, Column[] columns) {
        String[] args = new String[columns.length];
        for (int i = 0;
             i < columns.length;
             i++) {
            Column column = columns[i];
            String where = column.getName() +
                    " = " +
                    column.getValue();
            args[i] = where;
        }
        Cursor cursor = database.query(name,
                null,
                null,
                args,
                null,
                null,
                null);
        T t = onSet(cursor);
        cursor.close();
        database.close();
        return t;
    }

    @Override
    public final boolean onSave(List<T> list, SQLiteDatabase database, boolean close) {
        boolean saved = true;
        for (T t : list) {
            saved = saved && database.insert(name,
                    null,
                    onGet(t)) >= 0;
        }
        if (close) {
            database.close();
        }
        return saved;
    }

    @Override
    public final boolean onDrop(SQLiteDatabase database) throws ModelError {
        String sql = DROP_PREFIX + name + ";";
        LogUtil.e(TAG, "onDrop " +
                name +
                " with statement " +
                sql);
        try {
            database.execSQL(sql);
        } catch (SQLException e) {
            throw new ModelError(e);
        }
        return true;
    }

    @Override
    public final int onGetLastId(SQLiteDatabase database) {
        if (primary != null) {
            String[] cols = new String[]{
                    primary.getName()};
            Cursor cursor = database.query(
                    name,
                    cols,
                    null,
                    null,
                    null,
                    null,
                    null);
            cursor.moveToLast();
            int id = cursor.getInt(primary.getIndex());
            cursor.close();
            database.close();
            return id;
        }
        return 0;
    }

    public void setPrimary(Column primary) {
        this.primary = primary;
    }

    void backup(SQLiteDatabase database) {
        LogUtil.e(TAG, "backing up " + name);
        backup = onLoadList(database, false);
        if (backup == null || backup.isEmpty()) {
            return;
        }
        LogUtil.e(TAG, "backed up " +
                backup.size() +
                " items in " +
                name);
    }

    void restore(SQLiteDatabase database) {
        LogUtil.e(TAG, "restoring " + name);
        if (backup != null && !backup.isEmpty()) {
            onSave(backup, database, false);
            LogUtil.e(TAG, "restored " +
                    backup.size() +
                    " items into " +
                    name);
        }
    }
}
