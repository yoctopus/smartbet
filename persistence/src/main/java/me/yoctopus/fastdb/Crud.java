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

interface Crud<X> {
    String getName();

    boolean onCreate(List<Model> models, X x);

    boolean onCreate(Model model, X x) throws DBError;

    boolean onDrop(Model model, X x) throws DBError;

    boolean onDrop(X x);

    <T> List<T> loadList(Model<T> model);

    <T> List<T> loadList(Model<T> model, Column column);

    <T> boolean update(T t, Model<T> model, Column column);

    <T> T load(Model<T> model, Column column);

    <T> T load(Model<T> model, Column[] columns);

    boolean delete(Model model, Column column);

    boolean delete(Model model);

    <T> boolean delete(Model model, Column<T> column, List<T> list);

    <T> boolean save(T t, Model<T> model);

    <T> boolean save(List<T> list, Model<T> model);

    boolean deleteAllModels();

    int getLastId(Model model);
}
