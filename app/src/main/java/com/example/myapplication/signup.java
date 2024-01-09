package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    EditText name, email, password, phone, address;
    Button signUpBtn;
    TextView login;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    StorageReference storageRef;
    ImageView profileImage;
    Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        profileImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        signUpBtn = findViewById(R.id.signupBtn);
        profileImage = findViewById(R.id.profileImage);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        login = findViewById(R.id.textView17);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startMainActivity(user);
            return;
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(pickImageIntent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String userPhone = phone.getText().toString().trim();
                String userAddress = address.getText().toString().trim();

                // Perform user registration with email and password
                firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User registration successful
                                    Toast.makeText(signup.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                    // Get the user ID of the registered user
                                    String userId = firebaseAuth.getCurrentUser().getUid();

                                    // Upload the image to Firebase Storage
                                    uploadImageToStorage(userId, userName, userEmail, userPhone, userAddress);
                                }
                                else {
                                    // User registration failed
                                    Toast.makeText(signup.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
        );
    }
    private void uploadImageToStorage(String userId, String userName, String userEmail, String userPhone, String userAddress) {
        if (selectedImageUri != null) {
            StorageReference imageRef = storageRef.child("profile_images").child(userId);
            imageRef.putFile(selectedImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> uriTask) {
                                        if (uriTask.isSuccessful()) {
                                            // Get the download URL
                                            Uri downloadUri = uriTask.getResult();

                                            DocumentReference userDocumentRef = firestore.collection("user").document(userId);

                                            // Create a data object with user details
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("profileUserName", userName);
                                            user.put("profileUserEmail", userEmail);
                                            user.put("profileUserPhone", userPhone);
                                            user.put("profileUserAddress", userAddress);
                                            user.put("profileImageUrl", downloadUri.toString());

                                            userDocumentRef.set(user)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Intent intent = new Intent(signup.this, MainActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                // Document creation failed
                                                                Toast.makeText(signup.this, "Error creating user document", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            // Failed to get download URL
                                            Toast.makeText(signup.this, "Error getting download URL", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(signup.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            DocumentReference userDocumentRef = firestore.collection("user").document(userId);

            Map<String, Object> user = new HashMap<>();
            user.put("profileUserName", userName);
            user.put("profileUserEmail", userEmail);
            user.put("profileUserPhone", userPhone);
            user.put("profileUserAddress", userAddress);

            userDocumentRef.set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(signup.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Document creation failed
                                Toast.makeText(signup.this, "Error creating user document", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void startMainActivity(FirebaseUser user) {
        Intent intent = new Intent(signup.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finishAffinity();
        }

}
