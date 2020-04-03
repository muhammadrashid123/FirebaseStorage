package com.hybird.firebasestorage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class DownloadPage extends AppCompatActivity {

    private VideoView videoV;




    private static final int REQUEST_CODE=124;
    private MediaController mc;
    private Uri videoUri;
    private StorageReference videoRef;

    private FirebaseFirestore objectFirebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_page);
        videoV=findViewById(R.id.videoV);
        videoV.setVideoURI(videoUri);
        videoV.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mc = new MediaController(DownloadPage.this);
                        videoV.setMediaController(mc);
                        mc.setAnchorView(videoV);
                    }
                });
            }
        });
    videoV.start();
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try

        {
            if(REQUEST_CODE==requestCode && resultCode ==RESULT_OK && data !=null) {
                videoUri = data.getData();
                videoV.setVideoURI(videoUri);


            }
        }
        catch (Exception e) {
            Toast.makeText(this, "onActivityResult:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}
