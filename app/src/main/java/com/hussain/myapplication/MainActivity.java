package com.hussain.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static Cache mCache;
    @BindView(R.id.video_view)
    PlayerView mPlayerView;

    public static synchronized Cache getCache(Context context) {
        if (mCache == null) {
            File cacheDirectory = new File(context.getExternalFilesDir(null), "video");
            mCache = new SimpleCache(cacheDirectory, new NoOpCacheEvictor());
        }
        return mCache;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)));
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(
                getCache(this),
                dataSourceFactory,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(uri);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        mPlayerView.setPlayer(simpleExoPlayer);
    }
}
