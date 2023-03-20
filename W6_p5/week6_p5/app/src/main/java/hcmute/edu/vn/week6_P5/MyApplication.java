package hcmute.edu.vn.week6_P5;

import android.util.Log;

public class MyApplication extends android.app.Application {

    public static final String CHANNEL_ID = "MediaPlayerChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }
    private void createChannelNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                    CHANNEL_ID,
                    "Media Player Channel",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setSound(null, null);

            android.app.NotificationManager manager = getSystemService(android.app.NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                Log.e("MyApplication", "createChannelNotification");
            }
        }
    }
}
