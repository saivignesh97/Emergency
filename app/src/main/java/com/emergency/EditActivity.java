package com.emergency;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import static com.emergency.R.id.navmail;

public class EditActivity extends AppCompatActivity {
    private EditText username,userphone,name1,name2,name3,num1,num2,num3;
    private Button update;
    private FirebaseAuth auth;
    private DatabaseReference mref;
    private FirebaseUser fireuser;
    private ProgressDialog mProgress;
    private String usernamestr,userphonestr,name1str,name2str,name3str,num1str,num2str,num3str;
    private TextView tv,tv1;
    public SQLiteDatabase db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mref= FirebaseDatabase.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        db=openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);


        username=(EditText)findViewById(R.id.user_name);
        userphone=(EditText)findViewById(R.id.user_phone);
        name1=(EditText)findViewById(R.id.name1);
        name2=(EditText)findViewById(R.id.name2);
        name3=(EditText)findViewById(R.id.name3);
        num1=(EditText)findViewById(R.id.num1);
        num2=(EditText)findViewById(R.id.num2);
        num3=(EditText)findViewById(R.id.num3);
        update=(Button)findViewById(R.id.update);
        tv=(TextView)findViewById(R.id.tv);
        tv1=(TextView)findViewById(R.id.tv1);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/remachine.ttf");
        tv.setTypeface(face);
        tv1.setTypeface(face);

        auth=FirebaseAuth.getInstance();
        fireuser=auth.getCurrentUser();
new CountDownTimer(4000,1000)
{
    public void onTick(long ms)
    {   mProgress.setMessage("Loading your profile.This may take a few seconds.Please wait...");
        mProgress.show();

        }
    public void onFinish(){
        Cursor c=db.rawQuery("SELECT * FROM user WHERE mailid='"+fireuser.getEmail()+"'", null);
        if(c.moveToFirst())
        {
            username.setText(c.getString(2));
            userphone.setText(c.getString(3));
            name1.setText(c.getString(4));
            num1.setText(c.getString(5));
            name2.setText(c.getString(6));
            num2.setText(c.getString(7));
            name3.setText(c.getString(8));
            num3.setText(c.getString(9));

        }

    }
}.start();




        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fireuser=auth.getCurrentUser();
                UserDatabase user=new UserDatabase(username.getText().toString(),userphone.getText().toString(),name1.getText().toString(),num1.getText().toString(),name2.getText().toString(),num2.getText().toString(),name3.getText().toString(),num3.getText().toString());
                mref.child("contact").child(fireuser.getUid()).setValue(user);
                new CountDownTimer(3000,1000)
                {
                    public void onTick(long ms)
                    {
                        mProgress.setMessage("Updating your profile..");
                        mProgress.show();
                    }
                    public void onFinish()
                    {
                        mProgress.dismiss();
                        startActivity(new Intent(EditActivity.this,MainActivity.class));
                        Toast.makeText(getApplicationContext(),"Updated Successfully!",Toast.LENGTH_LONG).show();
                        finish();


                    }
                }.start();


            }
        });
    }
    @Override
    public void onBackPressed() {

        startActivity(new Intent(EditActivity.this,MainActivity.class));
        finish();
    }





}
