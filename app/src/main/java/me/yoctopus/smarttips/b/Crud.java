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

import java.util.List;

interface Crud<X> {
    String getName();
    boolean onCreate(List<Model> models, X x);
    <T> boolean onCreate(Model<T> model, X x) throws DBError;
    <T> boolean onDrop(Model<T> model, X x) throws DBError;
    boolean onDrop(X x);
    <T> List<T> loadList(Model<T> model);
    <T> List<T> loadList(Model<T> model, Col col);
    <T> boolean update(T t, Model<T> model, Col col);
    <T> T load(Model<T> model, Col col);
    <T> T load(Model<T> model, Col[] cols);
    <T> boolean delete(Model<T> model, Col col);
    <T> boolean delete(Model<T> model);
    <T> boolean save(T t, Model<T> model);
    <T> boolean save(List<T> list, Model<T> model);
    boolean deleteAllModels();
    int getLastId(Model model);
}
