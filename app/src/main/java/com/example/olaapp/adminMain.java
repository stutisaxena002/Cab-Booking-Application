package com.example.olaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

public class adminMain extends AppCompatActivity {
    VideoView videoView3;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        videoView3=findViewById(R.id.videoView3);
        mAuth=FirebaseAuth.getInstance();

        String path="android.resource://" + getPackageName() + "/" + R.raw.splashvideo;

        Uri uri=Uri.parse(path);
        videoView3.setVideoURI(uri);
        videoView3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i;


                i=new Intent(adminMain.this,adminLogin.class);

                startActivity(i);
                finish();
            }
        }, 4000);
    }
}