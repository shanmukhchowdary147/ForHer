package com.example.locationapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationapp.Model.Users;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btlocation, lgot;
    CardView EditProf;
    TextView txtvw1, txtvw2, txtprof,adrss,ext;
    FusedLocationProviderClient mFusedLocationClient;
    String _MName,_MPhone,_MPhone1,_MPhone2,saveCurrentDate,saveCurrentTime,address, imageUri;
    String extmsg="";
    String lat,longt;
    private SensorManager smm;
    String ggloa;
    NavigationView nav;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private float acelVal;
    private float acelLast;
    private float shake;
    private DatabaseReference ReqsRef;
    private String ReqRandomKey;
    private static final String TAG ="Shannuxy";
    int PERMISSION_ID = 44;
    private ImageView mProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        setContentView(R.layout.activity_main);

        ReqsRef= FirebaseDatabase.getInstance().getReference().child("Requests");

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        btlocation = findViewById(R.id.bt_location);
        EditProf=findViewById(R.id.cEditProfile);
        txtvw1 = findViewById(R.id.txtvw_1);
        txtvw2 = findViewById(R.id.txtvw_2);
//        adrss= findViewById(R.id.adrs);
        ext=findViewById(R.id.ext);
        txtprof=findViewById(R.id.textProfile);
        mProfile=(ImageView)findViewById(R.id.mProf);
        nav=(NavigationView) findViewById(R.id.nav_menu);

        Bundle bundle=getIntent().getExtras();
        _MPhone=bundle.getString("MPHONE");



        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_logout:
                        Paper.book().destroy();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        break;
                    case R.id.nav_account:
                        edit();
                }
                return true;
            }
        });

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(_MPhone).exists()) {
                    Users userData = dataSnapshot.child("Users").child(_MPhone).getValue(Users.class);
                    _MName=userData.getName();
                    _MPhone1=userData.getPhone1();
                    _MPhone2=userData.getPhone2();
                    imageUri=userData.getProfilePic();
                    txtprof.setText("Hello "+_MName+" 👋");
                    Log.i(TAG,imageUri);
                    Picasso.get().load(imageUri).into(mProfile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        EditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        btlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    extmsg=ext.getText().toString();
                    sendSMS(ggloa);

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

    }

    private void edit() {
        Intent intent2=new Intent(MainActivity.this,EditProfile.class);
        intent2.putExtra("PHONE",_MPhone);
        startActivity(intent2);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            double latitude=location.getLatitude();
                            double longitude=location.getLongitude();
                            txtvw1.setText("Latitude :- "+ latitude + "");
                            txtvw2.setText("Longitude :- "+ longitude + "");
                            lat=String.valueOf(latitude);
                            longt=String.valueOf(longitude);
                            getCompleteAddressString(latitude,longitude);
                            double x=location.getAltitude();
                            String y=String.valueOf(x);
//                            Log.i(TAG,"address"+ address);
                            ggloa=address+"  latitude:"+lat+"  longitude:"+longt;

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
        smm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        smm.registerListener(sensorListener, smm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            double latitude=mLastLocation.getLatitude();
            double longitude=mLastLocation.getLongitude();
            txtvw1.setText("Latitude :- " + latitude + "");
            txtvw2.setText("Longitude :- " + longitude + "");
            lat=String.valueOf(latitude);
            longt=String.valueOf(longitude);
            getCompleteAddressString(latitude,longitude);
            Log.i(TAG, address);
            ggloa=address+"  latitude:"+lat+"  longitude:"+longt;
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    private void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                address = strReturnedAddress.toString();
                adrss.setText("Address :- "+ address);
                Log.i("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;
            if (shake > 30) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Do not Shake Me" + shake, Toast.LENGTH_SHORT);
                toast.show();
                extmsg=ext.getText().toString();
                sendSMS(ggloa);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void sendSMS(String messagegg) {



        String message = "Emergency!! I'm at location : " + messagegg+ "  " + extmsg;
        final String number1 = _MPhone1;
        final String number2 = _MPhone2;

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select your answer.");
        builder.setMessage("Are you sure to Send Location?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SmsManager sysmanage = SmsManager.getDefault();
                sysmanage.sendTextMessage(number1, null, message, null, null);
                sysmanage.sendTextMessage(number2, null, message, null, null);
                StoreReqInfo();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void StoreReqInfo() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        ReqRandomKey = saveCurrentDate + saveCurrentTime;

        SaveReqInfoTodatabase();
    }

    private void SaveReqInfoTodatabase() {
        HashMap<String, Object> Reqmap=new HashMap<>();
        Reqmap.put("Eid",ReqRandomKey);
        Reqmap.put("date",saveCurrentDate);
        Reqmap.put("time",saveCurrentTime);
        Reqmap.put("name",_MName);
        Reqmap.put("image",imageUri);
        Reqmap.put("phone",_MPhone);
        Reqmap.put("latitude",lat);
        Reqmap.put("longitude",longt);
//        Reqmap.put("address",address);
        Reqmap.put("ext",extmsg);

//        Log.i(TAG, address);

        ReqsRef.child(ReqRandomKey).updateChildren(Reqmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Data has been sent to police Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(MainActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set a title for alert dialog
        builder.setTitle("Select your answer.");

        // Ask the final question
        builder.setMessage("Do you want to exit ForHer ?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }
}