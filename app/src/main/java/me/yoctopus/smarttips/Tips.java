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

package me.yoctopus.smarttips;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yoctopus on 2/18/17.
 */

public class Tips implements Parcelable {
    public static final Creator<Tips> CREATOR = new Creator<Tips>() {
        @Override
        public Tips createFromParcel(Parcel source) {
            return new Tips(source);
        }

        @Override
        public Tips[] newArray(int size) {
            return new Tips[size];
        }
    };
    private int id;
    private String league;
    private Date time;
    private String teamA;
    private String teamB;
    private String prediction;
    private String backgroung_url;
    private String dateTime;
    private String flag_name;


    public Tips() {
    }

    protected Tips(Parcel in) {
        this.id = in.readInt();
        this.league = in.readString();
        long tmpTime = in.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        this.teamA = in.readString();
        this.teamB = in.readString();
        this.prediction = in.readString();
        this.backgroung_url = in.readString();
        this.dateTime = in.readString();
        this.flag_name = in.readString();
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getBackgroung_url() {
        return backgroung_url;
    }

    public void setBackgroung_url(String backgroung_url) {
        this.backgroung_url = backgroung_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFlag_name() {
        return flag_name;
    }

    public void setFlag_name(String flag_name) {
        this.flag_name = flag_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Tips{" +
                "id=" + id +
                ", league='" + league + '\'' +
                ", time=" + time +
                ", teamA='" + teamA + '\'' +
                ", teamB='" + teamB + '\'' +
                ", prediction='" + prediction + '\'' +
                ", backgroung_url='" + backgroung_url + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", flag_name='" + flag_name + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);

        dest.writeString(this.league);
        dest.writeLong(this.time != null ? this.time.getTime() : -1);
        dest.writeString(this.teamA);
        dest.writeString(this.teamB);
        dest.writeString(this.prediction);
        dest.writeString(this.backgroung_url);
        dest.writeString(this.dateTime);
        dest.writeString(this.flag_name);
    }

    public static class TipsData {
        private List<Tips> tipses;

        public TipsData() {
            tipses = new ArrayList<>();
        }

        public List<Tips> getTipses() {
            return tipses;
        }

        public void setTipses(List<Tips> tipses) {
            this.tipses = tipses;
        }
    }
}
