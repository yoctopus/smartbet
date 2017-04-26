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

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


class ItemAdapter extends Adapter<Tips> {
    private final MainActivity.OnInteractionListener listener;
    private ArrayList<Integer> drawables;
    private TextView league;
    private TextView time;
    private TextView teamA;
    private TextView teamB;
    private TextView prediction;
    private NetworkImageView background, flag_icon;
    private Button like, dislike;
    ItemAdapter(List<Tips> list,
                MainActivity.OnInteractionListener listener) {
        super(list, R.layout.fragment_tips);
        this.listener = listener;
        drawables = new ArrayList<>();
        drawables.add(R.drawable.hd2);
        drawables.add(R.drawable.hd3);
        drawables.add(R.drawable.hd4);
    }

    @Override
    public void onInitViews(View parent) {
        league = (TextView) parent.findViewById(R.id.league_textview);
        time = (TextView) parent.findViewById(R.id.time_textview);
        teamA = (TextView) parent.findViewById(R.id.ta_textview);
        teamB = (TextView) parent.findViewById(R.id.tb_textview);
        prediction = (TextView) parent.findViewById(R.id.prediction_textview);
        background = (NetworkImageView) parent.findViewById(R.id.background_imageview);
        flag_icon = (NetworkImageView) parent.findViewById(R.id.flag_icon);
        like = (Button) parent.findViewById(R.id.like_button);
        dislike = (Button) parent.findViewById(R.id.dislike_button);
    }

    @Override
    public void onBind(final Tips tips) {
        prediction.setText(tips.getPrediction());
        teamB.setText(tips.getTeamB());
        teamA.setText(tips.getTeamA());
        time.setText(tips.getDateTime());
        time.setText(getTime(tips.getTime()));
        league.setText(tips.getLeague());
        loadBackground(background,
                tips.getBackgroung_url());
        like.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onLike(tips);
                    }
                });
        dislike.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDislike(tips);
                    }
                });
        loadFlag(flag_icon, tips.getFlag_name());
    }
    private String getTime(Date date) {
        return new SimpleDateFormat("MM/dd-HH:mm",
                Locale.ENGLISH)
                .format(date);
    }
    private void loadBackground(NetworkImageView imageView,
                                String url) {
        int n = new Random().nextInt(drawables.size());
        imageView.setDefaultImageResId(drawables.get(n));
        imageView.setImageUrl(url,
                SmartTipsApp.getInstance().getImageLoader());

    }
    private void loadFlag(NetworkImageView imageView, String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        String url = "http://greenbelemyafrica.com/picbetting/"+name;

        imageView.setImageUrl(url,
                SmartTipsApp.getInstance().getImageLoader());
    }
}
