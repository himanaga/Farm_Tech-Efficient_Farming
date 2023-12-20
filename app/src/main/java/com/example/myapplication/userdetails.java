package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class userdetails extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText name, num, adres;
    Button userdetails;
    FirebaseAuth dAuth;
    FirebaseFirestore user;
    String userID; // Declare userID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);
        name = findViewById(R.id.name);
        num = findViewById(R.id.num);
        adres = findViewById(R.id.adrees);
        userdetails = findViewById(R.id.button_continue);
        dAuth = FirebaseAuth.getInstance();
        user = FirebaseFirestore.getInstance();

        userdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString();
                String numText = num.getText().toString();
                String adresText = adres.getText().toString();

                if (TextUtils.isEmpty(nameText) || TextUtils.isEmpty(adresText) || TextUtils.isEmpty(numText)) {
                    Toast.makeText(userdetails.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (dAuth.getCurrentUser() != null && !TextUtils.isEmpty(nameText) && !TextUtils.isEmpty(adresText) && !TextUtils.isEmpty(numText)) {
                    // User is already signed in and has provided details
                    Intent intent = new Intent(userdetails.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


                dAuth.createUserWithEmailAndPassword(nameText, numText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(userdetails.this, "user creation successful", Toast.LENGTH_SHORT).show();
                            userID = dAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = user.collection("user").document(userID);
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", nameText);
                            userData.put("phoneNumber", numText);
                            userData.put("Address", adresText);
                            documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "user is created" + userID);
                                }
                            });
                            Intent intent = new Intent(userdetails.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(userdetails.this, "user creation unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
