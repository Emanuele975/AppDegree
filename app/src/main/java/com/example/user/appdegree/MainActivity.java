package com.example.user.appdegree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

private Button btningresso;
private Button btnuscita;
private Boolean richiesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         btningresso=findViewById(R.id.button1);
         btnuscita=findViewById(R.id.button2);
        btningresso.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent openLogin = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(openLogin);
            }
        });
       btnuscita.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent openLogin = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(openLogin);// FORSE CAMBIARE NOME ALL'INTENT
            }
        });
    }
}
