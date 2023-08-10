package com.example.olaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    VideoView videoView2;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        videoView2=findViewById(R.id.videoView2);
        mAuth=FirebaseAuth.getInstance();

        String path="android.resource://" + getPackageName() + "/" + R.raw.splashvideo;

        Uri uri=Uri.parse(path);
        videoView2.setVideoURI(uri);
        videoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i;

                if (mAuth.getCurrentUser()!=null){
                    i=new Intent(MainActivity.this,mapDriver.class);
                }
                else {
                    i=new Intent(MainActivity.this,loginDriver.class);
                }// the current activity will get finished.
                startActivity(i);
                finish();
            }
        }, 4000);
    }
}