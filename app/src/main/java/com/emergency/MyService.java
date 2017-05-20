package com.emergency;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

public class MyService extends Service{

    static NotificationManager nMgr;
    BroadcastReceiver bat;
    int currentLevel;
    static NotificationCompat.Builder builder;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(),"Service destroyed...",Toast.LENGTH_LONG).show();
        String ns = Context.NOTIFICATION_SERVICE;
        nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(1);
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Toast.makeText(getApplicationContext(),"Service started.... ",Toast.LENGTH_SHORT).show();
         bat = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            }
        };
        new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                registerReceiver(bat,intentFilter);
                if(currentLevel >=23) {
                     builder = new NotificationCompat.Builder(MyService.this);
                    builder.setSmallIcon(android.R.drawable.ic_dialog_info);
                  Intent  intent = new Intent(getApplicationContext(), sendEmergency.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, 0);
                    builder.setContentIntent(pendingIntent);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                    builder.setContentTitle("Emergency notification");
                    builder.setContentText("Inform your trusted Friends and Families");
                    builder.setColor(Color.GREEN);
                    builder.setOngoing(true);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1, builder.build());
                }

            }
            @Override
            public void onFinish() {}
        }.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
