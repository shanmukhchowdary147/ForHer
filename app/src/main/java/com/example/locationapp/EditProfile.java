package com.example.locationapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.locationapp.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class EditProfile extends AppCompatActivity
{

    private static final String TAG ="Shannukk" ;
    private ImageView eProf;

    private Button cancel;

    EditText ProfileFullName, ProfileEmailAddress, ProfilePassword,ProfilePhone1,ProfilePhone2 ;

    public String _Name,_Email,_Password,_Phone,_Phone1,_Phone2;
    DatabaseReference reference;
    private static final int PICK_FILE=1;
    private Uri ImageUri;
    StorageReference Folder;
    String ImagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference= FirebaseDatabase.getInstance().getReference("Users");


        ProfileFullName= findViewById(R.id.FullName) ;
        ProfilePhone1=findViewById(R.id.Phn1) ;
        ProfilePhone2=findViewById(R.id.Phn2) ;
        ProfileEmailAddress = findViewById(R.id.profileEmailAddress) ;
        ProfilePassword = findViewById(R.id.profilePassword) ;
        cancel=(Button)findViewById(R.id.cancel);
        eProf=(ImageView)findViewById(R.id.EditProfileImg);

        Bundle bundle=getIntent().getExtras();
        _Phone=bundle.getString("PHONE");

        Log.i(TAG,_Phone);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(_Phone).exists()) {
                    Users userData = dataSnapshot.child(_Phone).getValue(Users.class);
                    String _N=userData.getName();
                    String _Em=userData.getEmail();
                    String _Pass=userData.getPassword();
                    String _Ph1=userData.getPhone1();
                    String _Ph2=userData.getPhone2();
                    String imgU=userData.getProfilePic();
                    Log.i(TAG,imgU);

                    ProfileFullName.setText(_N);
                    ProfileEmailAddress.setText(_Em);
                    ProfilePassword.setText(_Pass);
                    ProfilePhone1.setText(_Ph1);
                    ProfilePhone2.setText(_Ph2);
                    Picasso.get().load(imgU).into(eProf);
                    SendData(_N,_Em,_Pass,_Ph1,_Ph2);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditProfile.this,MainActivity.class);
                intent.putExtra("MPHONE",_Phone);
                startActivity(intent);
            }
        });


    }
//    To Use the values like name email this function is created
    private void SendData(String n, String em, String pass, String ph1, String ph2) {
        _Name=n;
        _Email=em;
        _Password=pass;
        _Phone1=ph1;
        _Phone2=ph2;

    }


    public void update(View view)
    {
        if (isNameChanged() || isPasswordChanged() || isEmailChanged() || isPhone1Changed() || isPhone2Changed())
        {
            main_act();
            Toast.makeText(this, "Information Updated", Toast.LENGTH_SHORT).show();
        }
        else{
            main_act();
            Toast.makeText(this, "Data Cannot be Updated!!", Toast.LENGTH_SHORT).show();
        }

    }

    private void main_act() {
        Intent intent2=new Intent(EditProfile.this,MainActivity.class);
        intent2.putExtra("MPHONE",_Phone);
        startActivity(intent2);
    }

    private boolean isPasswordChanged() {
        if(!_Password.equals(ProfilePassword.getText().toString()))
        {
            reference.child(_Phone).child("password").setValue(ProfilePassword.getText().toString());
            _Password=ProfilePassword.getText().toString();
            return true;
        }
        else
        {
            return false;
        }

    }

    private boolean isEmailChanged() {
        if(!_Email.equals(ProfileEmailAddress.getText().toString()))
        {
            reference.child(_Phone).child("email").setValue(ProfileEmailAddress.getText().toString());
            _Email=ProfileEmailAddress.getText().toString();
            return true;
        }
        else
        {
            return false;
        }

    }


    private boolean isNameChanged() {
        if(!_Name.equals(ProfileFullName.getText().toString()))
        {
            reference.child(_Phone).child("name").setValue(ProfileFullName.getText().toString());
            _Name=ProfileFullName.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isPhone1Changed() {
        if(!_Phone1.equals(ProfilePhone1.getText().toString()))
        {
            reference.child(_Phone).child("phone1").setValue(ProfilePhone1.getText().toString());
            _Name=ProfileFullName.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isPhone2Changed() {
        if(!_Phone2.equals(ProfilePhone2.getText().toString()))
        {
            reference.child(_Phone).child("phone2").setValue(ProfilePhone2.getText().toString());
            _Name=ProfileFullName.getText().toString();
            return true;
        }
        else
        {

            return false;
        }
    }


    @Override
    public void onBackPressed()
    {
        main_act();

    }


}