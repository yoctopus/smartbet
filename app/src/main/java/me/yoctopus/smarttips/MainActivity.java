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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.yoctopus.cac.notif.NDialog;
import me.yoctopus.cac.notif.Notification;
import me.yoctopus.json.Complete;
import me.yoctopus.json.Config;
import me.yoctopus.smarttips.m.AppDatabase;
import me.yoctopus.smarttips.n.ServerConnect;

public class MainActivity extends AppCompatActivity {
    protected NetworkImageView backgroundImage;
    protected AdView adTop;
    protected TextView dateTextview;
    protected RecyclerView list;
    protected SwipeRefreshLayout refreshList;
    InterstitialAd mInterstitialAd;
    AppDatabase database;
    private OnInteractionListener listener;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setLogo(R.drawable.football_logo);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        refreshList.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadTips();
                    }
                });
        dateTextview = (TextView)
                findViewById(R.id.date_textview);
        list.setLayoutManager(
                new LinearLayoutManager(this));
        dateTextview.setText(new SimpleDateFormat("MMM" +
                " dd",
                Locale.getDefault())
                .format(new Date()));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(
                getResources().getString(
                        R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(
                new AdListener() {

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        mInterstitialAd.show();
                    }
                });
        database = new AppDatabase(this);

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
        adTop.loadAd(adRequest);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        listener = new OnInteractionListener() {
            @Override
            public void onLike(Tips tips) {
                //TODO
            }

            @Override
            public void onDislike(Tips tips) {
                //TODO
            }
        };
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null &&
                networkInfo.isConnected()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshList.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    refreshList.setRefreshing(
                                            true);
                                    loadTips();
                                }
                            },
                            1000);
                }
            });

        } else {
            Notification notification = new Notification(this);
            notification.showDialog("Network Request",
                    "Please enable network " +
                            "connection in order" +
                            "to retrieve betting tips",
                    new NDialog.DButton("Enable",
                            new NDialog.DButton.BListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent =
                                            new Intent(
                                                    Settings
                                                            .ACTION_DATA_ROAMING_SETTINGS);
                                    startActivity(
                                            intent);

                                }
                            }),
                    null,
                    null);
        }
    }

    private void loadBackground(NetworkImageView imageView, String url) {

        imageView.setImageUrl(url,
                Config.getInstance().getImageLoader());
    }

    private void loadTips() {
        ServerConnect connect = new ServerConnect(this);
        connect.getTips(new Complete<List<Tips>>() {
            @Override
            public void complete(List<Tips> tipses) {
                if (tipses == null || tipses.isEmpty()) {
                    notifyNoTips();
                    return;
                }
                listTips(new ArrayList<>(tipses));
                refreshList.setRefreshing(false);
                requestNewInterstitial();
            }
        });
    }

    private void notifyNoTips() {
        Notification notification = new Notification(this);
        notification.showDialog("Message",
                "There are no tips at the moment, kindly check back later",
                null,
                null,
                null);
    }


    private void listTips(ArrayList<Tips> list) {
        if (adapter == null) {
            adapter = new ItemAdapter(list, listener);
        }
        MainActivity.this.list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adTop != null) {
            adTop.resume();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adTop != null) {
            adTop.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adTop != null) {
            adTop.destroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,
                menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share: {
                Intent intent = new Intent(
                        Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "https://play.google.com/store/" +
                                "apps/details?id=me.yoctopus.smarttips");
                startActivity(intent);
                startActivity(Intent.createChooser(intent,
                        "Share with..."));
                break;
            }
            case R.id.action_eula: {
                Intent intent =
                        new Intent(this,
                                WebActivity.class);
                intent.putExtra(WebActivity.TITLE,
                        "Eula");
                intent.putExtra(WebActivity.URL,
                        "http:bait-technologies.com/" +
                                "bet_eula.html");
                startActivity(intent);
                break;
            }
            case R.id.action_credits: {
                Intent intent =
                        new Intent(this,
                                WebActivity.class);
                intent.putExtra(WebActivity.TITLE,
                        "Credits");
                intent.putExtra(WebActivity.URL,
                        "http:bait-technologies.com/" +
                                "bet_credits.html");
                startActivity(intent);
                break;
            }
            case R.id.action_exit: {
                finish();
                break;
            }
            case android.R.id.home: {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        backgroundImage = (NetworkImageView) findViewById(R.id.background_image);
        adTop = (AdView) findViewById(R.id.ad_top);
        dateTextview = (TextView) findViewById(R.id.date_textview);
        list = (RecyclerView) findViewById(R.id.list);
        refreshList = (SwipeRefreshLayout) findViewById(R.id.refresh_list);
    }

    interface OnInteractionListener {
        void onLike(Tips tips);

        void onDislike(Tips tips);
    }
}
