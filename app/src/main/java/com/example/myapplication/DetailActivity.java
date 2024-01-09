package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityDetailBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    ActivityDetailBinding binding;
    private Farm object;
    private int num = 1;
    private ManagmentCart managmentCart;
    private DrawerLayout drawerLayout;
    private ImageView navprofileImageView;
    private TextView navNameTextView;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();

        //navigation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        View headerView = navigationView.getHeaderView(0);
        navprofileImageView = headerView.findViewById(R.id.navprofile);
        navNameTextView = headerView.findViewById(R.id.nav_name);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            // Retrieve user details from Firestore
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("user").document(userId);
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        // Log details to help identify any issues
                        Log.d("Firestore", "DocumentSnapshot data: " + document.getData());

                        // Corrected key names to match the Firestore document
                        String userName = document.getString("profileUserName");
                        String profileImageUrl = document.getString("profileImageUrl");

                        navNameTextView.setText(userName);
                        Glide.with(this)
                                .load(profileImageUrl)
                                .into(navprofileImageView);
                        if (navprofileImageView != null && profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(DetailActivity.this).load(profileImageUrl).into(navprofileImageView);
                        } else {
                            Glide.with(DetailActivity.this).load(R.drawable.acc).into(navprofileImageView);
                        }
                    }
                }
            });
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            startActivity(new Intent(DetailActivity.this, MainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.nav_settings ) {
            String emailAddress = "your@email.com";
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + emailAddress));
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);}
        } else if (item.getItemId() == R.id.nav_share) {
            //sharing app
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String link = "https://github.com/himanaga/Mfarm";
            String shareMessage = "Check out this awesome app: Farm Tech !" + link;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share the app via"));
            return true;
        } else if (item.getItemId() == R.id.nav_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About App")
                    .setMessage("This is a sample app. Version 1.0")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (item.getItemId() == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DetailActivity.this, login.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.care) {
            startActivity(new Intent(DetailActivity.this, care.class));
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        //profile
        MenuItem profile = menu.findItem(R.id.profile);

        return super.onCreateOptionsMenu(menu);
    }
    public void profile(MenuItem item) {
        // Handle the click event for the profile menu item
        Toast.makeText(this, "Profile Pic Clicked", Toast.LENGTH_SHORT).show();

        Intent goToProfilePage = new Intent(this, profile2.class);
        startActivity(goToProfilePage);
    }
    private  void searcbar(String searchText){
        if (!searchText.isEmpty()) {
            Intent intent = new Intent(DetailActivity.this, ListActivity.class);
            intent.putExtra("text", searchText);
            intent.putExtra("Is Search", true);
            startActivity(intent);
        }
    }
//navigation end

    private void getIntentExtra() {
        object = (Farm) getIntent().getSerializableExtra("object");
    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);


        Glide.with(DetailActivity.this).load(object.getImagePath()).into(binding.pic1);

        binding.detailtitle.setText(object.getTitle());
        binding.detailprice.setText("₹" + object.getPrice());
        binding.startxtdetail.setText(object.getStar() + " Rating");
        binding.descrip.setText(object.getDescription());
        binding.ratingBar2.setRating((float) object.getStar());
        binding.detailtotalprice.setText(num * object.getPrice() + " ₹");

        binding.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.setNumberInCart(num);
                managmentCart.insertFood(object);
            }
        });

        binding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num > 1) {
                    num = num - 1;
                    binding.num.setText(num + " ");
                    binding.detailtotalprice.setText("₹" + (num * object.getPrice()));
                }
            }
        });
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = num + 1;
                binding.num.setText(num + " ");
                binding.detailtotalprice.setText("₹" + (num * object.getPrice()));
            }
        });
    }
}
