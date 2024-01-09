package com.example.myapplication;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.KeyEvent;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.main.MainFragment;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private ActivityMainBinding binding;
    private String profileUrl;
    ImageView profilePic;
    private ArrayList<Category> list = new ArrayList<>();
    Toolbar tb;
    private DrawerLayout drawerLayout;
    private TextView navNameTextView;
    private ImageView navprofileImageView;
    private LottieAnimationView animationView;
    //order

    private TextView o_name,o_quantity,o_price;
    private CircleImageView o_image;
    private RecyclerView recyclerView;
    private List<Order2> ordersList;
    private orderAdapter2 orderAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


         animationView = findViewById(R.id.no_net);
        if (isNetworkConnected()) {
            setupNormalContent();
            animationView.setVisibility(View.GONE);
        } else {
            animationView.setVisibility(View.VISIBLE);
            hideAllContent();
        }


        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
                Query query = databaseReference.orderByChild("Id").equalTo(0);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Category category = snapshot.getValue(Category.class);
                            if (category != null) {
                                // Handle the retrieved data, e.g., start a new activity
                                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                                intent.putExtra("CategoryId", category.getId());
                                intent.putExtra("CategoryName", category.getName());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        binding.title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
                Query query = databaseReference.orderByChild("Id").equalTo(1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Category category = snapshot.getValue(Category.class);
                            if (category != null) {
                                // Handle the retrieved data, e.g., start a new activity
                                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                                intent.putExtra("CategoryId", category.getId());
                                intent.putExtra("CategoryName", category.getName());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        binding.title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
                Query query = databaseReference.orderByChild("Id").equalTo(2);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Category category = snapshot.getValue(Category.class);
                            if (category != null) {
                                // Handle the retrieved data, e.g., start a new activity
                                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                                intent.putExtra("CategoryId", category.getId());
                                intent.putExtra("CategoryName", category.getName());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        binding.title3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
                Query query = databaseReference.orderByChild("Id").equalTo(3);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Category category = snapshot.getValue(Category.class);
                            if (category != null) {
                                // Handle the retrieved data, e.g., start a new activity
                                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                                intent.putExtra("CategoryId", category.getId());
                                intent.putExtra("CategoryName", category.getName());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        binding.title4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Price");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Worker worker = snapshot.getValue(Worker.class);
                            if (worker != null) {
                                // Handle the retrieved data, e.g., start a new activity
                                Intent intent = new Intent(MainActivity.this, workers.class);
                                startActivity(intent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //order
        o_image = findViewById(R.id.o_image);
        o_name = findViewById(R.id.o_name);
        o_price = findViewById(R.id.o_price);
        o_quantity = findViewById(R.id.o_quantity);
        recyclerView = findViewById(R.id.orderview);

        // Initialize orders list and adapter
      /*    ordersList = new ArrayList<>();
        orderAdapter2 = new orderAdapter2(ordersList, this);*/
      RecyclerView recyclerView = findViewById(R.id.order2list);
        ArrayList<Order2> ordersList = new ArrayList<>();
        orderAdapter2 orderAdapter2 = new orderAdapter2(ordersList);
        /*
        recyclerView.setAdapter(orderAdapter2);*/
    /*    binding.orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    CollectionReference ordersCollection = FirebaseFirestore.getInstance().collection("orders");
                    ordersCollection.whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ordersList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String orderId = document.getId();
                                String title = document.getString("title");
                                Double price = document.getDouble("price");
                                Long quantity = document.getLong("numberInCart");
                                String image = document.getString("imagePath");
                                // Check for null values
                                if (title != null && price != null && quantity != null && image != null) {
                                    Order2 order = new Order2(image, title, price, quantity);
                                    ordersList.add(order);
                                }
                            }

                            // Notify the adapter after all items are added
                            orderAdapter2.notifyDataSetChanged();

                            // Start the orders2 activity
                            Intent intent = new Intent(MainActivity.this, orders2.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });*/



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.news);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Get the item ID outside the switch to avoid constant expression error
            int itemId = item.getItemId();
            if (itemId == R.id.news) {
                startActivity(new Intent(MainActivity.this, news.class));
                return true;
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(getApplicationContext(), cart.class));
                return true;
            } else if (itemId == R.id.customer) {
                startActivity(new Intent(getApplicationContext(), care.class));
                return true;
            } else if (itemId == R.id.orderDetailsTextView) {
                startActivity(new Intent(getApplicationContext(), orders2.class));
                return true;
            }
            return false;
        });

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
                            Glide.with(MainActivity.this).load(profileImageUrl).into(navprofileImageView);
                        } else {
                            Glide.with(MainActivity.this).load(R.drawable.acc).into(navprofileImageView);
                        }
                    }
                }
            });
        }
    }
    //nav
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
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
          /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About App")
                    .setMessage("This is a sample app. Version 1.0")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();*/
            Intent intent = new Intent(MainActivity.this, aboutus.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.care) {
            startActivity(new Intent(MainActivity.this, care.class));
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
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
    private void hideAllContent() {
        binding.title.setVisibility(View.GONE);
        binding.title1.setVisibility(View.GONE);
        binding.title2.setVisibility(View.GONE);
        binding.title3.setVisibility(View.GONE);
        binding.title4.setVisibility(View.GONE);
    }
    private void setupNormalContent() {
        binding.title.setVisibility(View.VISIBLE);
        binding.title1.setVisibility(View.VISIBLE);
        binding.title2.setVisibility(View.VISIBLE);
        binding.title3.setVisibility(View.VISIBLE);
        binding.title4.setVisibility(View.VISIBLE);
    }
   private  void searcbar(String searchText){
       if (!searchText.isEmpty()) {
           Intent intent = new Intent(MainActivity.this, ListActivity.class);
           intent.putExtra("text", searchText);
           intent.putExtra("Is Search", true);
           startActivity(intent);
       }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }



}
