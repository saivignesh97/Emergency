package com.emergency;

import android.*;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class sendEmergency extends AppCompatActivity implements LocationListener {

    String message=null,ph=null;
    EditText e,no;
    Button send;
    public SQLiteDatabase db;
    public FirebaseAuth auth;
    public FirebaseUser fireuser;
    public LocationManager locationManager;
    public Location location;
    private Double latitude,longtitude;
    private Double altitude;
    private String latlong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        fireuser=auth.getCurrentUser();
        new CountDownTimer(6000,1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                SmsManager sms=SmsManager.getDefault();
                message = "Your Friend " + MainActivity.emerperson + " is in emergency" + "\n" + "Last Known Location:" + latlong+ "\n" + "Google Map Link:" + "https://www.google.co.in/maps/place/" + latitude + "," + longtitude+"\n"+altitude;
                ArrayList<String> parts = sms.divideMessage(message);
                //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                sms.sendMultipartTextMessage("9677102277", null, parts,
                        null, null);

            }
        }.start();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);




        Toast.makeText(getApplicationContext(),"Your current status is sent to your dear ones!",Toast.LENGTH_LONG).show();
        String ns = Context.NOTIFICATION_SERVICE;
        MyService.nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        MyService.nMgr.cancel(1);

            }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longtitude = location.getLongitude();
        latlong = "Current Location is :" + latitude.toString() + "," + longtitude.toString();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

