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

import android.content.res.Resources;
import android.util.TypedValue;

public class Func {

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp,
                             Resources resources) {
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics());
        return (int) px;
    }
}
