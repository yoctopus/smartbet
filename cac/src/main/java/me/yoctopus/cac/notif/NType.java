/*
 * Copyright 2017, Peter Vincent
 * Licensed under the Apache License, Version 2.0, Wallet.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.cac.notif;

/**
 * Created by yoctopus on 11/13/16.
 */

public enum NType {
    WARNING(1),
    ERROR(2),
    FAILURE(3),
    SUCCESS(4),
    INFO(5);
    static final int WARN = 1;
    static final int ERR = 2;
    static final int FAIL = 3;
    static final int SUCC = 4;
    static final int INF = 5;
    private final int type;



    NType(int t) {
        type = t;
    }

    public int getType() {
        return type;
    }
}
