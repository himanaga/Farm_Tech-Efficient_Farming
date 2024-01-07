package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityListBinding;
import com.example.myapplication.databinding.ActivityOrders2Binding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class orders2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private RecyclerView recyclerView;
    private orderAdapter adapter;
    TextView order;
    private DrawerLayout drawerLayout;
    private ImageView navprofileImageView;
    private TextView navNameTextView;
    private ActivityOrders2Binding binding;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrders2Binding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        ArrayList<Order2> ordersList = new ArrayList<>();
        orderAdapter2 orderAdapter = new orderAdapter2(ordersList);
        binding.order2list.setAdapter(orderAdapter);

        initList();

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
                            Glide.with(orders2.this).load(profileImageUrl).into(navprofileImageView);
                        } else {
                            Glide.with(orders2.this).load(R.drawable.acc).into(navprofileImageView);
                        }
                    }
                }
            });
        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            startActivity(new Intent(orders2.this, MainActivity.class));
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
            Intent intent = new Intent(orders2.this, login.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.care) {
            startActivity(new Intent(orders2.this, care.class));
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        if (searchView != null) {
            searchView.setQueryHint("Search Here...");

            searchView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                        // Perform search when Enter key is pressed
                        String searchText = searchView.getQuery().toString();
                        searcbar(searchText);
                        return true;
                    }
                    return false;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searcbar(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searcbar(newText);
                    return false;
                }
            });
        }
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
            Intent intent = new Intent(orders2.this, ListActivity.class);
            intent.putExtra("text", searchText);
            intent.putExtra("Is Search", true);
            startActivity(intent);
        }
    }
//navigation end


    private void initList() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ordersCollection = db.collection("orders");
        ArrayList<Order2> list = new ArrayList<>();
        binding.progressBar.setVisibility(View.VISIBLE);
            if (currentUser != null) {
                String userId = currentUser.getUid();
                ordersCollection.whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Check if the "items" field exists in the document
                            if (document.contains("items")) {
                                // Get the "items" array from the document
                                ArrayList<Map<String, Object>> items = (ArrayList<Map<String, Object>>) document.get("items");

                                // Iterate over each item in the "items" array
                                for (Map<String, Object> item : items) {
                                    // Extract the desired fields from each item
                                    String imagePath = (String) item.get("imagePath");
                                    String title = (String) item.get("title");
                                    Double price = (Double) item.get("price");
                                    Long numberInCart = (Long) item.get("numberInCart");

                                    // Create an Order2 object with the extracted fields
                                    Order2 order = new Order2(imagePath, title, price, numberInCart);

                                    // Add the Order2 object to the list
                                    list.add(order);
                                }
                            }
                        }

                        if (list.size() > 0) {
                            binding.order2list.setLayoutManager(new GridLayoutManager(orders2.this, 20, LinearLayoutManager.HORIZONTAL, false));
                            RecyclerView.Adapter adapter = new orderAdapter2(list);
                            binding.order2list.setAdapter(adapter);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    } else {
                        Log.e("Firebase", "Error fetching data: " + task.getException());
                    }
                });
            }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(orders2.this, MainActivity.class);
            startActivity(intent);
        }


    }
}