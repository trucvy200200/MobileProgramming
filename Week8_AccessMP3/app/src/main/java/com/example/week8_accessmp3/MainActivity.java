package com.example.week8_accessmp3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.week8_accessmp3.databinding.ActivityReadMp3Binding;

public class MainActivity extends AppCompatActivity {

    ActivityReadMp3Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityReadMp3Binding.inflate(getLayoutInflater());
////        setContentView(R.layout.activity_main);
//        setContentView(binding.getRoot());

        // redirect to ReadMp3
        setContentView(R.layout.activity_read_mp3);

    }

}