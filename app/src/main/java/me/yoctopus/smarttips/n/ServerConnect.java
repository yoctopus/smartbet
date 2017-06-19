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

package me.yoctopus.smarttips.n;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import me.yoctopus.json.Complete;
import me.yoctopus.json.Config;
import me.yoctopus.json.FastParser;
import me.yoctopus.smarttips.Tips;


public class ServerConnect extends FastParser {
    private static final String url =
            "http://bait-technologies.com";
    private static OnlineTips onlineTips;
    static {
        onlineTips = new OnlineTips();
    }
    public ServerConnect(Context context) {
        super(Config.create(context, url));
    }
    public void getTips(Complete<List<Tips>> onComplete) {
        loadList(onlineTips, onComplete, new HashMap<String, String>());
    }
}
