package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.databinding.ActivityListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ActivityListBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());

        getIntentExtra();

        Log.d("ListActivity", "categoryId: " + categoryId);
        Log.d("ListActivity", "categoryName: " + categoryName);
        Log.d("ListActivity", "searchText: " + searchText);
        Log.d("ListActivity", "isSearch: " + isSearch);
        mAuth = FirebaseAuth.getInstance();

        initList();

    }

    private void initList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference( "Farming");
        ArrayList<Farm> list=new ArrayList<>();
        binding.progressBar.setVisibility(View.VISIBLE);
        Log.d("ListActivity", "isSearch: " + isSearch);
        Query query;
        if (isSearch){
            query= myRef.orderByChild("Title").startAt(searchText).endAt(searchText +'\uf8ff');
        }else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Farm.class));
                    }
                    if (list.size()>0){
                        binding.farmViewlist.setLayoutManager(new GridLayoutManager(ListActivity.this, 10, LinearLayoutManager.HORIZONTAL,false));
                        RecyclerView.Adapter adapter = new ListAdapter(list);
                        binding.farmViewlist.setAdapter(adapter);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching data: " + error.getMessage());

            }
        });
    }


    private void getIntentExtra() {
        categoryId=getIntent().getIntExtra(  "CategoryId", 0);
        categoryName=getIntent().getStringExtra( "CategoryName");
        searchText=getIntent().getStringExtra(  "text");
        isSearch=getIntent().getBooleanExtra(  "isSearch", false);
        if (categoryName != null) {
            binding.title.setText(categoryName + " Page");
        } else {
            binding.title.setText("Farm Tech");
        }
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }
}