package com.example.week8_accessmp3;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.PermissionRequest;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.week8_accessmp3.databinding.ActivityReadMp3Binding;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class ReadMp3 extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher;
    String [] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    ListView listView;
    ArrayList<String> mp3files = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    ActivityReadMp3Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadMp3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listView = binding.listView;
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            Toast.makeText(ReadMp3.this, "Permission granted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReadMp3.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        if (checkPermission()) {
            File dir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("Music")));
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".mp3");
                }
            });
            for (File file : files) {
                mp3files.add(file.getAbsolutePath());
            }
            Log.e("mp3files", mp3files.toString());
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mp3files);
            listView.setAdapter(adapter);
        } else {
            requestPermission();
        }
    }

    void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(android.net.Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                activityResultLauncher.launch(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            }
        } else {
            ActivityCompat.requestPermissions(ReadMp3.this, permissions, 30);
        }
    }

    boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
//            int readPerms = getApplicationContext().checkCallingOrSelfPermission(READ_EXTERNAL_STORAGE);
//            int writePerms = getApplicationContext().checkCallingOrSelfPermission(WRITE_EXTERNAL_STORAGE);
            int readPerms = ActivityCompat.checkSelfPermission(ReadMp3.this, READ_EXTERNAL_STORAGE);
            int writePerms = ActivityCompat.checkSelfPermission(ReadMp3.this, WRITE_EXTERNAL_STORAGE);

            return readPerms == getPackageManager().PERMISSION_GRANTED && writePerms == getPackageManager().PERMISSION_GRANTED;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case 30:
                if (grantResults.length > 0) {
                    boolean readPerms = grantResults[0] == getPackageManager().PERMISSION_GRANTED;
                    boolean writePerms = grantResults[1] == getPackageManager().PERMISSION_GRANTED;

                    if (readPerms && writePerms) {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

}
