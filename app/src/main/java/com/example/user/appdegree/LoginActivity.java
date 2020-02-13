package com.example.user.appdegree;

import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    Button btnInvio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Bundle richiesta = getIntent().getExtras();
        btnInvio=findViewById(R.id.button3);

        btnInvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCamera = new Intent(LoginActivity.this, PhotoActivity.class);
                startActivity(openCamera);
                //openLogin.putExtra("richiesta", true);
            }
        });
    }
}