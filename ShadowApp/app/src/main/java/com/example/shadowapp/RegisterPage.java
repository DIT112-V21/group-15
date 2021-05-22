package com.example.shadowapp;

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

public class RegisterPage extends AppCompatActivity {

    EditText uFullName, uEmail, uPassword;
    Button uRegisterBtn;
    TextView uLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        uFullName     = findViewById(R.id.fullname);
        uEmail        = findViewById(R.id.loginemail);
        uPassword     = findViewById(R.id.loginpassword);
        uRegisterBtn  = findViewById(R.id.LoginButton);
        uLoginBtn     = findViewById(R.id.textView2);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

      

        uRegisterBtn.setOnClickListener(new View.OnClickListener() {
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

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterPage.this,"User Created. ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), JoystickPage.class));
                        }else {
                            Toast.makeText(RegisterPage.this,"Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        uLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), JoystickPage.class));
            }
        });

    }
}