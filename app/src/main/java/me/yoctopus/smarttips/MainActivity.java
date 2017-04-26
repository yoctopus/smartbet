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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.yoctopus.cac.notif.NDialog;
import me.yoctopus.cac.notif.Notification;
import me.yoctopus.cac.tx.Tx;
import me.yoctopus.smarttips.m.AppDatabase;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    TextView date;
    InterstitialAd mInterstitialAd;
    AdView topAd;
    AppDatabase database;
    private OnInteractionListener listener;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setLogo(R.drawable.football_logo);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = (RecyclerView)
                findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout)
                findViewById(R.id.refresh_list);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadTips();
                    }
                });
        date = (TextView)
                findViewById(R.id.date_textview);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        date.setText(new SimpleDateFormat("MMM" +
                " dd",
                Locale.getDefault())
                .format(new Date()));
        topAd = (AdView) findViewById(R.id.ad_top);
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
        topAd.loadAd(adRequest);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        listener = new OnInteractionListener() {
            @Override
            public void onLike(Tips tips) {
            }

            @Override
            public void onDislike(Tips tips) {

            }
        };
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =
                connMgr.getActiveNetworkInfo();
        if (networkInfo != null &&
                networkInfo.isConnected()) {
            refreshLayout.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(
                                    true);
                            loadTips();
                            requestNewInterstitial();
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
                            }), null, null);
        }
    }

    private void loadTips() {
        FetchTips fetchTips = new FetchTips(this, 100);
        fetchTips.setOnComplete(
                new Tx.OnComplete<Tips.TipsData>() {
            @Override
            public void onComplete(int id, Tips.TipsData tipsData) {
                if (tipsData == null) {
                    notifyNoTips();
                    return;
                }
                if (!tipsData.getTipses().isEmpty()) {
                    listTips(new ArrayList<>(
                            tipsData.getTipses()));
                }
                else {
                    notifyNoTips();
                }
                refreshLayout.setRefreshing(false);
            }
        });
        fetchTips.execute();
    }
    private void notifyNoTips() {
        Notification notification = new Notification(this);
        notification.showDialog("Message",
                "There are no tips at the moment," +
                        "kindly check back later", null, null, null);
    }


    private void listTips(ArrayList<Tips> list) {
        if (adapter == null) {
            adapter = new ItemAdapter(list, listener);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (topAd != null) {
            topAd.resume();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (topAd != null) {
            topAd.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (topAd != null) {
            topAd.destroy();
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
                        "http:greenbelemyafrica.com/" +
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
                        "http:greenbelemyafrica.com/" +
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

    interface OnInteractionListener {
        void onLike(Tips tips);

        void onDislike(Tips tips);
    }
}
