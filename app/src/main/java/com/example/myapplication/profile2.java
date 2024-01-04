package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class profile2 extends AppCompatActivity {
    Button logout,orders;
    TextView userNaam, userEmaail, userphone, adrees; // Added TextView for userAddress
    ImageView profilePicHere;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    // image
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        // Assume you have profilePicHere ImageView
                        profilePicHere.setImageBitmap(bitmap);

                        // Now you can upload this image to Firebase Storage or your preferred storage solution
                        // and get the URL to set it as the profileImageUrl.
                        // For simplicity, let's assume you have a method to upload the image and get the URL.
                        // String uploadedImageUrl = uploadImageToStorage(selectedImageUri);
                        // profileImageUrl = uploadedImageUrl;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Toast.makeText(this, "This is Profile Page Bro", Toast.LENGTH_SHORT).show();

        logout = findViewById(R.id.logout);
        userEmaail = findViewById(R.id.userEmaail);
        userNaam = findViewById(R.id.userName);
        userphone = findViewById(R.id.userphone2);
        adrees = findViewById(R.id.adres);
        profilePicHere = findViewById(R.id.profilePicHere);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            // Retrieve user details from Firestore
            DocumentReference documentReference = firestore.collection("user").document(userId);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        // Log details to help identify any issues
                        Log.d("Firestore", "DocumentSnapshot data: " + document.getData());

                        // Corrected key names to match the Firestore document
                        String userName = document.getString("profileUserName");
                        String userEmail = document.getString("profileUserEmail");
                        String userPhone = document.getString("profileUserPhone");
                        String userAddress = document.getString("profileUserAddress");
                        String profileImageUrl = document.getString("profileImageUrl");

                        Log.d("Firestore", "UserName: " + userName);
                        Log.d("Firestore", "UserEmail: " + userEmail);
                        Log.d("Firestore", "UserPhone: " + userPhone);
                        Log.d("Firestore", "UserAddress: " + userAddress);
                        Log.d("Firestore", "ProfileImageUrl: " + profileImageUrl);

                        userNaam.setText(userName);
                        userEmaail.setText(userEmail);
                        userphone.setText(userPhone);
                        adrees.setText(userAddress);

                        if (profilePicHere != null && profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(profile2.this).load(profileImageUrl).into(profilePicHere);
                        } else {
                            Glide.with(profile2.this).load(R.drawable.acc).into(profilePicHere);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                } else {
                    Log.d("Firestore", "get failed with ", task.getException());
                }
            });
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(profile2.this, signup.class);
                startActivity(intent);
                finish();
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.news) {
                startActivity(new Intent(getApplicationContext(), news.class));
                return true;
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(getApplicationContext(), cart.class));
                return true;
            } else if (itemId == R.id.customer) {
                startActivity(new Intent(getApplicationContext(), care.class));
                return true;
            }else if (itemId == R.id.orderDetailsTextView) {
                startActivity(new Intent(getApplicationContext(), orders2.class));
                return true;
            }
            return false;
        });
    }
}


//orginal code
// package com.example.myapplication;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.bumptech.glide.Glide;
//
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.firebase.ui.auth.AuthUI;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//public class profile2 extends AppCompatActivity {
//    Button logout;
//    TextView userNaam, userEmaail, userphone;
//    ImageView profilePicHere;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile2);
//        Toast.makeText(this, "This is Profile Page Bro", Toast.LENGTH_SHORT).show();
//
//        logout = findViewById(R.id.logout);
//        userEmaail = findViewById(R.id.userEmaail);
//        userNaam = findViewById(R.id.userName);
//        userphone = findViewById(R.id.adrees);
//        profilePicHere = findViewById(R.id.profilePicHere);
//
//        Intent intent = getIntent();
//        String profileUrl = intent.getStringExtra("profileImageUrl");
//        if (profilePicHere !=null && !profileUrl.isEmpty()) {
//            Glide.with(profile2.this).load(profileUrl).into(profilePicHere);
//        }else {
//            Glide.with(profile2.this).load(R.drawable.acc).into(profilePicHere);
//        }
//        String userName = intent.getStringExtra("profileUserName");
//        String userEmail = intent.getStringExtra("profileUserEmail");
//        String userPhone = intent.getStringExtra("profileUserPhone");
//
//        userNaam.setText(userName);
//        userEmaail.setText(userEmail);
//        userphone.setText(userPhone);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AuthUI.getInstance()
//                        .signOut(profile2.this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Intent intent = new Intent(profile2.this, signup.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//            }
//        });
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.news) {
//                startActivity(new Intent(getApplicationContext(), news.class));
//                return true;
//            } else if (itemId == R.id.cart) {
//                startActivity(new Intent(getApplicationContext(), cart.class));
//                return true;
//            } else if (itemId == R.id.customer) {
//                startActivity(new Intent(getApplicationContext(), care.class));
//                return true;
//            }
//            return false;
//        });
//
//    }
//
//
//}//