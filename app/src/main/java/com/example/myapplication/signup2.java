package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.GoogleAuthProvider;


public class signup2 extends AppCompatActivity {
    SignInButton signInButton;
    TextView txtMarquee;
    private static final String CHANNEL_ID = "my_channel_01";
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        signInButton = findViewById(R.id.gSignInBtn);
        txtMarquee = findViewById(R.id.marqueeText);
        txtMarquee.setSelected(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in, open MainActivity
            startMainActivity(user);
            return; // Finish the current activity to prevent going back to it
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(signup2.this, "Button Clicked", Toast.LENGTH_SHORT).show();
                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build());

                Intent signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build();
                signInLauncher.launch(signInIntent);
            }
        });
    }
    /* main thing
        private void startMainActivity(FirebaseUser user) {
            Intent intent = new Intent(signup2.this, MainActivity.class);
            intent.putExtra("profilePic", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "");
            intent.putExtra("userName", user.getDisplayName());
            intent.putExtra("userEmail", user.getEmail());
            intent.putExtra("userPhone", user.getPhoneNumber());
            startActivity(intent);
            finish();
        }

        private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
            IdpResponse response = result.getIdpResponse();
            if (result.getResultCode() == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userName = user.getDisplayName();
                    String userEmail = user.getEmail();
                    String userphone = user.getPhoneNumber();
                    String userUid = user.getUid();
                    String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";
                    String message = "User Logged in\nName: " + userName + "\nEmail: " + userEmail + "\nUID: " + userUid + "\nphone" + userphone;

                    startMainActivity(user);
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                    // Delay the notification by 10 seconds
                    showDelayedNotification("Thank you for using Farm Tech", 10000);
                }
            } else {
                Toast.makeText(this, "Some Error Broo", Toast.LENGTH_SHORT).show();
            }
        }
    */
    private void showDelayedNotification(final String message, long delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showNotification(message);
            }
        }, delayMillis);
    }

    private void showNotification(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.crop)
                .setContentTitle("Farm Tech")
                .setContentText(message)
                .setAutoCancel(true);

        getSystemService(NotificationManager.class).notify(1, builder.build()); // You can adjust the notification ID as needed
    }

    //main thing duplicate
    private void startMainActivity(FirebaseUser user) {
        Intent intent = new Intent(signup2.this, MainActivity.class);

        // Common extras for all authentication methods
        intent.putExtra("profilePic", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "");
        intent.putExtra("userName", user.getDisplayName());
        intent.putExtra("userEmail", user.getEmail());

        // Check authentication provider
        if (isGoogleSignIn(user)) {
            // For Google sign-in, only phone number and address are required
            intent.putExtra("userPhone", user.getPhoneNumber());
            intent.putExtra("userAddress", "");
        } else {
            // For other sign-in methods (email, phone), name, phone number, and address are required
            intent.putExtra("userPhone", "");
            intent.putExtra("userAddress", "");
        }

        startActivity(intent);
        finish();
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userName = user.getDisplayName();
                String userEmail = user.getEmail();
                String userphone = user.getPhoneNumber();
                String userUid = user.getUid();
                String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";

                String message = "User Logged in\nName: " + userName + "\nEmail: " + userEmail + "\nUID: " + userUid + "\nphone" + userphone;

                startMainActivity(user);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                // Delay the notification by 10 seconds
                showDelayedNotification("Thank you for using Farm Tech", 10000);
            }
        } else {
            Toast.makeText(this, "Some Error Broo", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to check if the user signed in with Google
    private boolean isGoogleSignIn(FirebaseUser user) {
        if (user != null) {
            for (UserInfo userInfo : user.getProviderData()) {
                if (userInfo.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID)) {
                    return true;
                }
            }
        }
        return false;
    }

}
