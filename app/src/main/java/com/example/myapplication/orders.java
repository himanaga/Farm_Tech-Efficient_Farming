package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ActivityOrdersBinding;
import com.example.myapplication.ui.main.MainFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class orders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private RecyclerView recyclerView;
    private orderAdapter adapter;
    TextView order;
    private DrawerLayout drawerLayout;
    private ImageView navprofileImageView;
    private TextView navNameTextView;
    private ActivityOrdersBinding binding;
    private TextView o_name,o_quantity,o_price;
    private CircleImageView o_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        order = findViewById(R.id.orderDetailsTextView);

        recyclerView = findViewById(R.id.orderview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Retrieve order details from intent
        List<Farm> orderItems = (List<Farm>) getIntent().getSerializableExtra("orderItems");
        if (orderItems != null) {
            // Set up the RecyclerView adapter
            adapter = new orderAdapter(orderItems);
            recyclerView.setAdapter(adapter);
            order.setVisibility(View.GONE);
        } else {
            order.setVisibility(View.VISIBLE);
        }

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
                            Glide.with(orders.this).load(profileImageUrl).into(navprofileImageView);
                        } else {
                            Glide.with(orders.this).load(R.drawable.acc).into(navprofileImageView);
                        }
                    }
                }
            });
        }


        //order trail1
        /*
        o_image = findViewById(R.id.o_image);
        o_name = findViewById(R.id.o_name);
        o_price = findViewById(R.id.o_price);
        o_quantity = findViewById(R.id.o_quantity);

         DocumentReference documentReference = FirebaseFirestore.getInstance().collection("orders").document("items");
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String title = document.getString("title");
                        String price = document.getString("price");
                        String quantity = document.getString("numberInCart");
                        String image = document.getString("imagePath");

                        Glide.with(orders.this).load(image).into(o_image);
                        o_name.setText(title);
                        o_price.setText(price);
                        o_quantity.setText(quantity);
                    } else {

                    }
                }
            });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference ordersCollection = db.collection("orders");
        ordersCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String orderId = document.getId();
                    String title = document.getString("title");
                    String price = document.getString("price");
                    String quantity = document.getString("numberInCart");
                    String image = document.getString("imagePath");

                    Glide.with(orders.this).load(image).into(o_image);
                    o_name.setText(title);
                    o_price.setText(price);
                    o_quantity.setText(quantity);
                }

            }
        });
        */

    }

@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            startActivity(new Intent(orders.this, MainActivity.class));
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
                Intent intent = new Intent(orders.this, signup.class);
                startActivity(intent);
                finish();
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
            Intent intent = new Intent(orders.this, ListActivity.class);
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
        }
        }

}



//gpt
/*

import android.os.Bundle;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList; // Import this if you're using ArrayList for your order data

public class orders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter; // Define the OrderAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_layout); // Replace with your actual layout file

        recyclerView = findViewById(R.id.your_recycler_view_id); // Replace with your actual RecyclerView ID

        // Check if recyclerView is not null before setting the layout manager
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Initialize and set up the adapter
            orderAdapter = new OrderAdapter(getOrderData()); // Pass your order data to the adapter
            recyclerView.setAdapter(orderAdapter);

            // Add other initialization code for your RecyclerView as needed
        } else {
            // Handle the case where recyclerView is null
        }

        // Rest of your onCreate method...
    }

    // Rest of your orders activity code...

    // Private method to get order data (replace this with your actual data retrieval logic)
    private ArrayList<Order> getOrderData() {
        // Replace this with your logic to fetch order data from wherever it comes
        // For example, you might fetch it from Firebase or another data source
        ArrayList<Order> orders = new ArrayList<>();
        // Add your order objects to the 'orders' list
        // Example:
        // orders.add(new Order(/* parameters */
        // orders.add(new Order(/* parameters */));
        // ...
   /*     return orders;
    }

    // Private OrderAdapter class
    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

        private ArrayList<Order> orderList; // Assuming Order is your data class

        // Constructor to receive the order data
        public OrderAdapter(ArrayList<Order> orderList) {
            this.orderList = orderList;
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Implement your onCreateViewHolder logic here
            // You'll need to inflate a layout for each item view
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.your_order_item_layout, parent, false);
            return new OrderViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            // Implement your onBindViewHolder logic here
            // You'll need to bind data to the views within the ViewHolder
            Order order = orderList.get(position);
            holder.bindOrder(order);
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        // ViewHolder class
        public class OrderViewHolder extends RecyclerView.ViewHolder {

            // Define your views within the ViewHolder
            private TextView orderNameTextView;
            private TextView orderDetailsTextView;

            public OrderViewHolder(@NonNull View itemView) {
                super(itemView);

                // Initialize your views here
                orderNameTextView = itemView.findViewById(R.id.your_order_name_text_view);
                orderDetailsTextView = itemView.findViewById(R.id.your_order_details_text_view);
            }

            // Method to bind data to the views
            public void bindOrder(Order order) {
                // Bind your order data to the views here
                orderNameTextView.setText(order.getName());
                orderDetailsTextView.setText(order.getDetails());
                // Add more bindings as needed
            }
        }
    }
}*/


//set 3
/*


package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.databinding.ActivityListBinding;
import com.example.myapplication.databinding.ActivityOrdersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class orders extends AppCompatActivity {
    ActivityOrdersBinding binding;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        initList();
    }
    private void initList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ordersCollection = db.collection("orders");
        ArrayList<Order2> list = new ArrayList<>();

        ordersCollection.get().addOnCompleteListener(task -> {
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
                            Order2 order = new Order2(imagePath,title, price, numberInCart );

                            // Add the Order2 object to the list
                            list.add(order);
                        }
                    }
                }

                if (list.size() > 0) {
                    binding.orderview.setLayoutManager(new GridLayoutManager(orders.this, 10, LinearLayoutManager.HORIZONTAL, false));
                    RecyclerView.Adapter adapter = new orderAdapter2(list);
                    binding.orderview.setAdapter(adapter);
                }
            } else {
                Log.e("Firebase", "Error fetching data: " + task.getException());
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(orders.this,MainActivity.class);
        startActivity(intent);
    }


}*/