package com.emergency;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.firebase.auth.FirebaseAuth;

public class FirstScreen extends AppCompatActivity {
    private Button login_start,signup_start,guest;
    private FirebaseAuth auth;
    private TextView help;
    final int PERMS_REQUEST_CODE = 123;
    private Boolean exit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(FirstScreen.this,MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_first_screen);
        login_start=(Button)findViewById(R.id.login_start);
        help=(TextView)findViewById(R.id.help);
        signup_start=(Button)findViewById(R.id.signup_start);
        auth=FirebaseAuth.getInstance();
        guest=(Button)findViewById(R.id.guest);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                if(!hasPermissions())
                    requestPermission();

            }

        }.start();
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreen.this,Guest.class));
            }
        });

        login_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreen.this,LoginActivity.class));
            }
        });

        signup_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FirstScreen.this,RegisterActivity.class));
            }
        });
    }

    boolean hasPermissions() {
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_SETTINGS};

        for (String s : permissions) {
            res = checkCallingOrSelfPermission(s);
            if (!(res == PackageManager.PERMISSION_GRANTED))
                return false;
        }
        return true;

    }

    private void requestPermission() {
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_SETTINGS};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(permissions, PERMS_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;
        switch (requestCode) {
            case PERMS_REQUEST_CODE:
                for (int res : grantResults)
                    allowed = allowed && res == PackageManager.PERMISSION_GRANTED;
                break;
            default:
                allowed = false;
                break;


        }
        if (allowed)
            Toast.makeText(getApplicationContext(), "Permission Granted now.........", Toast.LENGTH_SHORT).show();
        else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                Toast.makeText(getApplicationContext(), " Storage Warning not permitted...", Toast.LENGTH_SHORT).show();
            else if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS))
                Toast.makeText(getApplicationContext(), "SMS Warning not permitted...", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {

        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }



}
