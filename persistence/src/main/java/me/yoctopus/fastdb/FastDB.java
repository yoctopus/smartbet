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

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import me.yoctopus.utils.LogUtil;

public abstract class FastDB extends SQLiteOpenHelper implements Crud<SQLiteDatabase> {
    private static final String DEFAULT_NAME = "fast_db";
    private static List<Model> models;
    private String TAG = LogUtil.makeTag(FastDB.class);
    private Context context;

    private FastDB(Context context, String name, SQLiteDatabase.CursorFactory factory,
                   int version,
                   DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        LogUtil.i(TAG, "FastDB init");
        this.context = context;
        models = onGetModels();
    }

    public FastDB(Context context, String name, int version, final Corrupt listener) {
        this(context, name, null, version, new DatabaseErrorHandler() {
            @Override
            public void onCorruption(
                    SQLiteDatabase dbObj) {
                assert listener != null;
                listener.onCorrupt();
            }
        });
    }

    public FastDB(Context context, int version) {
        this(context, DEFAULT_NAME, version, null);
    }

    @Override
    public final void onCreate(SQLiteDatabase db) {
        LogUtil.i(TAG, "onCreate");
        onCreate(models, db);
    }

    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i(TAG, "onUpgrade");
        synchronized (this) {
            boolean action = shouldBackup(oldVersion, newVersion);
            if (action) {
                for (int i = 0; i < models.size(); i++) {
                    models.get(i).backup(db);
                }
                if (onDrop(db)) {
                    onCreate(db);
                }
                for (Model model : models) {
                    model.restore(db);
                }
            }
        }
    }

    public abstract boolean shouldBackup(int oldVersion, int newVersion);

    @Override
    public String getName() {
        return this.getDatabaseName();
    }

    public abstract List<Model> onGetModels();

    @Override
    public final boolean onCreate(List<Model> models, SQLiteDatabase database) {
        boolean created = true;
        for (Model model : models) {
            try {
                created = created && onCreate(model, database);
            } catch (DBError e) {
                e.printStackTrace();
                return false;
            }
        }
        LogUtil.i(TAG, "onCreate " + created);
        return created;
    }

    @Override
    public final boolean onCreate(Model model, SQLiteDatabase database) throws DBError {
        try {
            model.onCreate(database);
        } catch (ModelError e) {
            throw new DBError(e);
        }
        return true;
    }

    @Override
    public final boolean onDrop(Model model, SQLiteDatabase database) throws DBError {
        try {
            model.onDrop(database);
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
                dropped = dropped && onDrop(model, database);
            } catch (DBError e) {
                e.printStackTrace();
                return false;
            }
        }
        LogUtil.i(TAG, "onDrop " + dropped);
        return dropped;
    }

    @Override
    public final <T> List<T> loadList(Model<T> model) {
        return model.onLoadList(getReadableDatabase(), true);
    }

    @Override
    public <T> List<T> loadList(Model<T> model, Column column) {
        return model.onLoadList(getReadableDatabase(), column);
    }

    @Override
    public final <T> boolean update(T t, Model<T> model, Column column) {
        return model.onUpdate(t, getWritableDatabase(), column);
    }

    @Override
    public final <T> T load(Model<T> model, Column column) {
        return model.onLoad(getReadableDatabase(), column);
    }

    @Override
    public <T> T load(Model<T> model, Column[] columns) {
        return model.onLoad(getReadableDatabase(), columns);
    }

    @Override
    public final boolean delete(Model model, Column column) {
        return model.onDelete(getWritableDatabase(), column);
    }

    @Override
    public boolean delete(Model model) {
        return model.onDelete(getWritableDatabase());
    }

    @Override
    public <T> boolean delete(Model model, Column<T> column, List<T> list) {
        return model.onDelete(getWritableDatabase(), column, list);
    }

    @Override
    public final <T> boolean save(T t, Model<T> model) {
        return model.onSave(t, getWritableDatabase());
    }

    @Override
    public final <T> boolean save(List<T> list, Model<T> model) {
        return model.onSave(list, getWritableDatabase(), true);
    }

    @Override
    public boolean deleteAllModels() {
        boolean deleted = true;
        for (Model model : models) {
            deleted = deleted && delete(model);
        }
        return deleted;
    }

    @Override
    public int getLastId(Model model) {
        return model.onGetLastId(getReadableDatabase());
    }

    public Context getContext() {
        return context;
    }
}
