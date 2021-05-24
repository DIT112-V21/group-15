package com.example.shadowapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginPage extends AppCompatActivity {
    EditText uEmail, uPassword;
    Button uLoginBtn;
    TextView uCreateBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        uEmail       = findViewById(R.id.loginemail);
        uPassword    = findViewById(R.id.loginpassword);
        progressBar  = findViewById(R.id.progressBar2);
        fAuth        = FirebaseAuth.getInstance();
        uLoginBtn    = findViewById(R.id.LoginButton);
        uCreateBtn   = findViewById(R.id.textView2);

        uLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = uEmail.getText().toString().trim();
                String password = uPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    uEmail.setError("Email is Required. ");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    uPassword.setError("Password is Required. ");
                    return;
                }

                if(password.length() < 6) {
                    uPassword.setError("Password Must be >= 6 Characters.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(LoginPage.this,"Logged in Successfully ", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(getApplicationContext(),OptionPage.class));
                      }else
                          Toast.makeText(LoginPage.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        uCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterPage.class));
            }
        });
    }
}