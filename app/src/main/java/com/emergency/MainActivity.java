package com.emergency;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener,SensorListener {

    private Button mBtnSave;
    public static String markerString;
    private MediaRecorder mRecorder;
    private String mFileName = null;
    private static final String LOG_TAG = "Record_Log";
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DateFormat df;
    private String userID, videofilename = null;
    final static int REQUEST_VIDEO_CAPTURE = 1;
    private Date date;
    View v;
    private String timestamp = null;
    public static boolean flag = false;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected static double latitude, longtitude;
    public static boolean gps_enabled, network_enabled,btnflag=false;
    private FirebaseUser fireuser;
    private FirebaseAuth auth;
    private String AudioUrl,VideoUrl,googleLink;
    public static String Last_location;
    private SmsManager Sms;
    private TextView navname,navmail,tv,tv1;
    private Boolean exit=false;
    public static int retrieve_flag=0;
    public static String ph1,ph2,ph3,emerperson,emermail,userphonestr,name1str,name2str,name3str;

    private GoogleMap mMap;
    private Location location;
    public DatabaseReference mdatabase;
    private Address address;
    private String curlocality,policenum;
    private HashMap<String,String> hm;
    public static String message,message1,message2,message3;
    private SensorManager sensorMgr;
    public Float x,y,z;
    public int SHAKE_THRESHOLD=100;
    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText reason;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sensorMgr=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensorMgr.registerListener((SensorListener)this,SensorManager.SENSOR_ACCELEROMETER,SensorManager.SENSOR_DELAY_GAME);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        reason=(EditText)findViewById(R.id.reason);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View hView=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
         mdatabase = FirebaseDatabase.getInstance().getReference("contact");
        hm = new HashMap<String,String>();
        hm.put("Hyderabad","08413233866");
        hm.put("Patna","09470001389");
        hm.put("Panaji","08322428482");
        hm.put("Gandhinagar","07923210108");
        hm.put("Shimla","09838616589");
        hm.put("Bangalore","08022942807");
        hm.put("Thiruvananthapuram","04712315096");
        hm.put("Bhopal","07552555922");
        hm.put("Mumbai","09993402670");
        hm.put("Bhubaneswar","08280338297");
        hm.put("Chandigarh","9779580952");
        hm.put("Jaipur","09530424266");
        hm.put("Chennai","09176241517");
        hm.put("Lucknow","09454401495");
        hm.put("Allahabad","09677102277");
        hm.put("Kolkata","09124492135");
        hm.put("Raipur","07712254496");
        hm.put("Dehradun","09084332352");
        hm.put("Ranchi","06512203754");
        hm.put("Delhi","09910641064");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //Main Code Starts Here
        mBtnSave = (Button) findViewById(R.id.save_me);
        mProgress = new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        fireuser=auth.getCurrentUser();
        navname=(TextView)hView.findViewById(R.id.navname);
        navmail=(TextView)hView.findViewById(R.id.navmail);

        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date();
        mStorage = FirebaseStorage.getInstance().getReference();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.mp3";

            new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    NameRetrieval();
                    NumRetrieval();
                    PersonRetrieval1();
                    PersonRetrieval2();
                    PersonRetrieval3();

                    DataRetrieval1();
                    DataRetrieval2();
                    DataRetrieval3();
                    mProgress.dismiss();
                    retrieve_flag=1;
                    navmail.setText(auth.getCurrentUser().getEmail());

                }

            }.start();

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);





        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting GPS Location
                if(btnflag==true)
                {
                    mBtnSave.setText("Save Me!");
                    btnflag=false;
                    SmsManager sms=SmsManager.getDefault();
                    String safe="Your friend "+emerperson+" is marked safe!";
                    sms.sendTextMessage(ph1,null,safe,null,null);
                    sms.sendTextMessage(ph2,null,safe,null,null);

                    sms.sendTextMessage(ph3,null,safe,null,null);
                    mFirebaseAnalytics.setUserProperty("Reason",reason.getText().toString());




                }
                else
                {
                SmsManager sms=SmsManager.getDefault();
                 message = "Your Friend " + emerperson + " is in emergency" + "\n" + "Last Known Location:" + Last_location + "\n" + "Google Map Link:" + "https://www.google.co.in/maps/place/" + latitude + "," + longtitude +"\n"+"Stay alert to receive audio and video links!";
                message3 = "A person named " + emerperson + " is in emergency" + "\n" + "Last Known Location:" + Last_location + "\n" + "Location Co-ordinates: "+latitude+","+longtitude+"\n"+"Google Map Link:" + "https://www.google.co.in/maps/place/" + latitude + "," + longtitude;

                ArrayList<String> parts = sms.divideMessage(message);
                //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                sms.sendMultipartTextMessage(ph1, null, parts,
                        null, null);
                sms.sendMultipartTextMessage(ph2, null, parts,
                        null, null);
                sms.sendMultipartTextMessage(ph3, null, parts,
                        null, null);
                    btnflag=true;
                //sms.sendMultipartTextMessage(policenum,null,parts,null,null);


                startRecording();
                new CountDownTimer(16000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        mProgress.setMessage("Recording Audio...............");
                        mProgress.show();
                    }

                    public void onFinish() {
                        Toast.makeText(getApplicationContext(), "Audio Recorded", Toast.LENGTH_SHORT).show();

                        mProgress.dismiss();
                        stopRecording();

                    }

                }.start();


            }

        }});




    }




    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, FirstScreen.class));
                finish();
            }
        }
    };

    @Override
    public void onLocationChanged(Location location) {


        latitude = location.getLatitude();
        longtitude = location.getLongitude();
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gc.getFromLocation(latitude, longtitude, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        if(addresses!=null)
        {
        if (addresses.size() > 0) {
             address = addresses.get(0);
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                sb.append(address.getAddressLine(i)).append("\n");
            sb.append(address.getLocality()).append("\n");
            sb.append(address.getPostalCode()).append("\n");
            sb.append(address.getCountryName());
        }
        mdatabase.child(fireuser.getUid()).child("Last Known Location").setValue(sb.toString()+"Time:"+ df.format(date));
            markerString=sb.toString();
        curlocality=address.getLocality().toString();
        policenum=hm.get(curlocality);
        Last_location=sb.toString()+"\n"+"Last Active on: "+df.format(date);



    }}

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(),"Unable to Fetch GPS!",Toast.LENGTH_SHORT).show();
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }


    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        uploadAudio();

    }


    private void uploadAudio() {
        timestamp = df.format(date);

        if (flag == true) {
            final StorageReference filepath = mStorage.child(fireuser.getUid()).child(timestamp).child("Audio").child("new_audio.mp3");

            Uri uri = Uri.fromFile(new File(mFileName));
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downUri=taskSnapshot.getDownloadUrl();
                    AudioUrl=downUri.toString();

                    mProgress.dismiss();


                }
            });
        } else {
            StorageReference filepath = mStorage.child(fireuser.getUid()).child(timestamp).child("Audio").child("new_audio.mp3");

            Uri uri = Uri.fromFile(new File(mFileName));
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // AudioUrl=downUri.toString();

                    Uri downUri=taskSnapshot.getDownloadUrl();
                    AudioUrl=downUri.toString();
                    mProgress.dismiss();



                }
            });
        }
        captureVideo(v);


    }

    /* public void uploadGPS(){

          DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("contact");
          mdatabase.child(ContactActivity.userId).child("Location").setValue(address);


      }*/
    //video Recording Code
    public void captureVideo(View v) {
        Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File videopath = getFilePath();
        Uri video_uri = Uri.fromFile(videopath);
        video.putExtra(MediaStore.EXTRA_OUTPUT, video_uri);
        video.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        video.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
        startActivityForResult(video, REQUEST_VIDEO_CAPTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            Toast.makeText(getApplicationContext(), "Video recorded Successfuly", Toast.LENGTH_SHORT).show();
            uploadVideo();
        } else
            Toast.makeText(getApplicationContext(), "Video recorded failed", Toast.LENGTH_SHORT).show();

    }

    public File getFilePath() {
        String timestamp = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss aa").format(Calendar.getInstance().getTime());
        videofilename = Environment.getExternalStorageDirectory().getAbsolutePath();
        videofilename += "/vid.mp4";
        File videopath = new File(videofilename);
        return videopath;
    }

    //Video Uploading Code
    private void uploadVideo() {

        mProgress.setMessage("Uploading Audio and Video..");
        mProgress.show();

        if (flag == true) {
            final StorageReference filepath = mStorage.child(fireuser.getUid()).child(timestamp).child("Video").child("new_video.mp4");

            Uri uri = Uri.fromFile(new File(videofilename));
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                                                           @Override
                                                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                               Uri downUri = taskSnapshot.getDownloadUrl();
                                                               VideoUrl = downUri.toString();


                                                               mProgress.dismiss();
                                                               SmsManager sms = SmsManager.getDefault();
                                                               message1 = message + "\n" + "AudioURL:" + AudioUrl + " \n\n" + "VideoURL:" + VideoUrl;
                                                               message2 = "Emergency Help!" + "\n" + "AudioURL:" + AudioUrl + " \n\n" + "VideoURL:" + VideoUrl;

                                                               ArrayList<String> parts = sms.divideMessage(message2);
                                                               //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                                                               sms.sendMultipartTextMessage(ph1, null, parts,
                                                                       null, null);
                                                               sms.sendMultipartTextMessage(ph2, null, parts,
                                                                       null, null);
                                                               sms.sendMultipartTextMessage(ph3, null, parts,
                                                                       null, null);

                                                               Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_LONG).show();
                                                               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                               builder.setTitle("Emergency help");
                                                               builder.setMessage("Do you want to send to police helpline number?");
                                                               builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                   public void onClick(DialogInterface dialogInterface, int i) {
                                                                       // Show location settings when the user acknowledges the alert dialog
                                                                       SmsManager sms = SmsManager.getDefault();
                                                                       message1 = message + "\n" + "AudioURL:" + AudioUrl + " \n\n" + "VideoURL:" + VideoUrl;
                                                                       ArrayList<String> parts = sms.divideMessage(message1);
                                                                       //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                                                                       sms.sendMultipartTextMessage(policenum, null, parts,
                                                                               null, null);
                                                                       while (btnflag) {
                                                                           final Handler handler = new Handler();
                                                                           handler.postDelayed(new Runnable() {
                                                                               @Override
                                                                               public void run() {
                                                                                   // Do something after 5s = 5000ms
                                                                                   String message3 = "A person named " + emerperson + " is in emergency" + "\n" + "Last Known Location:" + Last_location + "\n" + "Location Co-ordinates: " + location.getLatitude() + "," + location.getLongitude() + "\n" + "Altitude:" + location.getAltitude() + "Google Map Link:" + "https://www.google.co.in/maps/place/" + location.getLatitude() + "," + location.getLongitude();

                                                                                   new SendMail().execute(message3);
                                                                               }
                                                                           }, 120000);


                                                                       }
                                                                   }});
                                                               Dialog alertDialog = builder.create();
                                                               alertDialog.setCanceledOnTouchOutside(false);
                                                               alertDialog.show();


                                                           }
                                                       }
            );
        }else {
            StorageReference filepath = mStorage.child(fireuser.getUid()).child(timestamp).child("Video").child("new_video.mp4");

            Uri uri = Uri.fromFile(new File(videofilename));
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downUri = taskSnapshot.getDownloadUrl();
                    VideoUrl = downUri.toString();

                    mProgress.dismiss();
                    SmsManager sms = SmsManager.getDefault();
                    message1 = message3 + "\n" + "AudioURL:" + AudioUrl + " \n\n" + "VideoURL:" + VideoUrl;
                    message2 = "Emergency Help!" + "\n" + "AudioURL:" + AudioUrl + " \n\n" + "VideoURL:" + VideoUrl;

                    ArrayList<String> parts = sms.divideMessage(message2);
                    //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    sms.sendMultipartTextMessage(ph1, null, parts,
                            null, null);
                    sms.sendMultipartTextMessage(ph2, null, parts,
                            null, null);
                    sms.sendMultipartTextMessage(ph3, null, parts,
                            null, null);
                    sms.sendMultipartTextMessage(policenum, null, parts, null, null);
                    while (btnflag) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                String message3 = "A person named " + emerperson + " is in emergency" + "\n" + "Last Known Location:" + Last_location + "\n" + "Location Co-ordinates: " + location.getLatitude() + "," + location.getLongitude() + "\n" + "Altitude:" + location.getAltitude() + "Google Map Link:" + "https://www.google.co.in/maps/place/" + location.getLatitude() + "," + location.getLongitude();

                                new SendMail().execute(message3);
                            }
                        }, 120000);


                        Toast.makeText(getApplicationContext(), "Messages sent to your friends!", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Emergency help");
                        builder.setMessage("Do you want to send to police helpline number?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Show location settings when the user acknowledges the alert dialog
                                SmsManager sms = SmsManager.getDefault();
                                message1 = message + "\n" + "AudioURL:" + AudioUrl + " \n\n" + "VideoURL:" + VideoUrl;
                                ArrayList<String> parts = sms.divideMessage(message1);
                                //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                                sms.sendMultipartTextMessage(policenum, null, parts,
                                        null, null);


                            }
                        });
                        Dialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    }
                }
