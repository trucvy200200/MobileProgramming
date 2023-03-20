package hcmute.edu.vn.week6_P5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hcmute.edu.vn.week6_P5.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private RelativeLayout layoutBottom;
    private ImageView imgSong, imgPlayOrPause, imgClear;
    private TextView tvTitleSong, tvSingerSong;
    private Song mSong;
    private boolean isPlaying;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            mSong = (Song) bundle.get("object_song");
            isPlaying = bundle.getBoolean("status_player");
            int actionMusic = bundle.getInt("action_music");

            handleLayoutMusic(actionMusic);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("sendActionToActivity"));

        layoutBottom = binding.layoutBottom;
        imgSong = binding.imgSong;
        imgPlayOrPause = binding.imgPlayOrPause;
        imgClear = binding.imgClear;
        tvTitleSong = binding.tvSongName;
        tvSingerSong = binding.tvSingleSong;
    }

    public void clickStartService(View view) {
        Song song = new Song("Sweet Appetite", "Gawr Gura ft. Hakos Baelz", R.drawable.sweetappetite, R.raw.sweetappetite);

        // Start the service
        Intent intent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song);
        intent.putExtras(bundle);
        startService(intent);
    }

    public void clickStopService(View view) {
        // Stop the service
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void handleLayoutMusic(int actionMusic) {
        switch (actionMusic) {
            case MyService.ACTION_START:
                layoutBottom.setVisibility(View.VISIBLE);

                showInfoSong();
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_PAUSE:
            case MyService.ACTION_RESUME:
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_CLEAR:
                layoutBottom.setVisibility(View.GONE);
                break;
        }
    }

    private void showInfoSong() {
        if (mSong == null) {
            return;
        }


        imgSong.setImageResource(mSong.getImage());
        tvTitleSong.setText(mSong.getTitle());
        tvSingerSong.setText(mSong.getSingle());

        imgPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (isPlaying) {
                    sendActionToService(MyService.ACTION_PAUSE);
                } else {
                    sendActionToService(MyService.ACTION_RESUME);
                }
            }
        });

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionToService(MyService.ACTION_CLEAR);
            }
        });

    }
    private void setStatusButtonPlayOrPause() {
        if (isPlaying) {
            imgPlayOrPause.setImageResource(R.drawable.ic_pause);
        } else {
            imgPlayOrPause.setImageResource(R.drawable.ic_play);
        }
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("action_music_service", action);

        startService(intent);
    }


}