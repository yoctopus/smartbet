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

package me.yoctopus.json;


import java.util.List;
import java.util.Map;

interface Accessor<T> {

    void onLoadList(Config config, Complete<List<T>> complete, Map<String, String> map);

    void onUpdate(Config config, T t, Complete complete, Map<String, String> map);

    void onDelete(Config config, Complete complete, Map<String, String> map);

    void onDelete(Config config, List<T> list, Complete complete, Map<String, String> map);

    void onSave(Config config, T t, Complete complete, Map<String, String> map);

    void onLoad(Config config, Complete<T> complete, Map<String, String> map);

    void onSave(Config config, List<T> list, Complete complete, Map<String, String> map);
}
