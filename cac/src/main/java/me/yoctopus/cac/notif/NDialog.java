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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import me.yoctopus.cac.util.Func;


public class NDialog extends AlertDialog {

    private String title, message;
    private DButton positive, negative, neutral;
    private CListener cListener;
    private OnAnswer listener;

    public NDialog(Context context,
                   String title,
                   String message,
                   DButton positive,
                   DButton negative,
                   DButton neutral) {
        super(context);
        this.title = title;
        this.message = message;
        this.positive = positive;
        this.negative = negative;
        this.neutral = neutral;
        requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
    }
    public NDialog(Context context,
                   String title,
                   String message,
                   DButton positive,
                   DButton negative,
                   DButton neutral,
                   OnAnswer listener) {
        this(context,
                title,message,
                positive,
                negative,
                neutral);
        this.listener = listener;
    }


    public void setcListener(CListener cListener) {
        this.cListener = cListener;
    }

    public NDialog(Context context,
                   @StringRes int title,
                   @StringRes int message,
                   DButton positive,
                   DButton negative,
                   DButton neutral) {
        this(context,
                context.getResources().getString(title),
                context.getResources().getString(message),
                positive,
                negative,
                neutral);
    }

    @Override
    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        if (positive != null) {
            builder.setPositiveButton(positive.getText(),
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,
                                            int i) {
                            positive.getListener().onClick(null);
                        }
                    });
        }
        if (negative != null) {
            builder.setNegativeButton(negative.getText(),
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,
                                            int i) {
                            negative.getListener().onClick(null);
                        }
                    });
        }
        if (neutral != null) {
            builder.setNeutralButton(neutral.getText(),
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,
                                            int i) {
                            neutral.getListener().onClick(null);
                        }
                    });
        }

        if (cListener != null &&
                listener == null) {
            builder.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    cListener.onCancel(dialogInterface);
                }
            });
        }
        if (listener != null) {
            final EditText editText = new EditText(getContext());
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(Func.dpToPx(50, getContext().getResources()),
                    Func.dpToPx(0, getContext().getResources()),
                    Func.dpToPx(50, getContext().getResources()),
                    Func.dpToPx(0, getContext().getResources()));

            editText.setLayoutParams(params);
            builder.setView(editText);
            if (positive != null) {
                builder.setPositiveButton(positive.getText(),
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onAnswer(editText.getText().toString());
                            }
                        });
            }
        }
        builder.show();
    }

    public static class DButton {
        private String text;
        private BListener listener;
        public DButton(String text,
                       BListener listener) {
            this.text = text;
            this.listener = listener;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public BListener getListener() {
            return listener;
        }

        public void setListener(BListener listener) {
            this.listener = listener;
        }

        public interface BListener {
            void onClick(View v);
        }
    }
    public interface CListener {
        void onCancel(DialogInterface dialogInterface);
    }
    public interface OnAnswer {
        void onAnswer(String t);
    }
}
