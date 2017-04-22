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

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import me.yoctopus.smarttips.LogUtil;

public abstract class Database extends SQLiteOpenHelper implements Crud<SQLiteDatabase> {
    private static final String DEFAULT_NAME =
            "database";
    public static final int REMOVE_ALL_ADD_ALL = 0;
    public static final int DEFAULT_ACTION_ON_UPGRADE =
            REMOVE_ALL_ADD_ALL;
    public static final int REMOVE_ALL = 1;
    public static final int ADD_MORE = 2;
    public static final int REMOVE_SOME = 3;

    private List<Model> models;
    private String TAG =
            LogUtil.makeLogTag(Database.class);
    private Context context;

    private Database(Context context,
                     String name,
                     SQLiteDatabase.CursorFactory factory,
                     int version,
                     DatabaseErrorHandler errorHandler) {
        super(context,
                name,
                factory,
                version,
                errorHandler);
        LogUtil.i(TAG,
                "Database init");
        this.context = context;
        models = onGetModels();
    }

    public Database(Context context,
                    String name,
                    int version,
                    final OnCorrupt listener) {
        this(context,
                name,
                null,
                version,
                new DatabaseErrorHandler() {
                    @Override
                    public void onCorruption(
                            SQLiteDatabase dbObj) {
                        assert listener != null;
                        listener.onCorrupt();
                    }
                });
    }

    public Database(Context context,
                    int version) {
        this(context,
                DEFAULT_NAME,
                version,
                null);
    }

    @Override
    public final void onCreate(SQLiteDatabase db) {
        LogUtil.i(TAG,
                "onCreate");
        onCreate(models,
                db);
    }

    @Override
    public final void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        LogUtil.i(TAG,
                "onUpgrade");
        synchronized (this) {
            int action = shouldBackup(oldVersion,
                    newVersion);
            switch (action) {
                case REMOVE_ALL_ADD_ALL : {
                    for (int i = 0;
                         i < models.size();
                         i++) {
                        models.get(i).backup(db);
                    }
                    if (onDrop(db)) {
                        onCreate(db);
                    }
                    for (Model model : models) {
                        model.restore(db);
                    }
                    break;
                }
                case ADD_MORE : {
                    break;
                }
                case REMOVE_SOME :{
                    break;
                }
                case REMOVE_ALL:{
                    break;
                }
            }
        }
    }

    public abstract int shouldBackup(int oldVersion,
                                         int newVersion);

    @Override
    public String getName() {
        return this.getDatabaseName();
    }

    public abstract List<Model> onGetModels();

    @Override
    public final boolean onCreate(List<Model> models,
                                  SQLiteDatabase database) {
        boolean created = true;
        for (Model model : models) {
            try {
                created = created &&
                        onCreate(model,
                                database);
            } catch (DBError e) {
                e.printStackTrace();
                return false;
            }
        }
        LogUtil.i(TAG,
                "onCreate " +
                        created);
        return created;
    }

    @Override
    public final <T> boolean onCreate(Model<T> model,
                                      SQLiteDatabase database) throws DBError {
        try {
            model.onCreate(
                    database);
        } catch (ModelError e) {
            throw new DBError(e);
        }
        return true;
    }

    @Override
    public final <T> boolean onDrop(Model<T> model,
                                    SQLiteDatabase database) throws DBError {
        try {
            model.onDrop(
                    database);
        } catch (ModelError e) {
            throw new DBError(e);
        }
        return true;
    }


    @Override
    public final boolean onDrop(SQLiteDatabase database) {
        boolean dropped = true;
        for (Model model : models) {
            try {
                dropped = dropped &&
                        onDrop(model,
                                database);
            } catch (DBError e) {
                e.printStackTrace();
                return false;
            }
        }
        LogUtil.i(TAG,
                "onDrop " +
                        dropped);
        return dropped;
    }

    @Override
    public final <T> List<T> loadList(Model<T> model) {
        return model.onLoadList(
                getReadableDatabase(),
                true);
    }

    @Override
    public <T> List<T> loadList(Model<T> model,
                                Col col) {
        return model.onLoadList(getReadableDatabase(),
                col);
    }

    @Override
    public final <T> boolean update(T t,
                                    Model<T> model,
                                    Col col) {
        return model.onUpdate(t,
                col,
                getWritableDatabase());
    }

    @Override
    public final <T> T load(Model<T> model,
                            Col col) {
        return model.onLoad(col,
                getReadableDatabase());
    }

    @Override
    public <T> T load(Model<T> model,
                      Col[] cols) {
        return model.onLoad(cols,
                getReadableDatabase());
    }

    @Override
    public final <T> boolean delete(Model<T> model,
                                    Col col) {
        return model.onDelete(col,
                getWritableDatabase());
    }

    @Override
    public <T> boolean delete(Model<T> model) {
        return model.onDelete(getWritableDatabase());
    }

    @Override
    public final <T> boolean save(T t,
                                  Model<T> model) {
        return model.onSave(t,
                getWritableDatabase());
    }

    @Override
    public final <T> boolean save(List<T> list,
                                  Model<T> model) {
        return model.onSave(list,
                getWritableDatabase(),
                true);
    }

    @Override
    public boolean deleteAllModels() {
        boolean deleted = true;
        for (Model model : models) {
            deleted = deleted &&
                    delete(model);
        }
        return deleted;
    }

    @Override
    public int getLastId(Model model) {
        return model.onGetLastId(
                getReadableDatabase());
    }

    public Context getContext() {
        return context;
    }
}
