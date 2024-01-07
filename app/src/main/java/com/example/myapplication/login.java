package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class login extends AppCompatActivity {

    private TextInputLayout usernameOrEmailLayout;
    private EditText usernameOrEmailEditText;
    private TextInputLayout passwordLayout;
    private EditText passwordEditText;
    private Button loginButton;
    TextView signup;

    private FirebaseAuth firebaseAuth;
    private void startMainActivity(FirebaseUser user) {
        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameOrEmailLayout = findViewById(R.id.usernameOrEmailLayout);
        usernameOrEmailEditText = findViewById(R.id.usernameOrEmailEditText);
        passwordLayout = findViewById(R.id.passwordLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signup = findViewById(R.id.textView13);

        TextView marqueeText = findViewById(R.id.marqueeText);
        marqueeText.setSelected(true);
        marqueeText.requestFocus();


        signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(login.this, signup.class);
               startActivity(intent);
               finish();
           }
       });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in, open MainActivity
            startMainActivity(user);
            return; // Finish the current activity to prevent going back to it
        }

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameOrEmail = usernameOrEmailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Perform authentication using Firebase
                firebaseAuth.signInWithEmailAndPassword(usernameOrEmail, password)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });
    }



    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this,"Authentication failed",Toast.LENGTH_LONG).show();
        }
    }
}
