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

package me.yoctopus.cac.tx;

import android.content.Context;


public abstract class AsynchronousTx<T, X> extends Tx<T, X> {
    protected OnComplete<T> onComplete;
    public AsynchronousTx(Context context, int id) {
        super(context, id);
    }

    public void setOnComplete(OnComplete<T> onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    public void finalize(T t) {
        if (onComplete != null) {

        }
        super.finalize(t);
    }
    public interface OnComplete<T> {
        void onComplete(T t);
    }
}