//

//            filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                    int currentprogress = (int) progress;
//                    mProgress.setMessage("Uploading in progress.. " + "\n" + currentprogress);
//                    mProgress.show();
//                }
//            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                }
//            });
        });
        mBtnSave.setText("Im safe!");
        btnflag=true;
    }}


    private void sendmail(String body){

        gmailSender mailsender = new gmailSender("emergencyhelpaai@gmail.com", "emergency1!2@3#");

        String[] toArr = { "vsai97@gmail.com", "rajeshkumaran1996@gmail.com" };
        mailsender.set_to(toArr);
        mailsender.set_from("emergencyhelpaai@gmail.com");
        mailsender.set_subject("Emergency Situation!");
        mailsender.setBody(body);

        try {
            //mailsender.addAttachment("/sdcard/filelocation");

            if (mailsender.send()) {
                Toast.makeText(MainActivity.this,
                        "Email was sent successfully.",
                        Toast.LENGTH_LONG).show();
                Log.e("Mailapp","mail sent");
            } else {
                Toast.makeText(MainActivity.this, "Email was not sent.",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

            Log.e("MailApp", "Could not send email", e);
        }
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if(sensor==SensorManager.SENSOR_ACCELEROMETER)
        {
            long currTime=System.currentTimeMillis();
            if(currTime>100)
            {

                long diff=currTime;
                x=values[SensorManager.DATA_X];
                y=values[SensorManager.DATA_X];
                z=values[SensorManager.DATA_X];

                Float speed=Math.abs(x+y+z);
                if(speed>SHAKE_THRESHOLD)
                {

                    final int[] flag = new int[1];

                    flag[0] = 0;
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Alert Dialog").setMessage("Do you want to contact the police?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            flag[0] = 1;
                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage("9952969333", null, message3, null, null);
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(), "You clicked on No", Toast.LENGTH_SHORT).show();
                            flag[0] = 2;
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                    if(flag[0] == 0) {
                        final Handler handler = new Handler();
                        final Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage("9952969333", null, message3, null, null);
                                alert.dismiss();
                            }
                        };
                        handler.postDelayed(runnable, 6000);
                    }
                }
            }


                }



    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }

    private class SendMail extends AsyncTask<String, Integer, Void> {
            String mail=MainActivity.message3;

        protected void onProgressUpdate() {
            //called when the background task makes any progress
        }

        @Override
        protected Void doInBackground(String... params) {
            sendmail(mail);

            return null;
        }

        protected void onPreExecute() {
            //called before doInBackground() is started
        }
        protected void onPostExecute() {
            //called after doInBackground() has finished
        }
    }






    private void NameRetrieval(){

        try{ //
            // mRef=new Firebase("https://emergency-b628f.firebaseio.com/Sai");

            DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("contact/"+fireuser.getUid()+"/user");

            mdatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    emerperson=dataSnapshot.getValue().toString();
                    navname.setText(emerperson);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Unable",Toast.LENGTH_LONG).show();
        }
    }
    private void NumRetrieval(){

        try{ //
            // mRef=new Firebase("https://emergency-b628f.firebaseio.com/Sai");
            DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("contact/"+fireuser.getUid()+"/phone");

            mdatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    userphonestr=dataSnapshot.getValue().toString();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Unable",Toast.LENGTH_LONG).show();
        }
    }
    private void PersonRetrieval1(){

        try{ //
            // mRef=new Firebase("https://emergency-b628f.firebaseio.com/Sai");
            DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("contact/"+fireuser.getUid()+"/name1");

            mdatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                    name1str=dataSnapshot.getValue().toString();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Unable",Toast.LENGTH_LONG).show();
        }
    }
    private void PersonRetrieval2(){

        try{ //
            // mRef=new Firebase("https://emergency-b628f.firebaseio.com/Sai");
            DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("contact/"+fireuser.getUid()+"/name2");

            mdatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    name2str=dataSnapshot.getValue().toString();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Unable",Toast.LENGTH_LONG).show();
        }
    }
    private void PersonRetrieval3(){

        try{ //
            // mRef=new Firebase("https://emergency-b628f.firebaseio.com/Sai");
            DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("contact/"+fireuser.getUid()+"/name3");

            mdatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    name3str=dataSnapshot.getValue().toString();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Unable",Toast.LENGTH_LONG).show();
        }
    }

    private void DataRetrieval1(){

        try{ //
            // mRef=new Firebase("https://emergency-b628f.firebaseio.com/Sai");
            DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("contact/"+fireuser.getUid()+"/num1");

            mdatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    ph1=dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Unable",Toast.LENGTH_LONG).show();
        }
    }
    private void DataRetrieval2(){

        try{ //
            // mRef=new Firebase("https://emergency-b628f.firebaseio.com/Sai");
            DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("contact/"+fireuser.getUid()+"/num2");

            mdatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    ph2=dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Unable",Toast.LENGTH_LONG).show();
        }
    }
    private void DataRetrieval3(){

        try{ //
            // mRef=new Firebase("https://emergency-b628f.firebaseio.com/Sai");
            DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("contact/"+fireuser.getUid()+"/num3");

            mdatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    ph3=dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Unable",Toast.LENGTH_LONG).show();
        }
    }







    @Override
    public void onBackPressed() {


        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.edit) {
            startActivity(new Intent(MainActivity.this,EditActivity.class));
            finish();


        }
        else if(id==R.id.battery)
        {


            Intent i = new Intent(getApplicationContext(), MyService.class);
            startService(i);
        }

        else if (id == R.id.logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Emergency Help");

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to log out?");


            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    // Write your code here to invoke YES event
                    auth.signOut();
                    startActivity(new Intent(MainActivity.this, FirstScreen.class));
                    finish();
                }
            });


            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
        else if(id==R.id.mapdrawer)
        {
            startActivity(new Intent(MainActivity.this,MapsActivity.class));


        }
        else if(id==R.id.help)
        {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
