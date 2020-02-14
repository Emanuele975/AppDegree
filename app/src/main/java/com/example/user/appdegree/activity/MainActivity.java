package com.example.user.appdegree.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.user.appdegree.R;

public class MainActivity extends AppCompatActivity {

private Button btningresso;
private Button btnuscita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btningresso=findViewById(R.id.button1);
        btnuscita=findViewById(R.id.button2);
        btningresso.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("nell on click");
                Intent openLogin = new Intent(MainActivity.this,LoginActivity.class);
                openLogin.putExtra("richiesta","true");
                startActivity(openLogin);
            }
        });
        btnuscita.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent openLogin = new Intent(MainActivity.this,LoginActivity.class);
                openLogin.putExtra("richiesta","false");
                startActivity(openLogin);// FORSE CAMBIARE NOME ALL'INTENT

            }
        });
    }
}
