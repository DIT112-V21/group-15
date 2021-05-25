package com.example.shadowapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionPage extends AppCompatActivity {

    CardView cardController;
    CardView cardMic;
    CardView cardJoystick;
    CardView cardProfile;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);

        cardController = findViewById(R.id.cardController);
        cardMic = findViewById(R.id.cardMic);
        cardJoystick = findViewById(R.id.cardJoystick);
        cardProfile = findViewById(R.id.cardProfile);

        cardController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  ControllerPage(); }
        });
        cardMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  VoiceCommand();}
        });
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){ ProfilePage(); }
        });

    }
    public void ControllerPage () {
        Intent intent = new Intent(this, ControllerPage.class);
        startActivity(intent);
    }
    public void VoiceCommand () {
        Intent intent = new Intent(this, VoiceCommand.class);
        startActivity(intent);
    }
    public void ProfilePage () {
        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }

}



