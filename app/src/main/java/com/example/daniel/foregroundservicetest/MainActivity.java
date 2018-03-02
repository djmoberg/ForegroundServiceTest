package com.example.daniel.foregroundservicetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ForegroundService.IS_SERVICE_RUNNING) {
            TextView tv = findViewById(R.id.textView);
            tv.setText(String.valueOf(ForegroundService.currentTime));

            Intent service = new Intent(MainActivity.this, ForegroundService.class);
            service.setAction("STOP_FOREGROUND");
            ForegroundService.IS_SERVICE_RUNNING = false;
            startService(service);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!ForegroundService.IS_SERVICE_RUNNING) {
            Intent service = new Intent(MainActivity.this, ForegroundService.class);
            service.setAction("START_FOREGROUND");
            ForegroundService.IS_SERVICE_RUNNING = true;
            startService(service);
        }
    }

//    public void buttonClicked(View v) {
//        Button button = (Button) v;
//        Intent service = new Intent(MainActivity.this, ForegroundService.class);
//        if (!ForegroundService.IS_SERVICE_RUNNING) {
//            service.setAction("START_FOREGROUND");
//            ForegroundService.IS_SERVICE_RUNNING = true;
//            button.setText("Stop Service");
//        } else {
//            service.setAction("STOP_FOREGROUND");
//            ForegroundService.IS_SERVICE_RUNNING = false;
//            button.setText("Start Service");
//
//        }
//        startService(service);
//    }
}
