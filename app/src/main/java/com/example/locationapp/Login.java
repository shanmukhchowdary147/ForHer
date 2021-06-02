package com.example.locationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationapp.Model.Users;
import com.example.locationapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    private static final String TAG ="shannu2" ;
    EditText InPassword,InPhone;
    Button mLoginBtn,pol,mCreateBtn;
    TextView forgotTextLink;
    ProgressBar progressBar;
    private CheckBox chkbox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        InPassword =(EditText) findViewById(R.id.password);
        mLoginBtn = (Button)findViewById(R.id.loginBtn);
        pol = (Button)findViewById(R.id.pol_Log);
        mCreateBtn = (Button) findViewById(R.id.signin);
        InPhone =(EditText) findViewById(R.id.phone);
        chkbox=(CheckBox) findViewById(R.id.checkBox);

        progressBar = findViewById(R.id.progressBar);

        Paper.init(this);
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey !="" && UserPasswordKey!=""){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccessToAcc(UserPhoneKey,UserPasswordKey);

            }
        }

        pol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),police_Login.class));
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();

            }
        });


    }

    private void LoginUser() {

        String password=InPassword.getText().toString();
        String phone=InPhone.getText().toString();
        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please enter your phone number!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {

            progressBar.setVisibility(View.VISIBLE);
            AllowAccessToAcc(phone,password);
        }
    }

    private void AllowAccessToAcc(final String phone, final String password) {


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    Users userData= dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if (userData.getPhone().equals(phone))
                    {
                        if (userData.getPassword().equals(password))
                        {
                            if(chkbox.isChecked()){
                                Paper.book().write(Prevalent.UserPhoneKey,phone);
                                Paper.book().write(Prevalent.UserPasswordKey,password);
                            }
                            Toast.makeText(Login.this, "Login Success..", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Login.this,MainActivity.class);
                            intent.putExtra("MPHONE",phone);
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    else{
                        Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Account with this deatils doesnot exist..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
