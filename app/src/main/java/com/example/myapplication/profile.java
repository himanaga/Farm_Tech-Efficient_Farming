package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// SMS
import android.telephony.SmsManager;
import android.widget.ImageView;
import java.util.ArrayList;

public class profile extends AppCompatActivity {
    ImageView profilePic;
    ActivityProfileBinding binding;
    private ArrayList<User> cartList = new ArrayList<>();
    private static final String CHANNEL_ID = "my_channel_01";

    private User getItemDetails() {
        return new User("ItemName", "ItemPrice", "ItemDescription", R.drawable.a);
    }

    private static int notificationIdCounter = 1;

    private void addToCart(User user) {
        if (!cartList.contains(user)) {
            cartList.add(user);
            Toast.makeText(profile.this, "Item is on the way", Toast.LENGTH_SHORT).show();
            showNotification("Your order " + user.getName() + " is on the way", notificationIdCounter);
            showFloatingNotification("Item is on the way");
            sendConfirmationSMS(user);
            notificationIdCounter++;
        }
    }

    private void showFloatingNotification(String message) {
        Toast toast = Toast.makeText(profile.this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void showNotification(String message, int notificationId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.crop)
                .setContentTitle("Order Notification")
                .setContentText(message)
                .setAutoCancel(true);
        getSystemService(NotificationManager.class).notify(notificationId, builder.build());
    }

    private void sendConfirmationSMS(User user) {
        String phoneNumber = "7396723082";
        String message = "Your order " + user.getName() + " is confirmed. Thank you for shopping with us!";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            showFloatingNotification("SMS sent to " + phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            showFloatingNotification("Failed to send SMS");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String description = intent.getStringExtra("description");
            String price = intent.getStringExtra("price");
            int image = intent.getIntExtra("image", R.drawable.a);

            binding.itemName.setText(name);
            binding.itemDesc.setText(description);
            binding.itemPrice.setText(price);
            binding.itemImg.setImageResource(image);
        }

        Button addToCartButton = findViewById(R.id.item_cart);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User itemDetails = getItemDetails();
                addToCart(itemDetails);
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
                startActivity(new Intent(getApplicationContext(), cart.class));
                return true;
            }
            return false;
        });

    }
}