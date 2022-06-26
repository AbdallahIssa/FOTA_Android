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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText userET, passET, emailET;
    Button registerBtn;

    //FireBase
    FirebaseAuth auth;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing widgets
        userET = findViewById(R.id.UsertxtEdit);
        passET = findViewById(R.id.PasswordtxtEdit);
        emailET = findViewById(R.id.EmailtxtEdit);
        registerBtn = findViewById(R.id.Registerbtn);

        //FireBase Auth
        auth = FirebaseAuth.getInstance();

        // Adding the Register Button event listener
        registerBtn.setOnClickListener(v -> {
            String username_text = userET.getText().toString();
            String email_text = emailET.getText().toString();
            String password_text = passET.getText().toString();

            if (TextUtils.isEmpty(username_text) || TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text)) {
                Toast.makeText(this, "please Fill all required fields", Toast.LENGTH_SHORT).show();
            } else {
                RegisterNow(username_text, email_text, password_text);
            }

        });

    }

    private void RegisterNow(final String username, String email, String password) {

        if (isValidEmail(email)) {
            auth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            String userid = user.getUid();
                            ref = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(userid);

                            //HashMaps
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("ImageURL", "default");

                            //opening the main Activity After successful Register
                            ref.setValue(hashMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } else {
                            Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
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