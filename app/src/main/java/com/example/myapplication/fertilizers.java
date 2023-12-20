package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

    public class fertilizers extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fertilizers);

            Button btnSendNotification = findViewById(R.id.btnSendNotification);

            btnSendNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendTestNotification();

                }
            });
        }

        private void sendTestNotification() {
            // TODO: Implement the logic to send an FCM notification here
            // For simplicity, you can use the Firebase Console or FCM API to send a test notification.
            // You may need to handle FCM server-side implementation for production use.
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
        }
    }
