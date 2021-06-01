package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class police_Login extends AppCompatActivity {

    Button pLogin;
    TextView pt1, pt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.police_login);

        pLogin = findViewById(R.id.ploginBtn);
        pt1 = findViewById(R.id.pEmail);
        pt2 = findViewById(R.id.pPassword);

        pLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pEm=pt1.getText().toString();
                String pPass=pt2.getText().toString();

                if(pEm.equals("police@100") && pPass.equals("123456")  ){
                    startActivity(new Intent(getApplicationContext(),search.class));
                    Toast.makeText(police_Login.this, "Here are the Recent requests", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(police_Login.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}