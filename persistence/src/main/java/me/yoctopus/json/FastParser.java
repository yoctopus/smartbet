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

public class FastParser implements Crud {
    private Config config;

    public FastParser(Config config) {
        this.config = config;
    }

    @Override
    public final <T> void loadList(JsonModel<T> model, Complete<List<T>> complete, Map<String, String> map) {
        model.onLoadList(config, complete, map);
    }

    @Override
    public final <T> void update(T t, JsonModel<T> model, Complete complete, Map<String, String> map) {
        model.onUpdate(config, t, complete, map);
    }

    @Override
    public final <T> void load(JsonModel<T> model,
                               Complete<T> complete,
                               Map<String, String> map) {
        model.onLoad(config,
                complete,
                map);
    }

    @Override
    public final void delete(JsonModel model,
                             Complete complete,
                             Map<String, String> map) {
        model.onDelete(config,
                complete,
                map);
    }

    @Override
    public final <T> void delete(List<T> list,
                                 JsonModel<T> model,
                                 Complete complete,
                                 Map<String, String> map) {
        model.onDelete(config,
                list,
                complete,
                map);
    }

    @Override
    public final <T> void save(T t,
                               JsonModel<T> model,
                               Complete complete,
                               Map<String, String> map) {
        model.onSave(config,
                t,
                complete,
                map);
    }

    @Override
    public final <T> void save(List<T> list,
                               JsonModel<T> model,
                               Complete complete,
                               Map<String, String> map) {
        model.onSave(config,
                list,
                complete,
                map);
    }
}
