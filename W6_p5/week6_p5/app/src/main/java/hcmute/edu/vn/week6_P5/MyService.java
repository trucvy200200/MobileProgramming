package hcmute.edu.vn.week6_P5;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RemoteViews;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.jetbrains.annotations.NotNull;

import hcmute.edu.vn.week6_P5.databinding.ActivityMainBinding;
import hcmute.edu.vn.week6_P5.databinding.LayoutCustomNotificationBinding;

public class MyService extends Service {

    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_CLEAR = 3;
    public static final int ACTION_START = 4;

//    LayoutCustomNotificationBinding binding;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private Song mSong;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyService", "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Song song = (Song) bundle.get("object_song");

            if (song != null) {
                mSong = song;
                startMusic(song);
                sendNotification(song);
                Log.e("MyService", "Sent notification: " + song.getTitle());
            }
        }

        int actionMusic = intent.getIntExtra("action_music_service", 0);
        Log.e("MyService", "onStartCommand: action " + actionMusic);
        handleActionMusic(actionMusic);


        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), song.getResource());
        }

        mediaPlayer.start();
        isPlaying = true;
        sendActionToActivity(ACTION_START);
    }

    private void handleActionMusic(int action) {
        switch (action) {
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();
                sendActionToActivity(ACTION_CLEAR);
                break;
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            sendNotification(mSong);
            sendActionToActivity(ACTION_PAUSE);
        }
    }

    private void resumeMusic() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(mSong);
            sendActionToActivity(ACTION_RESUME);
        }
    }

    private void sendNotification(Song song) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//        PendingIntent pendingIntent =  PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent =  PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), song.getImage());

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);

        remoteViews.setTextViewText(R.id.tv_song_name, song.getTitle());
        remoteViews.setTextViewText(R.id.tv_single_song, song.getSingle());
        remoteViews.setImageViewBitmap(R.id.img_song, bitmap);

        remoteViews.setImageViewResource(R.id.imgPlayOrPause, R.drawable.ic_pause);

        Log.e("MyService", "sendNotification before onClick: " + isPlaying);

        if (isPlaying) {
            Log.e("MyService", "sendNotification: isPlaying");
            remoteViews.setOnClickPendingIntent(R.id.imgPlayOrPause, getPendingIntent(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.imgPlayOrPause, R.drawable.ic_pause);
        } else {
            Log.e("MyService", "sendNotification: isPause");
            remoteViews.setOnClickPendingIntent(R.id.imgPlayOrPause, getPendingIntent(this, ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.imgPlayOrPause, R.drawable.ic_play);
        }

        remoteViews.setOnClickPendingIntent(R.id.img_clear, getPendingIntent(this, ACTION_CLEAR));
        Log.e("MyService", "sendNotification remoteViews: " + "Clear");

        Notification notification = new Notification.Builder(this, MyApplication.CHANNEL_ID)
                .setCustomBigContentView(remoteViews)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    private PendingIntent getPendingIntent(@NotNull Context context, int action) {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("action_music", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MyService", "onDestroy");

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void sendActionToActivity(int action) {
        Intent intent = new Intent("sendActionToActivity");
        Bundle bundle = new Bundle();

        bundle.putSerializable("object_song", mSong);
        bundle.putBoolean("status_player", isPlaying);
        bundle.putInt("action_music", action);

        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
}
