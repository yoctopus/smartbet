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

package me.yoctopus.cac.util;


import me.yoctopus.cac.BuildConfig;

public class LogUtil {
    private static final String PREFIX = "_";
    private static final int PREFIX_LENGTH = PREFIX.length();
    private static final int MAX_TAG_LENGTH = 20;
    public static String makeTag(String str) {
        if (str.length() > MAX_TAG_LENGTH - PREFIX_LENGTH) {
            return PREFIX + str.substring(0, MAX_TAG_LENGTH - PREFIX_LENGTH - 1);
        }

        return PREFIX + str;
    }

    public static String makeTag(Class cls) {
        return makeTag(cls.getSimpleName());
    }


    public static void v(String tag, Object... messages) {
        if (BuildConfig.DEBUG) {
            log(tag, android.util.Log.VERBOSE, null, messages);
        }
    }

    public static void d(String tag, Object... messages) {

        if (BuildConfig.DEBUG) {
            log(tag, android.util.Log.DEBUG, null, messages);
        }
    }

    public static void i(String tag, Object... messages) {
        log(tag, android.util.Log.INFO, null, messages);
    }

    public static void w(String tag, Object... messages) {
        log(tag, android.util.Log.WARN, null, messages);
    }

    public static void w(String tag, Throwable t, Object... messages) {
        log(tag, android.util.Log.WARN, t, messages);
    }

    public static void e(String tag, Object... messages) {
        log(tag, android.util.Log.ERROR, null, messages);
    }

    public static void e(String tag, Throwable t, Object... messages) {
        log(tag, android.util.Log.ERROR, t, messages);
    }

    private static void log(String tag, int level, Throwable t, Object... messages) {
        if (android.util.Log.isLoggable(tag, level)) {
            String message;
            if (t == null && messages != null && messages.length == 1) {
                message = messages[0].toString();
            } else {
                StringBuilder sb = new StringBuilder();
                if (messages != null) for (Object m : messages) {
                    sb.append(m);
                }
                if (t != null) {
                    sb.append("\n").append(
                            android.util.Log.getStackTraceString(t));
                }
                message = sb.toString();
            }
            android.util.Log.println(level, tag, message);
        }
    }
}
