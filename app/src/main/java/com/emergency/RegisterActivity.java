package com.emergency;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;

    private Button btnSignIn, btnSignUp, btnResetPassword,choose1,choose2,choose3;
    private ProgressDialog mProgress;
    private EditText username,userphone,usermail,confpassword,name1,name2,name3;
    private FirebaseAuth auth;
    public  String email,uid;
    private DatabaseReference mref;
    private FirebaseUser fireuser;
    private TextView tv1,tv2,num1,num2,num3;
    private CheckBox cb1,cb2;
    public SQLiteDatabase db;

    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        mref = FirebaseDatabase.getInstance().getReference();
        tv1 = (TextView) findViewById(R.id.tv);
        btnSignUp = (Button) findViewById(R.id.signup_btn);
        inputEmail = (EditText) findViewById(R.id.user_mail);
        mProgress = new ProgressDialog(this);
        inputPassword = (EditText) findViewById(R.id.pass);
        username = (EditText) findViewById(R.id.user_name);
        userphone = (EditText) findViewById(R.id.user_phone);
        confpassword = (EditText) findViewById(R.id.cpass);
        name1 = (EditText) findViewById(R.id.name1);
        num1 = (TextView) findViewById(R.id.num1);
        name2 = (EditText) findViewById(R.id.name2);
        num2 = (TextView) findViewById(R.id.num2);
        name3 = (EditText) findViewById(R.id.name3);
        num3 = (TextView) findViewById(R.id.num3);
        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);
        choose1 = (Button) findViewById(R.id.choose1);
        choose2 = (Button) findViewById(R.id.choose2);

        choose3 = (Button) findViewById(R.id.choose3);
        // Creating database and table
        db=openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS user(mailid VARCHAR,username VARCHAR,userphone VARCHAR,name1 VARCHAR,num1 VARCHAR,name2 VARCHAR,num2 VARCHAR,name3 VARCHAR,num3 VARCHAR);");


        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/remachine.ttf");
        tv1.setTypeface(face);

        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    inputPassword.setInputType(129);
                }
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    confpassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    confpassword.setInputType(129);
                }
            }
        });

        choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                flag = 1;
                startActivityForResult(intentPickContact, 1);

            }
        });
        choose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                flag = 2;
                startActivityForResult(intentPickContact, 1);
            }
        });
        choose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                flag = 3;
                startActivityForResult(intentPickContact, 1);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userphone.getText().toString().length()<10)
                {Toast.makeText(getApplicationContext(), "Check your mobile number!", Toast.LENGTH_SHORT).show();
                    return;


                }
                if(!(inputPassword.getText().toString().equals(confpassword.getText().toString())))
                {Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;


                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(num1.getText().toString().length()<10){
                    Toast.makeText(getApplicationContext(), "Enter a valid 10 digit number for Person1!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(num2.getText().toString().length()<10){
                    Toast.makeText(getApplicationContext(), "Enter a valid 10 digit number for Person2!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(num3.getText().toString().length()<10){
                    Toast.makeText(getApplicationContext(), "Enter a valid 10 digit number for Person3!", Toast.LENGTH_SHORT).show();
                    return;
                }


                mProgress.setMessage("Creating new account..");
                mProgress.show();
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed!",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    MainActivity.flag=true;
                                    fireuser=auth.getCurrentUser();
                                    UserDatabase user = new UserDatabase(username.getText().toString(),userphone.getText().toString(),inputEmail.getText().toString(),name1.getText().toString(),num1.getText().toString(),name2.getText().toString(),num2.getText().toString(),name3.getText().toString(),num3.getText().toString());
                                    mref.child("contact").child(fireuser.getUid()).setValue(user);
                                    db.execSQL("INSERT INTO user VALUES('"+email+"','"+username.getText().toString()+"','"+userphone.getText().toString()+"','"+name1.getText().toString()+"','"+num1.getText().toString()+"','"+name2.getText().toString()+"','"+num2.getText().toString()+"','"+name3.getText().toString()+"','"+num3.getText().toString()+"');");



                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
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
                            switch(flag){
                                case 1:
                                    name1.setText(stringName);
                                    num1.setText(stringNumber);
                                    break;
                                case 2:
                                    name2.setText(stringName);
                                    num2.setText(stringNumber);
                                    break;
                                case 3:
                                    name3.setText(stringName);
                                    num3.setText(stringNumber);

                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}