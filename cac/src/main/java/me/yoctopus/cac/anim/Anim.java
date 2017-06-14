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

import android.content.Context;
import android.support.annotation.AnimRes;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.daimajia.androidanimations.library.Techniques;

public class Anim {
    private static final int TECHNIQUE = 1;
    private static final int ANIMATION = 2;
    private static final int CUSTOM = 3;
    private Techniques techniques;
    private Animation animation;
    private CustomAnimation customAnimation;
    private int type;

    private Anim(Animation animation) {
        this.animation = animation;
        type = ANIMATION;
    }

    private Anim(Techniques techniques) {
        this.techniques = techniques;
        type = TECHNIQUE;
    }

    public Anim(CustomAnimation customAnimation) {
        this.customAnimation = customAnimation;
        type = CUSTOM;
    }

    public static Anim load(Context context,
                            @AnimRes int resID) {
        return newAnimation(context, resID);
    }

    public static Anim load(CustomAnimation customAnimation) {
        return newAnimation(customAnimation);
    }

    private static Anim newAnimation(Techniques type) {
        return new Anim(type);
    }

    private static Anim newAnimation(Context context,
                                     @AnimRes int resID) {
        return new Anim(AnimationUtils.loadAnimation(context,
                resID));
    }

    private static Anim newAnimation(CustomAnimation customAnimation) {
        return new Anim(customAnimation);
    }

    protected boolean isTechnique() {
        return type == TECHNIQUE;
    }

    protected boolean isAnimation() {
        return type == ANIMATION;
    }

    protected boolean isCustom() {
        return type == CUSTOM;
    }

    Techniques getTechnique() {
        return techniques;
    }


    public Animation getAnimation() {
        return animation;
    }


    public CustomAnimation getCustomAnimation() {
        return customAnimation;
    }

    public interface CustomAnimation {
        void doCustom();
    }

    public static class Attention {

        public static Anim flash() {
            return newAnimation(Techniques.Flash);
        }

        public static Anim pulse() {
            return newAnimation(Techniques.Pulse);
        }

        public static Anim rubberBand() {
            return newAnimation(Techniques.RubberBand);
        }

        public static Anim shake() {
            return newAnimation(Techniques.Shake);
        }

        public static Anim swing() {
            return newAnimation(Techniques.Swing);
        }

        public static Anim wobble() {
            return newAnimation(Techniques.Wobble);
        }

        public static Anim standUp() {
            return newAnimation(Techniques.StandUp);
        }

        public static Anim tada() {
            return newAnimation(Techniques.Tada);
        }

        public static Anim wave() {
            return newAnimation(Techniques.Wave);
        }
    }

    public static class Special {
        public static Anim hinge() {
            return newAnimation(Techniques.Hinge);
        }

        public static Anim rollIn() {
            return newAnimation(Techniques.RollIn);
        }

        public static Anim rollOut() {
            return newAnimation(Techniques.RollOut);
        }

        public static Anim landing() {
            return newAnimation(Techniques.Landing);
        }

        public static Anim takingOff() {
            return newAnimation(Techniques.TakingOff);
        }

        public static Anim dropOut() {
            return newAnimation(Techniques.DropOut);
        }
    }

    public static class Bounce {
        public static Anim bounceIn() {
            return newAnimation(Techniques.BounceIn);
        }

        public static Anim bounceInDown() {
            return newAnimation(Techniques.BounceInDown);
        }

        public static Anim bounceInLeft() {
            return newAnimation(Techniques.BounceInLeft);
        }

        public static Anim bounceInRight() {
            return newAnimation(Techniques.BounceInRight);
        }

        public static Anim bounceInUp() {
            return newAnimation(Techniques.BounceInUp);
        }
    }

    public static class Fade {
        public static Anim fadeIn() {
            return newAnimation(Techniques.FadeIn);
        }

        public static Anim fadeInUp() {
            return newAnimation(Techniques.FadeInUp);
        }

        public static Anim fadeInDown() {
            return newAnimation(Techniques.FadeInDown);
        }

        public static Anim fadeInLeft() {
            return newAnimation(Techniques.FadeInLeft);
        }

