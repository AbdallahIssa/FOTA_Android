package com.example.fotaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    EditText userETLogin, passETLogin;
    Button loginBtn, NotAMemberBtn;

    //FireBase Auth
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       userETLogin = findViewById(R.id.editTextUserName);
       passETLogin = findViewById(R.id.editTextPassword);
       loginBtn = findViewById(R.id.loginBtn);
       NotAMemberBtn = findViewById(R.id.NotAMemberBtn);

        //login Button
        loginBtn.setOnClickListener(v -> {
            String email_text = userETLogin.getText().toString();
            String pass_text = passETLogin.getText().toString();

            //Checking if it is empty
            if (TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text)) {
                Toast.makeText(this, "please Fill all required fields", Toast.LENGTH_SHORT).show();
            } else {
                LoginNow(email_text, pass_text);
            }

        });

        //NotAMemberBtn Button
        NotAMemberBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void LoginNow(String email, String password) {
        if (isValidEmail(email)) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Login has been failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Enter correct email format please!", Toast.LENGTH_SHORT).show();
        }


    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}