package com.example.boundserviceproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MusicBoundService mMusicBoundService;
    private boolean  isServiceConnected;
    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicBoundService.MyBinder myBinder = (MusicBoundService.MyBinder) iBinder;
            mMusicBoundService=myBinder.getMusicBoundService();
            mMusicBoundService.startMusic();
            isServiceConnected=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceConnected=false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStartService=findViewById(R.id.btn_start_service);
        Button btnStopService=findViewById(R.id.btn_stop_service);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStartService();
            }
        });
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStopService();
            }
        });
    }

    private void onClickStopService() {
        if (isServiceConnected){
            unbindService(mServiceConnection);
            isServiceConnected=false;
        }
    }

    private void onClickStartService() {
        Intent intent=new Intent(this, MusicBoundService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

}