        public static Anim fadeInRigh() {
            return newAnimation(Techniques.FadeInRight);
        }

        public static Anim fadeOut() {
            return newAnimation(Techniques.FadeOut);
        }

        public static Anim fadeOutDown() {
            return newAnimation(Techniques.FadeOutDown);
        }

        public static Anim fadeOutLeft() {
            return newAnimation(Techniques.FadeOutLeft);
        }

        public static Anim fadeOutRight() {
            return newAnimation(Techniques.FadeOutRight);
        }

        public static Anim fadeOutUp() {
            return newAnimation(Techniques.FadeOutUp);
        }
    }

    public static class Flip {
        public static Anim flipInX() {
            return newAnimation(Techniques.FlipInX);
        }

        public static Anim flipInY() {
            return newAnimation(Techniques.FlipInX);
        }

        public static Anim flipOutX() {
            return newAnimation(Techniques.FlipOutX);
        }

        public static Anim flipOutY() {
            return newAnimation(Techniques.FlipOutY);
        }
    }

    public static class Rotate {
        public static Anim rotateIn() {
            return newAnimation(Techniques.RotateIn);
        }

        public static Anim rotateInDownLeft() {
            return newAnimation(Techniques.RotateInDownLeft);
        }

        public static Anim rotateInDownRight() {
            return newAnimation(Techniques.RotateInDownRight);
        }

        public static Anim rotateInUpLeft() {
            return newAnimation(Techniques.RotateInUpLeft);
        }

        public static Anim rotateInUpRight() {
            return newAnimation(Techniques.RotateInUpRight);
        }

        public static Anim rotateOut() {
            return newAnimation(Techniques.RotateOut);
        }

        public static Anim rotateOutDownLeft() {
            return newAnimation(Techniques.RotateOutDownLeft);
        }

        public static Anim rotateOutDownRight() {
            return newAnimation(Techniques.RotateOutDownRight);
        }

        public static Anim rotateOutUpLeft() {
            return newAnimation(Techniques.RotateOutUpLeft);
        }

        public static Anim rotateOutUpRight() {
            return newAnimation(Techniques.RotateOutUpRight);
        }
    }

    public static class Slide {
        public static Anim slideInLeft() {
            return newAnimation(Techniques.SlideInLeft);
        }

        public static Anim slideInRight() {
            return newAnimation(Techniques.SlideInRight);
        }

        public static Anim slideInUp() {
            return newAnimation(Techniques.SlideInUp);
        }

        public static Anim slideInDown() {
            return newAnimation(Techniques.SlideInDown);
        }

        public static Anim slideOutUp() {
            return newAnimation(Techniques.SlideOutUp);
        }

        public static Anim slideOutDown() {
            return newAnimation(Techniques.SlideOutDown);
        }

        public static Anim slideOutLeft() {
            return newAnimation(Techniques.SlideOutLeft);
        }

        public static Anim slideOutRight() {
            return newAnimation(Techniques.SlideOutRight);
        }
    }

    public static class Zoom {
        public static Anim zoomIn() {
            return newAnimation(Techniques.ZoomIn);
        }

        public static Anim zoomInDown() {
            return newAnimation(Techniques.ZoomInDown);
        }

        public static Anim zoomInLeft() {
            return newAnimation(Techniques.ZoomInLeft);
        }

        public static Anim zoomInRight() {
            return newAnimation(Techniques.ZoomInRight);
        }

        public static Anim zoomInUp() {
            return newAnimation(Techniques.ZoomInUp);
        }

        public static Anim zoomOut() {
            return newAnimation(Techniques.ZoomOut);
        }

        public static Anim zoomOutDown() {
            return newAnimation(Techniques.ZoomOutDown);
        }

        public static Anim zoomOutLeft() {
            return newAnimation(Techniques.ZoomOutLeft);
        }

        public static Anim zoomOutRight() {
            return newAnimation(Techniques.ZoomOutRight);
        }

        public static Anim zoomOutUp() {
            return newAnimation(Techniques.ZoomOutUp);
        }
    }
}
