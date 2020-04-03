package com.hybird.firebasestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.sip.SipSession;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;

import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;

public class UploadScreen extends AppCompatActivity {

    private VideoView videoToUploadIV;
    private EditText videoNameET;

    private Button videoUploadingBtn;
    private static final int REQUEST_CODE=124;
    private MediaController mc;
    private Uri videoUri;
    private StorageReference videoRef;

    private FirebaseFirestore objectFirebaseFirestore;
    private boolean isVideoSelected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_screen);

        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        videoRef= FirebaseStorage.getInstance().getReference("myVideos");
        connectXMLToJava();
    }

    private void connectXMLToJava() {
        try {
            videoToUploadIV = findViewById(R.id.videoToUploadIV);
            videoNameET = findViewById(R.id.videoNameET);

            videoUploadingBtn = findViewById(R.id.videoUploadingBtn);
            videoToUploadIV.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            mc = new MediaController(UploadScreen.this);
                            videoToUploadIV.setMediaController(mc);
                            mc.setAnchorView(videoToUploadIV);
                        }
                    });
                }
            });
            videoToUploadIV.start();
            videoToUploadIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openGallery();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "connectXMLToJava:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        try {
            Intent objectIntent = new Intent(); //Step 1:create the object of intent
            objectIntent.setAction(Intent.ACTION_GET_CONTENT); //Step 2: You want to get some data

            objectIntent.setType("video/*");//Step 3: Images of all type
            startActivityForResult(objectIntent,REQUEST_CODE);

        } catch (Exception e) {
            Toast.makeText(this, "openGallery:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try

        {
            if(REQUEST_CODE==requestCode && resultCode ==RESULT_OK && data !=null) {
                videoUri = data.getData();
                videoToUploadIV.setVideoURI(videoUri);


            }
        }
        catch (Exception e) {
            Toast.makeText(this, "onActivityResult:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

  public void videoUploadToServer(View view)
  {

      StorageReference riversRef = videoRef.child("video/*");
      riversRef.putFile(videoUri)
              .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                      Toast.makeText(UploadScreen.this, "video UPloaded", Toast.LENGTH_SHORT).show();
                  }
              }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Toast.makeText(UploadScreen.this,"videoUploadToServer" + e.toString(),Toast.LENGTH_LONG).show();
          }
      });
  }



    private String getExtension(Uri videoUri)
    {
        try
        {
            ContentResolver objectContentResolver=getContentResolver();
            MimeTypeMap objectMimeTypeMap=MimeTypeMap.getSingleton();

            String extension=objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(videoUri));
            return extension;
        }
        catch (Exception e)
        {
            Toast.makeText(this, "getExtension:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return "";
    }
}
