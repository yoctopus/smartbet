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

package me.yoctopus.cac.anim;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.daimajia.androidanimations.library.YoYo;

public class Animator {
    private Anim anim;
    private View view;
    private Context context;
    private boolean repeat = false;
    private AnimatorListener animatorListener = null;
    private AnimDuration animDuration =
            AnimDuration.ofMilliSeconds(100);
    private AnimDuration waitDuration =
            AnimDuration.ofLessThanHalfSecond();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (getAnim().isAnimation()) {

                if (animatorListener != null) {
                    animatorListener.onStartAnimator(
                            Animator.this);
                }

                getView().startAnimation(
                        getAnim().getAnimation());
                if (animatorListener != null) {
                    animatorListener.onStopAnimator(
                            Animator.this);
                }
            } else if (getAnim().isTechnique()) {
                if (animatorListener != null) {
                    animatorListener.onStartAnimator(
                            Animator.this);
                }
                if (!isRepeat()) {
                    doAnimation(getView(),
                            getAnim(),
                            getAnimDuration());

                } else {
                    doRepeatAnimation(getView(),
                            getAnim(),
                            getAnimDuration());
                    if (animatorListener != null) {
                        animatorListener.onRepeatAnimator(
                                Animator.this);
                    }
                }
                if (animatorListener != null) {
                    animatorListener.onStopAnimator(
                            Animator.this);
                }
            } else if (getAnim().isCustom()) {
                getAnim().getCustomAnimation().doCustom();
            }
        }
    };

    public Animator() {
    }

    public Animator(View view,
                    Anim anim) {
        setView(view);
        setAnim(anim);
    }

    public Animator(View view,
                    Anim anim,
                    AnimDuration duration) {
        this.setAnim(anim);
        this.setView(view);
        this.setAnimDuration(duration);
    }

    public Animator(Activity activity,
                    Anim anim) {
        this(activity.findViewById(
                android.R.id.content),
                anim);
    }

    public Animator(Activity activity,
                    Anim anim,
                    AnimDuration duration) {
        this(activity,
                anim);
        this.setAnimDuration(duration);
    }

    public Animator addView(View view) {
        this.view = view;
        return this;
    }

    public Animator addAnimDuration(AnimDuration duration) {
        this.animDuration = duration;
        return this;
    }

    public Animator addAnim(Anim anim) {
        this.anim = anim;
        return this;
    }

    public Animator addRepeating() {
        this.setRepeat(true);
        return this;
    }

    public Animator addWaitDuration(AnimDuration duration) {
        this.waitDuration = duration;
        return this;
    }

    public Animator addAnimatorListener(AnimatorListener listener) {
        this.setAnimatorListener(listener);
        return this;
    }

    public void addDuration(AnimDuration duration) {
        this.getWaitDuration().setTime(
                this.getWaitDuration().getTime() +
                        duration.getTime());
    }

    public void addSameDuration() {
        addDuration(this.waitDuration);
    }

    protected Anim getAnim() {
        return anim;
    }

    public void setAnim(Anim anim) {
        this.anim = anim;
    }

    public void setRepeating() {
        setRepeat(true);
    }

    public void unRepeat() {
        setRepeat(false);
    }

    protected View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    protected Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected AnimDuration getAnimDuration() {
        return animDuration;
    }

    public void setAnimDuration(AnimDuration duration) {
        this.animDuration = duration;
    }

    protected AnimatorListener getAnimatorListener() {
        return animatorListener;
    }

    public void setAnimatorListener(AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public void animate() {
        Handler handler = new Handler();
        handler.postDelayed(getRunnable(),
                getWaitDuration().getTime());
    }

    public void doAnimation(View view,
                            Anim anim,
                            AnimDuration duration) {
        if (view == null) {
            return;
        }
        YoYo.with(
                anim.getTechnique())
                .duration(duration.getTime())
                .playOn(view);
    }

    public void doRepeatAnimation(final View view,
                                  final Anim anim,
                                  final AnimDuration duration) {
        view.setVisibility(View.VISIBLE);
        animatorListener = new AnimatorListener() {
            @Override
            public void onStartAnimator(Animator animator) {
                setRepeat(false);
            }

            @Override
            public void onRepeatAnimator(Animator animator) {

            }

            @Override
            public void onStopAnimator(Animator animator) {
                doAnimation(view,
                        anim,
                        duration);
                animator.setAnimatorListener(this);
                animator.animate();
            }
        };
        this.setAnimatorListener(animatorListener);
        animate();
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public AnimDuration getWaitDuration() {
        return waitDuration;
    }

    public void setWaitDuration(AnimDuration waitDuration) {
        this.waitDuration = waitDuration;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public interface AnimatorListener {
        void onStartAnimator(Animator animator);

        void onRepeatAnimator(Animator animator);

        void onStopAnimator(Animator animator);

    }
}
