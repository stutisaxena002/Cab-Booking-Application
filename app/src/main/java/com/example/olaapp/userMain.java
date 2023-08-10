package com.example.olaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

public class userMain extends AppCompatActivity {

    VideoView videoView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        videoView=findViewById(R.id.videoView);
        mAuth=FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String path="android.resource://" + getPackageName() + "/" + R.raw.splashvideo;

        Uri uri=Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
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
                    i=new Intent(userMain.this,mapUser.class);
                }
                else {
                    i=new Intent(userMain.this,loginUser.class);
                }// the current activity will get finished.
                startActivity(i);
                finish();
            }
        }, 4000);
    }
}