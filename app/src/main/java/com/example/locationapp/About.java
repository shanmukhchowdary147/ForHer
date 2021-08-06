package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class About extends AppCompatActivity {
    String pho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Bundle bundle=getIntent().getExtras();
        pho=bundle.getString("PHONEa");
    }
    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(About.this,MainActivity.class);
        intent.putExtra("MPHONE",pho);
        startActivity(intent);

    }
}