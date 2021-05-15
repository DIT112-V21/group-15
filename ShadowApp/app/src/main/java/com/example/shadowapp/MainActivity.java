package com.example.shadowapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btSubmit;

    public void removeKeyboard(View c) {
     View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager close = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            close.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, OptionPage.class);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btSubmit = findViewById(R.id.bt_submit);
        String failure = "Invalid Username or password.";

        btSubmit.setOnClickListener(v -> {
            if (etUsername.getText().toString().equals("admin") &&
                    etPassword.getText().toString().equals("admin")) {

                startActivity(intent);
                
            } else {
                View view = this.getCurrentFocus();
                InputMethodManager close = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                close.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Toast.makeText(getApplicationContext(), failure,
                        Toast.LENGTH_SHORT).show();
            }


        });

    }
    
}
