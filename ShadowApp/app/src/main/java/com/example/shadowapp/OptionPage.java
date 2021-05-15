package com.example.shadowapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);

        Button controllerB  = (Button) findViewById(R.id.button4);
        Button voiceCommand = (Button) findViewById(R.id.button5);
        Button mainPage = (Button) findViewById(R.id.button6);
        controllerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        voiceCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });

        mainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity1();
            }
        });
    }
    public void openActivity2 () {
        Intent intent = new Intent(this, ControllerPage.class);
        startActivity(intent);
    }
    public void openActivity3 () {
        Intent intent = new Intent(this, VoiceCommand.class);
        startActivity(intent);
    }
    public void openActivity1 () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}