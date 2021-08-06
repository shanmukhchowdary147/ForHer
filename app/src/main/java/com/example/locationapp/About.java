package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {
    String pho,nam;
    private static final String TAGx ="Shann";
    TextView na,em;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        na=findViewById(R.id.textView7);
        em=findViewById(R.id.textView10);
        Bundle bundle=getIntent().getExtras();
        pho=bundle.getString("PHONEa");
        nam=bundle.getString("NAMEa");
        Log.i(TAGx,nam);
        na.setText("Hello "+nam +" 👋");
        em.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "shanmukhchowdary2002@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    Log.i(TAGx, "onClick: error");
                }
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(About.this,MainActivity.class);
        intent.putExtra("MPHONE",pho);
        startActivity(intent);

    }
}