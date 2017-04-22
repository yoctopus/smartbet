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

package me.yoctopus.smarttips.b;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import me.yoctopus.smarttips.LogUtil;

public abstract class Model<T> implements Table<T, SQLiteDatabase> {
    private static final String CREATE_PREFIX =
            "CREATE TABLE" +
                    " IF NOT EXISTS ";
    private static final String DROP_PREFIX =
            "DROP TABLE IF EXISTS ";
    private static final String SELECT_PREFIX =
            "SELECT * FROM ";
    private String name = "`" +
            onGetName() +
            "`";
    private String TAG =
            LogUtil.makeLogTag(
                    Model.class)
                    .concat(name);
    private Col primary;

    private List<T> backup;

    public abstract List<Col> onGetColumns();

    public abstract String onGetName();

    @Override
    public final boolean onCreate(SQLiteDatabase database) throws ModelError {
        LogUtil.i(TAG,
                "onCreate" +
                        name);
        String sql = CREATE_PREFIX;
        sql = sql.concat(name +
                " (");
        List<Col> cols =
                onGetColumns();
        for (int i = 0;
             i < cols.size();
             i++) {
            Col col =
                    cols.get(
                            i);
            if (i ==
                    cols.size() -
                            1) {
                sql =
                        sql.concat(
                                col.toString());
            } else {
                sql =
                        sql.concat(
                                col.toString() +
                                        ",");
            }
        }
        sql =
                sql.concat(
                        ");");
        try {
            LogUtil.e(TAG,
                    sql);
            database.execSQL(
                    sql);
        } catch (SQLException e) {
            LogUtil.e(TAG,
                    e);
            throw new ModelError(e);
        }
        return true;
    }

    @Override
    public final List<T> onLoadList(SQLiteDatabase database,
                                    boolean close) {
        LogUtil.e(TAG,
                "onLoadList" +
                        name);
        Cursor cursor;
        try {
            cursor =
                    database.rawQuery(SELECT_PREFIX +
                                    name +
                                    ";",
                            null);
        }
        catch (SQLiteException e) {
            return new ArrayList<>();
        }
        List<T> ts = new ArrayList<>();
        while (cursor.moveToNext()) {
            ts.add(onLoad(cursor));
        }
        cursor.close();
        if (close) {
            database.close();
        }
        return ts;
    }


    @Override
    public List<T> onLoadList(SQLiteDatabase database,
                              Col col) {
        LogUtil.e(TAG,
                "onLoadList" +
                        name);
        if (col == null) {
            return onLoadList(database, true);
        }
        String where = col.getName() +
                " = " +
                col.getData();
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
            ts.add(onLoad(cursor));
        }
        cursor.close();
        database.close();
        return ts;
    }

    public abstract T onLoad(Cursor cursor);

    public abstract ContentValues onGetValues(T t);

    @Override
    public final boolean onUpdate(T t,
                                  Col col,
                                  SQLiteDatabase database) {
        LogUtil.e(TAG,
                "onProgress" +
                        name);
        if (col == null) {
            return database.update(name,
                    onGetValues(t),
                    null,
                    null) >= 0;
        } else {
            String where = col.getName() +
                    " = " +
                    col.getData();
            return database.update(name,
                    onGetValues(t),
                    where,
                    null) >= 0;
        }
    }

    @Override
    public final boolean onDelete(Col col,
                                  SQLiteDatabase database) {
        return database.delete(name,
                col.getName() +
                        " = " +
                        col.getData(),
                null) >= 0;
    }

    @Override
    public final boolean onDelete(SQLiteDatabase database) {
        return database.delete(name,
                null,
                null) >= 0;
    }

    @Override
    public final boolean onSave(T t,
                                SQLiteDatabase database) {
        return database.insert(name,
                null,
                onGetValues(t)) >= 0;
    }

    @Override
    public final T onLoad(Col col,
                          SQLiteDatabase database) {
        String where = col.getName() +
                " = " +
                col.getData();
        Cursor cursor =
                database.query(name,
                        null,
                        where,
                        null,
                        null,
                        null,
                        null);
        T t = onLoad(cursor);
        cursor.close();
        database.close();
        return t;
    }

    @Override
    public T onLoad(Col[] cols,
                    SQLiteDatabase database) {
        String[] args = new String[cols.length];
        for (int i = 0;
             i < cols.length;
             i++) {
            Col col = cols[i];
            String where = col.getName() +
                    " = " +
                    col.getData();
            args[i] = where;
        }
        Cursor cursor =
                database.query(name,
                        null,
                        null,
                        args,
                        null,
                        null,
                        null);
        T t = onLoad(cursor);
        cursor.close();
        database.close();
        return t;
    }

    @Override
    public final boolean onSave(List<T> list,
                                SQLiteDatabase database,
                                boolean close) {
        boolean saved = true;
        for (T t : list) {
            saved = saved &&
                    database.insert(name,
                    null,
                    onGetValues(t)) >= 0;
        }
        if (close) {
            database.close();
        }
        return saved;
    }

    @Override
    public final boolean onDrop(SQLiteDatabase database) throws ModelError {
        String sql = DROP_PREFIX +
                name +
                ";";
        LogUtil.e(TAG,
                "onDrop " +
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
            String[] cols =
                    new String[]{
                            primary.getName()};
            Cursor cursor =
                    database.query(
                            name,
                            cols,
                            null,
                            null,
                            null,
                            null,
                            null);
            cursor.moveToLast();
            int id = cursor.getInt(
                    primary.getIndex());
            cursor.close();
            database.close();
            return id;
        }
        return 0;
    }

    public void setPrimary(Col primary) {
        this.primary = primary;
    }

    void backup(SQLiteDatabase database) {
        LogUtil.e(TAG,
                "backing up " +
                        name);
        backup = onLoadList(database,
                false);
        if (backup == null ||
                backup.isEmpty()) {
            return;
        }
        LogUtil.e(TAG,
                "backed up " +
                        backup.size() +
                        " items in " +
                        name);
    }

    void restore(SQLiteDatabase database) {
        LogUtil.e(TAG,
                "restoring " +
                        name);
        if (backup != null &&
                !backup.isEmpty()) {
            onSave(backup,
                    database,
                    false);
            LogUtil.e(TAG,
                    "restored " +
                            backup.size() +
                            " items into " +
                            name);
        }
    }
}
