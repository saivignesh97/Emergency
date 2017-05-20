package com.emergency;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import java.util.ArrayList;

public class Guest extends AppCompatActivity implements LocationListener {
    private Button sel, send;
    private TextView contact;
    private String latlong,sendto;
    private EditText name;
    private Toast toast;

    private Double latitude, longtitude;
    int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        sel = (Button) findViewById(R.id.select);
        send = (Button) findViewById(R.id.sendguest);
        name = (EditText) findViewById(R.id.name);
        contact = (TextView) findViewById(R.id.contact);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Turn On Location Services");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }



        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


        sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                flag = 1;

                startActivityForResult(intentPickContact, 1);


            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast=Toast.makeText(getApplicationContext(), "Current Location: " + latitude + "," + longtitude, Toast.LENGTH_SHORT);

                SmsManager sms = SmsManager.getDefault();
                String message = name.getText().toString() + " is in emergency" + "\n" + latlong + "\n" + "Google Map Link:" + "https://www.google.co.in/maps/place/" + latitude + "," + longtitude;
                ArrayList<String> parts = sms.divideMessage(message);
                //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                sms.sendMultipartTextMessage(sendto, null, parts,
                        null, null);

                Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_LONG).show();


            }
        });


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri returnUri = data.getData();
                Cursor cursor = getContentResolver().query(returnUri, null, null, null, null);
                if (cursor.moveToNext()) {
                    int columnIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactID = cursor.getString(columnIndex_ID);
                    int columnIndex_HASPHONENUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    String stringHasPhoneNumber = cursor.getString(columnIndex_HASPHONENUMBER);
                    if (stringHasPhoneNumber.equalsIgnoreCase("1")) {
                        Cursor cursorNum = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID, null, null);
                        if (cursorNum.moveToNext()) {
                            int columnIndex_number = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            int columnIndex_name = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            String stringName = cursorNum.getString(columnIndex_name);
                            String stringNumber = cursorNum.getString(columnIndex_number);
                                    contact.setText(stringName+"\n"+stringNumber);
                            sendto=stringNumber;

                            }
                        }
                    }
                }
            }
        }
    }








