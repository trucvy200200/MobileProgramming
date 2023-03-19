package com.example.backgroundservice;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyService extends IntentService {
    /**
     * @param name
     * @deprecated
     */
    public MyService() {
        super("my_intent_thread");
    }



    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(MyService.this,"service started", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(MyService.this,"service destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        synchronized (this){
            try{
                wait(20000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
