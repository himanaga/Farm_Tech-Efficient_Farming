package com.example.myapplication;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        initCategory();

        profilePic = findViewById(R.id.profilePic);

        Intent intent = getIntent();
        String profileUrl = intent.getStringExtra("profilePic");
        if (profileUrl != null && !profileUrl.isEmpty()) {
            Glide.with(MainActivity.this).load(profileUrl).into(profilePic);
        } else {
            Glide.with(MainActivity.this).load(R.drawable.acc).into(profilePic);
        }

     //   String profileUrl = intent.getStringExtra("profilePic");
        //Glide.with(MainActivity.this).load(profileUrl).into(profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Profile Pic Clicked", Toast.LENGTH_SHORT).show();
                Intent goToProfilePage = new Intent(MainActivity.this, profile2.class);
                goToProfilePage.putExtra("profileImageUrl", profileUrl);
                goToProfilePage.putExtra("profileUserName", intent.getStringExtra("userName"));
                goToProfilePage.putExtra("profileUserEmail", intent.getStringExtra("userEmail"));
                goToProfilePage.putExtra("profileUserPhone", intent.getStringExtra("userPhone"));

                startActivity(goToProfilePage);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.news);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Get the item ID outside the switch to avoid constant expression error
            int itemId = item.getItemId();
            if (itemId == R.id.news) {
                startActivity(new Intent(MainActivity.this,news.class));
                return true;
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(getApplicationContext(), cart.class));
                return true;
            } else if (itemId == R.id.customer) {
                startActivity(new Intent(getApplicationContext(), care.class));
                return true;
            }
            return false;
        });

    }

    private void initCategory(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference( "Category");
        ArrayList<Category> list=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Category.class));
                    }
                    if (list.size()>0){
                        binding.categories.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
                        RecyclerView.Adapter adapter = new CategoryAdapter(list);
                        binding.categories.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setVariable(){
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=binding.searchTxt.getText().toString();
                if (!text.isEmpty()){
                    Intent intent= new Intent(MainActivity.this,ListActivity.class);
                    intent.putExtra("text",text);
                    intent.putExtra("Is Search",true);
                    startActivity(intent);
                }
            }
        });
    }

}