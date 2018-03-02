package com.example.daniel.foregroundservicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class ForegroundService extends Service {

    public static boolean IS_SERVICE_RUNNING = false;
    public static int currentTime = 0;

    String NOTIFICATION_CHANNEL_ID = "timer_notification_channel";
    int notificationId = 1;

    Notification notification;
    MyTask myTask = new MyTask();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("START_FOREGROUND")) {
            showNotification();
            Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals("STOP_FOREGROUND")) {
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Timer kanal");
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

//        notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Timer: ")
//                .setContentText("0").build();
//
//        startForeground(notificationId, notification);

//        new MyTask().execute("haha");
        myTask.execute("haha");

    }

    private class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            currentTime = 0;
            while (currentTime < 100000 && !isCancelled()) {
                publishProgress(currentTime);
                currentTime++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            notification = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Timer: ")
                    .setContentText(values[0].toString()).build();
            startForeground(notificationId, notification);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "onPostExecute", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Detroyed!", Toast.LENGTH_SHORT).show();
        myTask.cancel(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
