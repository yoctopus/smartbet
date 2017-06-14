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

import java.util.List;


interface Table<T, X> {
    boolean onCreate(X x) throws ModelError;

    List<T> onLoadList(X x, boolean close);

    List<T> onLoadList(X x, Column column);

    boolean onUpdate(T t, X x, Column column);

    boolean onDelete(X x, Column column);

    boolean onDelete(X x);

    <C> boolean onDelete(X x, Column<C> column, List<C> list);

    boolean onSave(T t, X x);

    T onLoad(X x, Column[] columns);

    T onLoad(X x, Column column);

    boolean onSave(List<T> list, X x, boolean close);

    boolean onDrop(X x) throws ModelError;

    int onGetLastId(X x);
}
