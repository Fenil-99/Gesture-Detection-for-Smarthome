package com.example.smarthomegesturecontroltraining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import okhttp3.OkHttpClient;



import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Practice extends AppCompatActivity {

    private static final int REQUEST_ID = 1;
    private static final int VIDEO_TIME = 5;
    public static final String MYPREF = "practice_number_pref";
    private static final String USER_LNAME = "MADLANI";

    String currGesture;
    String DemoGestureFileName;
    String practiceFile_GestureName;
    String practiceFile_fullName;

    VideoView Demo_View;
    Button practiceBtn;
    Button uploadBtn;
    private Uri URI_Demo;
    private Uri URI_Practice;
    VideoView Practice_View;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_practice);
        Demo_View = findViewById(R.id.demo);
        Practice_View = findViewById(R.id.practice);

        Intent intent = getIntent();
        currGesture = intent.getStringExtra("Gesture Name");
        setFileName(currGesture);

        setVideoView(DemoGestureFileName);

        practiceBtn = (Button)findViewById(R.id.practice_btn);
        practiceBtn.setOnClickListener(v -> recordPracticeVideo(currGesture));

        uploadBtn = findViewById(R.id.upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToServer();
                finish();
            }
        });
        uploadBtn.setVisibility(View.GONE);
        Practice_View.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Demo_View.setVideoURI(URI_Demo);
        Demo_View.start();
        Demo_View.setOnCompletionListener (mediaPlayer -> Demo_View.start());

        if(URI_Practice != null) {
            Practice_View.setVideoURI(URI_Practice);
            Practice_View.start();
            Practice_View.setOnCompletionListener (mediaPlayer -> Practice_View.start());
        }
    }

    private void setFileName(String currGesture) {
        if(currGesture.equals("Turn On Light")){
            DemoGestureFileName = "lighton";
            practiceFile_GestureName = "LightOn";
        }
        else if(currGesture.equals("Turn Off Light")) {
            DemoGestureFileName = "lightoff";
            practiceFile_GestureName = "LightOff";
        }
        else if(currGesture.equals("Turn On Fan")) {
            DemoGestureFileName = "fanon";
            practiceFile_GestureName = "FanOn";
        }
        else if(currGesture.equals("Turn Off Fan")) {
            DemoGestureFileName = "fanoff";
            practiceFile_GestureName = "FanOff";
        }
        else if(currGesture.equals("Increase Fan Speed")) {
            DemoGestureFileName = "increasefanspeed";
            practiceFile_GestureName = "FanUp";
        }
        else if(currGesture.equals("Decrease Fan Speed")) {
            DemoGestureFileName = "decreasefanspeed";
            practiceFile_GestureName = "FanDown";
        }
        else if(currGesture.equals("Set Thermostat")) {
            DemoGestureFileName = "setthermo";
            practiceFile_GestureName = "SetThermo";
        }
        else if(currGesture.equals("0")) {
            DemoGestureFileName = "h0";
            practiceFile_GestureName = "Num0";
        }
        else if(currGesture.equals("1")) {
            DemoGestureFileName = "h1";
            practiceFile_GestureName = "Num1";
        }
        else if(currGesture.equals("2")) {
            DemoGestureFileName = "h2";
            practiceFile_GestureName = "Num2";
        }
        else if(currGesture.equals("3")) {
            DemoGestureFileName = "h3";
            practiceFile_GestureName = "Num3";
        }
        else if(currGesture.equals("4")) {
            DemoGestureFileName = "h4";
            practiceFile_GestureName = "Num4";
        }
        else if(currGesture.equals("5")) {
            DemoGestureFileName = "h5";
            practiceFile_GestureName = "Num5";
        }
        else if(currGesture.equals("6")) {
            DemoGestureFileName = "h6";
            practiceFile_GestureName = "Num6";
        }
        else if(currGesture.equals("7")) {
            DemoGestureFileName = "h7";
            practiceFile_GestureName = "Num7";
        }
        else if(currGesture.equals("8")) {
            DemoGestureFileName = "h8";
            practiceFile_GestureName = "Num8";
        }
        else if(currGesture.equals("9")) {
            DemoGestureFileName = "h9";
            practiceFile_GestureName = "Num9";
        }
    }

    private void recordPracticeVideo(String fileName) {

        practiceFile_fullName = practiceFile_GestureName + "_PRACTICE_" + getPracticeNumber(practiceFile_GestureName) + "_" + USER_LNAME + ".mp4";
        File fileVideo = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PracticeVideos/" + practiceFile_fullName);

        Intent record = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        record.putExtra(MediaStore.EXTRA_DURATION_LIMIT, VIDEO_TIME);
        record.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        URI_Practice = FileProvider.getUriForFile(getApplicationContext(),
                "com.example.smarthomegesturecontroltraining.provider", fileVideo);
        record.putExtra(MediaStore.EXTRA_OUTPUT, URI_Practice);

        startActivityForResult(record, REQUEST_ID);
    }

    private int getPracticeNumber(String practiceFile_gestureName) {
        SharedPreferences pref = getSharedPreferences(MYPREF,MODE_PRIVATE);

        int pracNum=0;

        if(!pref.contains(practiceFile_gestureName)) {
            pracNum = 1;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(practiceFile_gestureName, pracNum);
            editor.apply();
        }
        else if(pref.contains(practiceFile_gestureName)) {
            pracNum = pref.getInt(practiceFile_gestureName,0)+1;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(practiceFile_gestureName, pracNum);
            editor.apply();
        }

        return pracNum;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_ID && resultCode == RESULT_OK) {
            uploadBtn.setVisibility(View.VISIBLE);
            Practice_View.setVisibility(View.VISIBLE);

            Practice_View.setVideoURI(URI_Practice);
            Practice_View.start();
            Practice_View.setOnCompletionListener (mediaPlayer -> Practice_View.start());

            Toast.makeText(this, "video loc: /Internal Storage/PracticeVideos/" + practiceFile_fullName,Toast.LENGTH_LONG).show();
        }
    }

    private void setVideoView(String videoName) {
        int resId = this.getResources().getIdentifier(videoName, "raw", this.getPackageName());
        URI_Demo = Uri.parse("android.resource://" + getPackageName() + "/" + resId);
        Demo_View.setVideoURI(URI_Demo);

        Demo_View.start();
        Demo_View.setOnCompletionListener (mediaPlayer -> Demo_View.start());
    }

    public void uploadToServer() {
        File videoPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PracticeVideos/");
        File sourceFile = new File(videoPath, practiceFile_fullName);

        MultipartBody.Builder builder=new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("myfile",sourceFile.getName().toString(), RequestBody.create(MediaType.parse("/"),sourceFile));
        MultipartBody multipartBody = builder.build();

        String urlStr = "http://" + "192.168.0.57" + ":" + 5000 + "/api/upload";
        Request request = new Request
                .Builder()
                .post(multipartBody)
                .url(urlStr)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Practice.this, "Error:" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(Practice.this,response.body().string(),Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
