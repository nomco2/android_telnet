package com.example.kimfamily.arduino_car_nodemcu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import telnet.MainActivity;
import algorithim_dev_classis.Algorithm_dev_activity;

public class Main_activiy extends AppCompatActivity {



    private Button telnetbtn;
    private Button argorithm_dev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activiy);


        telnetbtn = (Button) findViewById(R.id.telnet_btn);
        telnetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), MainActivity.class));
                Main_activiy.this.finish();
            }
        });

        telnetbtn = (Button) findViewById(R.id.telnet_btn);
        telnetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), MainActivity.class));
                Main_activiy.this.finish();
            }
        });

        argorithm_dev = (Button) findViewById(R.id.argorithm_dev);
        argorithm_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), Algorithm_dev_activity.class));
                Main_activiy.this.finish();
            }
        });
    }


}
