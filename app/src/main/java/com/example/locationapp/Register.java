package com.example.locationapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.locationapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//import io.paperdb.Paper;

public class Register extends AppCompatActivity {

    private EditText InFullName,InEmail,InPassword,InPhone,InPhone1,InPhone2;
    private Button mRegisterBtn,mLoginBtn,pol;
    ProgressBar progressBar;
    private static final String TAG ="Shannu" ;
    private static final String TAG1 ="Shannu2";
    private ImageView profilePic;
    private static final int PICK_FILE=1;
    Uri ImageUri;
    StorageReference Folder;
    String ImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        InFullName   = (EditText)findViewById(R.id.fullName);
        InEmail      = (EditText)findViewById(R.id.Email);
        InPassword   = (EditText)findViewById(R.id.password);
        InPhone      =(EditText) findViewById(R.id.phone);
        InPhone1     =(EditText) findViewById(R.id.phone1);
        InPhone2     =(EditText) findViewById(R.id.phone2);
        mRegisterBtn= (Button)findViewById(R.id.registerBtn);
        mLoginBtn   = (Button) findViewById(R.id.signin);
        pol   = (Button) findViewById(R.id.pol_sig);
        profilePic=findViewById(R.id.profile_pic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePictures();
            }
        });



        progressBar = findViewById(R.id.progressBar);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        pol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(),police_Login.class));
            }
        });


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAcc();
            }
        });
    }

    private void choosePictures() {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_FILE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null )
        {
            ImageUri= data.getData();
            Folder= FirebaseStorage.getInstance().getReference().child("Profile Photos");
            profilePic.setImageURI(ImageUri);
            CreateAcc();
        }
    }

    private void CreateAcc() {
        String name=InFullName.getText().toString();
        String email=InEmail.getText().toString();
        String phone=InPhone.getText().toString();
        String phone1=InPhone1.getText().toString();
        String phone2=InPhone2.getText().toString();
        String password=InPassword.getText().toString();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        String randm = saveCurrentDate + saveCurrentTime;

        final StorageReference file_name=Folder.child("image"+ImageUri.getLastPathSegment()+randm +".jpg");

        file_name.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImagePath=String.valueOf(uri);
                    }
                });

            }
        });

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Please Valid Email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your Email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone1))
        {
            Toast.makeText(this, "Please write your Guardians phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone2))
        {
            Toast.makeText(this, "Please write your Guardians phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            ValidatePhoneNumber(name,phone,email,password,phone1,phone2,ImagePath);
        }

    }

    private void ValidatePhoneNumber(final String name, final String phone, final String email, final String password ,final String phone1,final String phone2, final String ImagePath) {


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    Log.i(TAG, email);


                    HashMap<String, Object> userdataMap=new HashMap<>();
                    userdataMap.put("email",email);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);
                    userdataMap.put("phone",phone);
                    userdataMap.put("phone1",phone1);
                    userdataMap.put("phone2",phone2);
                    userdataMap.put("profilePic",ImagePath);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Register.this, "Your Account has been Created", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(Register.this,Login.class);
                                        startActivity(intent);
                                        progressBar.setVisibility(View.GONE);

                                    }
                                    else
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Register.this, "Network Error!! Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
                else
                {
                    Toast.makeText(Register.this, "This Phone Number"+phone+"already exists", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(),Login.class));
    }


}