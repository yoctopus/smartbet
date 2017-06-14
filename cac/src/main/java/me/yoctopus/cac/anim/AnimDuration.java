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

public class AnimDuration {

    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static AnimDuration ofMilliSeconds(int millis) {
        return newType(millis);
    }

    public static AnimDuration ofSeconds(int seconds) {
        int millis = seconds * 1000;
        return ofMilliSeconds(millis);
    }

    public static AnimDuration ofMinutes(int minutes) {
        int seconds = minutes * 60;
        return ofSeconds(seconds);
    }
    public static AnimDuration noDuration() {
        return ofMinutes(0);
    }
    public static AnimDuration standard() {
        return ofSeconds(3);
    }

    public static AnimDuration ofSecond() {
        return ofSeconds(1);
    }

    public static AnimDuration ofHalfSecond() {
        return small();
    }

    public static AnimDuration ofMoreThanHalfSecond() {
        return ofMilliSeconds(750);
    }

    public static AnimDuration ofLessThanHalfSecond() {
        return ofMilliSeconds(250);
    }

    public static AnimDuration small() {
        return ofMilliSeconds(500);
    }

    public static AnimDuration big() {
        return ofSeconds(5);
    }

    public void addDuration(AnimDuration duration) {
        this.time += duration.getTime();
    }

    public void addSameDuration() {
        this.time += time;

    }
    private static AnimDuration newType(int time) {
        AnimDuration duration = new AnimDuration();
        duration.setTime(time);
        return duration;
    }
}
