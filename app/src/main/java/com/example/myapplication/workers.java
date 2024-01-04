package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.databinding.ActivityWorkersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class workers extends AppCompatActivity{
    ActivityWorkersBinding binding;
    private RecyclerView.Adapter adapterListFood;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding = ActivityWorkersBinding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());

        getIntentExtra();
        mAuth = FirebaseAuth.getInstance();
        initList();
    }

    private void initList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference( "Price");
        ArrayList<Worker> list=new ArrayList<>();
        binding.progressBar.setVisibility(View.VISIBLE);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Worker.class));
                    }
                    if (list.size()>0){
                        binding.workerlist.setLayoutManager(new GridLayoutManager(workers.this, 10, LinearLayoutManager.HORIZONTAL,false));
                        RecyclerView.Adapter adapter = new workerAdapter(list);
                        binding.workerlist.setAdapter(adapter);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getIntentExtra() {
        binding.title.setText("Workers Page");
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }
}